package com.ap2.replocker.report_collection;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.admin.AdminRepository;
import com.ap2.replocker.common.PageResponse;
import com.ap2.replocker.exception.custom.AdminNotFoundException;
import com.ap2.replocker.exception.custom.CollectionNotFoundException;
import com.ap2.replocker.exception.custom.DuplicateCollectionException;
import com.ap2.replocker.report_collection.access_request.access_token.AccessTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReportCollectionService {
    private final ReportCollectionRepository reportCollectionRepository;
    private final ReportCollectionMapper reportCollectionMapper;
    private final AccessTokenService accessTokenService;
    private final AdminRepository adminRepository;

    public ReportCollectionResponse createCollection(ReportCollectionRequest request, UUID adminId) {
        Admin admin = this.adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));

        this.validateUniqueName(request.name(), adminId);

        return this.reportCollectionMapper.toReportCollectionResponse(
                this.reportCollectionRepository.save(
                        this.reportCollectionMapper.toReportCollection(request, admin)));
    }

    public PageResponse<ReportCollectionResponse> getCollectionsByAdmin(UUID adminId, int page, int size) {
        Page<ReportCollection> collections = this.reportCollectionRepository.findByAdminId(
                adminId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"))
        );
        return PageResponse.fromPage(collections.map(this.reportCollectionMapper::toReportCollectionResponse));
    }

    public ReportCollectionResponse getCollectionByIdAndAdmin(UUID collectionId, UUID adminId) {
        return this.reportCollectionMapper.toReportCollectionResponse(this.reportCollectionRepository.findByIdAndAdminId(collectionId, adminId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId)));
    }

    public ReportCollectionResponse updateCollection(UUID collectionId, ReportCollectionRequest request, UUID adminId) {
        ReportCollection collection = this.reportCollectionRepository.findByIdAndAdminId(collectionId, adminId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));

        if (!collection.getName().equalsIgnoreCase(request.name())) {
            this.validateUniqueName(request.name(), adminId);
        }

        collection.setName(request.name());
        collection.setDescription(request.description());
        collection.setLocked(request.isLocked());
        collection.setPublished(request.isPublished());

        return this.reportCollectionMapper.toReportCollectionResponse(this.reportCollectionRepository.save(collection));
    }

    public PageResponse<ReportCollectionResponse> getCollectionsPublic(int page, int size) {
        Page<ReportCollection> collections = this.reportCollectionRepository.findByPublishedTrue(
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"))
        );

        return PageResponse.fromPage(collections.map(this.reportCollectionMapper::toReportCollectionResponse));
    }

    public ReportCollectionResponse getCollectionWithAccessCheck(UUID collectionId, String accessToken) {
        ReportCollection collection = this.reportCollectionRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));

        if (collection.isLocked()) {
            this.accessTokenService.validateToken(
                accessToken,
                collection.getId()
            );
        }

        return this.reportCollectionMapper.toReportCollectionResponse(collection);
    }

    public void deleteCollection(UUID collectionId, UUID adminId) {
        ReportCollection collection = this.reportCollectionRepository.findByIdAndAdminId(collectionId, adminId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));

        this.reportCollectionRepository.delete(collection);
    }

    private void validateUniqueName(String name, UUID adminId) {
        if (this.reportCollectionRepository.existsByNameAndAdminId(name, adminId)) {
            throw new DuplicateCollectionException(name);
        }
    }
}
