package com.ap2.replocker.exception.custom;

import java.util.UUID;

/**
 * @author Dave AKN
 * @version 1.0
 */
public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(UUID reportId) {
        super(reportId.toString());
    }
}
