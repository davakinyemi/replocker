package com.ap2.replocker.report_collection.report;

import com.ap2.replocker.common.PageResponse;
import com.ap2.replocker.exception.custom.CollectionNotFoundException;
import com.ap2.replocker.exception.custom.DuplicateReportException;
import com.ap2.replocker.exception.custom.InvalidFileTypeException;
import com.ap2.replocker.exception.custom.ReportNotFoundException;
import com.ap2.replocker.file.FileService;
import com.ap2.replocker.report_collection.ReportCollection;
import com.ap2.replocker.report_collection.ReportCollectionRepository;
import com.ap2.replocker.report_collection.access_request.access_token.AccessTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final AccessTokenService accessTokenService;
    private final ReportCollectionRepository reportCollectionRepository;
    private final FileService fileService;

    public ReportResponse createReport(ReportRequest reportRequest, UUID adminId) throws IOException {
        ReportCollection reportCollection = this.reportCollectionRepository.findByIdAndAdminId(
                reportRequest.reportCollectionId(),
                adminId
        ).orElseThrow(() -> new CollectionNotFoundException(reportRequest.reportCollectionId()));

        this.validateUniqueName(reportRequest.name(), reportRequest.reportCollectionId());

        String filePath;

        try {
            filePath = this.fileService.saveFile(reportRequest.file(), reportCollection.getId().toString());
        } catch (IOException e) {
            throw new RuntimeException("File upload failed", e);
        }

        Report report = this.reportMapper.toReport(reportRequest, reportCollection);
        report.setFilePath(filePath);
        report.setSizeBytes(reportRequest.file().getSize());
        report.setType(ReportType.fromMimeType(reportRequest.file().getContentType()));
        /* report.setType(ReportType.valueOf(
                Objects.requireNonNull(reportRequest.file().getContentType()).split("/")[1].toUpperCase()
        )); */

        return this.reportMapper.toReportResponse(this.reportRepository.save(report));
    }

    public PageResponse<ReportResponse> getReportsByCollectionAndAdmin(UUID collectionId, UUID adminId, int page, int size) {
        ReportCollection collection = this.reportCollectionRepository.findByIdAndAdminId(collectionId, adminId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));

        Page<Report> reports = this.reportRepository.findByReportCollectionId(
                collectionId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"))
        );
        return PageResponse.fromPage(reports.map(this.reportMapper::toReportResponse));
    }

    public PageResponse<ReportResponse> getReportsByCollection(UUID collectionId, int page, int size, String accessToken) {
        ReportCollection collection = this.reportCollectionRepository.findById(collectionId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));

        if (collection.isLocked()) {
            this.accessTokenService.validateToken(accessToken, collection.getId());
        }

        Page<Report> reports = this.reportRepository.findByReportCollectionId(
                collectionId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"))
        );
        return PageResponse.fromPage(reports.map(this.reportMapper::toReportResponse));
    }

    public ReportResponse getReportByCollection(UUID reportCollectionId, UUID reportId, String accessToken) {
        Report report = this.reportRepository.findByIdAndReportCollectionId(reportId, reportCollectionId)
                .orElseThrow(() -> new ReportNotFoundException(reportId));

        if (report.getReportCollection().isLocked()) {
            this.accessTokenService.validateToken(accessToken, report.getReportCollection().getId());
        }

        return this.reportMapper.toReportResponse(report);
    }

    public ReportResponse updateReport(
        UUID collectionId,
        UUID reportId,
        ReportRequest request,
        MultipartFile file,
        UUID adminId
    ) throws IOException {
        ReportCollection collection = this.reportCollectionRepository.findByIdAndAdminId(collectionId, adminId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));

        Report report = this.reportRepository.findByIdAndReportCollectionId(reportId, collectionId)
                .orElseThrow(() -> new ReportNotFoundException(reportId));

        if (!report.getName().equals(request.name()) && this.reportRepository.existsByNameAndCollectionIdExcludingId(
                request.name(), collectionId, reportId
        )) {
            throw new DuplicateReportException(request.name());
        }

        if (file != null && !file.isEmpty()) {
            this.validateFileType(file);
            String newFilePath = this.fileService.saveFile(file, collectionId.toString());

            this.fileService.deleteFile(report.getFilePath());

            report.setFilePath(newFilePath);
            report.setSizeBytes(file.getSize());
            report.setType(ReportType.fromMimeType(file.getContentType()));
            /* report.setType(ReportType.valueOf(
                    Objects.requireNonNull(file.getContentType()).split("/")[1].toUpperCase()
            )); */

        }

        report.setName(request.name());

        return this.reportMapper.toReportResponse(this.reportRepository.save(report));
    }

    public void deleteReport(UUID collectionId, UUID reportId, UUID adminId) {
        ReportCollection collection = this.reportCollectionRepository.findByIdAndAdminId(collectionId, adminId)
                .orElseThrow(() -> new CollectionNotFoundException(collectionId));

        Report report = this.reportRepository.findByIdAndReportCollectionId(reportId, collectionId)
                .orElseThrow(() -> new ReportNotFoundException(reportId));

        this.fileService.deleteFile(report.getFilePath());

        this.reportRepository.delete(report);
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void purgeOldReports() {
        LocalDateTime threshold = LocalDateTime.now().minusYears(1);
        this.reportRepository.deleteAll(this.reportRepository.findByCreatedDateBefore(threshold));
    }

    private void validateUniqueName(String name, UUID collectionId) {
        if (this.reportRepository.existsByNameAndReportCollectionId(name, collectionId)) {
            throw new DuplicateReportException(name);
        }
    }

    private void validateFileType(MultipartFile file) {
        try {
            ReportType.fromMimeType(file.getContentType());
        } catch (IllegalArgumentException e) {
            throw new InvalidFileTypeException(file.getContentType());
        }
        /* if (!List.of("text/csv", "application/vnd.ms-excel").contains(file.getContentType())) {
            throw new InvalidFileTypeException(file.getContentType());
        } */
    }
}
