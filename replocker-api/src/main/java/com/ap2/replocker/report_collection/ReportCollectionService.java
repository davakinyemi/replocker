package com.ap2.replocker.report_collection;

import com.ap2.replocker.admin.Admin;
import com.ap2.replocker.admin.AdminRepository;
import com.ap2.replocker.common.PageResponse;
import com.ap2.replocker.exception.custom.AdminNotFoundException;
import com.ap2.replocker.exception.custom.DuplicateCollectionException;
import com.ap2.replocker.report_collection.report.ReportResponse;
import com.ap2.replocker.report_collection.report.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReportCollectionService {
    private final ReportCollectionRepository reportCollectionRepository;
    private final ReportCollectionMapper reportCollectionMapper;
    private final AdminRepository adminRepository;

    public ReportCollectionResponse createCollection(ReportCollectionRequest request, UUID adminId) {
        this.validateUniqueName(request.name());
        Admin admin = this.adminRepository.findById(adminId)
                .orElseThrow(() -> new AdminNotFoundException(adminId));

        ReportCollection reportCollection = this.reportCollectionRepository.save(this.reportCollectionMapper.toReportCollection(request, admin));
        return this.reportCollectionMapper.toReportCollectionResponse(reportCollection);
    }

    public PageResponse<ReportCollectionResponse> getPublicCollections(int page, int size) {
        Page<ReportCollection> collections = this.reportCollectionRepository.findByIsPublishedTrueAndIsLockedFalse(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"))
        );

        Page<ReportCollectionResponse> responsePage = collections.map(this.reportCollectionMapper::toReportCollectionResponse);
        return PageResponse.fromPage(responsePage);
    }

    private void validateUniqueName(String name) {
        if (this.reportCollectionRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateCollectionException(name);
        }
    }
}
