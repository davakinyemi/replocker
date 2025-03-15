package com.ap2.replocker.report_collection;

import com.ap2.replocker.report_collection.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ReportCollectionController {
    private final ReportCollectionService reportCollectionService;
    private final ReportService reportService;
}
