package com.ap2.replocker.file;

import com.ap2.replocker.exception.custom.InvalidFileTypeException;
import com.ap2.replocker.report_collection.report.ReportType;
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
    ) throws IOException {
        this.validateFileType(sourceFile);

        final String fileUploadSubPath = "report_collection" + separator + reportCollectionId;

        return this.uploadFile(sourceFile, fileUploadSubPath, reportCollectionId);
    }

    public String uploadFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull String fileUploadSubPath,
            @Nonnull String reportCollectionId
    ) throws IOException {
        final String finalUploadPath = this.fileUploadPath + separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists() && !targetFolder.mkdirs()) {
            log.warn("Could not create folder: {}", targetFolder);
            throw new IOException("Failed to create directory: " + targetFolder);
        }

        final String fileExtension = this.getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + separator + currentTimeMillis() + "." + fileExtension;
        // Path targetPath = Paths.get(targetFilePath);

        try {
            Files.write(Paths.get(targetFilePath), sourceFile.getBytes());
            log.info("Uploaded file: {}", targetFilePath);
            return targetFilePath;
        } catch (IOException e) {
            log.error("Could not upload file: {}", targetFilePath, e);
            throw new IOException("File upload failed: " + targetFilePath, e);
        }

    }

    public void deleteFile(@Nonnull String filePath) {
        try {
            Path targetPath = Paths.get(filePath);
            Files.delete(targetPath);
        } catch (IOException e) {
            log.error("Could not delete file: {}", filePath, e);
        }
    }

    private void validateFileType(MultipartFile file) {
        try {
            ReportType.fromMimeType(file.getContentType());
        } catch (IllegalArgumentException e) {
            throw new InvalidFileTypeException(file.getContentType());
        }
        /* String fileType = file.getContentType();
        if (!"text/csv".equals(fileType) && !"application/vnd.ms-excel".equals(fileType)) {
            throw new InvalidFileTypeException(fileType);
        } */
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) return "";

        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) return "";

        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }
}
