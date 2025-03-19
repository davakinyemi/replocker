package com.ap2.replocker.file;

import com.ap2.replocker.exception.custom.InvalidFileTypeException;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;

/**
 * @author Dave AKN
 * @version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    @Value("${application.file.uploads.report-output-path}")
    private String fileUploadPath;

    public String saveFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull String reportCollectionId
    ) {
        this.validateFileType(sourceFile);

        final String fileUploadSubPath = "report_collection" + separator + reportCollectionId;

        return this.uploadFile(sourceFile, fileUploadSubPath, reportCollectionId);
    }

    public String uploadFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull String fileUploadSubPath,
            @Nonnull String reportCollectionId
    ) {
        final String finalUploadPath = this.fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if (!folderCreated) {
                log.warn("Could not create folder: {}", targetFolder);
                return null;
            }
        }

        final String fileExtension = this.getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + separator + reportCollectionId + separator + currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);

        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("Uploaded file: {}", targetFilePath);
            return targetFilePath;
        } catch (IOException exception) {
            log.error("Could not upload file: {}", targetFilePath, exception);
        }
        return null;
    }

    private void validateFileType(MultipartFile file) {
        String fileType = file.getContentType();
        if (!"text/csv".equals(fileType) && !"application/vnd.ms-excel".equals(fileType)) {
            throw new InvalidFileTypeException("", fileType);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) return "";

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) return "";

        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
