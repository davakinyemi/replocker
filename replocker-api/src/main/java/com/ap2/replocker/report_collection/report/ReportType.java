package com.ap2.replocker.report_collection.report;

import java.util.Arrays;

public enum ReportType {
    CSV("text/csv"),
    XLS("application/vnd.ms-excel"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    XLSM("application/vnd.ms-excel.sheet.macroEnabled.12"),
    XLSB("application/vnd.ms-excel.sheet.binary.macroEnabled.12"),
    XLTX("application/vnd.openxmlformats-officedocument.spreadsheetml.template")
    ;

    private final String mimeType;

    ReportType(String mimeType) {
        this.mimeType = mimeType;
    }

    public static ReportType fromMimeType(String mimeType) {
        return Arrays.stream(values())
                .filter(reportType -> reportType.mimeType.equalsIgnoreCase(mimeType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid mime type: " + mimeType));
    }
}
