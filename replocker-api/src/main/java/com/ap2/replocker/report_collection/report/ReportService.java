package com.ap2.replocker.report_collection.report;

import com.ap2.replocker.exception.custom.DuplicateReportException;
import com.ap2.replocker.file.FileService;
import com.ap2.replocker.report_collection.ReportCollectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final ReportCollectionRepository reportCollectionRepository;
    private final FileService fileService;

    @Scheduled(cron = "0 0 3 * * *")
    public void purgeOldReports() {
        LocalDateTime threshold = LocalDateTime.now().minusYears(1);
        this.reportRepository.deleteAll(this.reportRepository.findByCreatedDateBefore(threshold));
    }

    private void validateUniqueName(String name, UUID collectionId) {
        if (this.reportRepository.existsByNameAndCollectionId(name, collectionId)) {
            throw new DuplicateReportException(name);
        }
    }


}
