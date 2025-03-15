package com.ap2.replocker.report_collection;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportCollectionService {
    private final ReportCollectionRepository reportCollectionRepository;
    private final ReportCollectionMapper reportCollectionMapper;
}
