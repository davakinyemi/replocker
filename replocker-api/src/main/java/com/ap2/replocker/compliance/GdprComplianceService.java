package com.ap2.replocker.compliance;

import com.ap2.replocker.report_collection.access_request.access_token.AccessToken;
import com.ap2.replocker.report_collection.access_request.access_token.AccessTokenRepository;
import com.ap2.replocker.report_collection.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class GdprComplianceService {
    private final AccessTokenRepository accessTokenRepository;
    private final ReportRepository reportRepository;

    public void purgeExpiredData() {
        this.purgeAccessTokens();
        this.purgeReports();
    }

    private void purgeAccessTokens() {
        List<AccessToken> expiredTokens = this.accessTokenRepository.findByExpiresAtBefore(LocalDateTime.now());
        this.accessTokenRepository.deleteAll(expiredTokens);
    }

    private void purgeReports() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(365);
        this.reportRepository.deleteByCreatedDateBefore(threshold);
    }
}
