package com.example.crm.uploads;

import com.example.crm.exceptions.handlers.FileSizeLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3ImageUpload {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${app.upload.max-size-bytes}")
    private long MAX_FILE_SIZE;

@Autowired
private S3Client s3Client;

    public String uploadImageToS3(MultipartFile file) {
        try {
            if (s3Client == null ||file == null || file.isEmpty()) {
                return null;
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new FileSizeLimitExceededException("Max image size is 2MB allowed");
            }

            // Extract extension
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // Generate unique key (NO folders)
            String key = UUID.randomUUID().toString() + extension;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes())
            );

            // Return FULL URL (same as reference code)
            return "https://" + bucketName + ".s3.amazonaws.com/" + key;

        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        }
    }

    public void deleteImageFromS3(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) return;

        String bucketUrl = "https://" + bucketName + ".s3.amazonaws.com/";

        if (!imageUrl.startsWith(bucketUrl)) {
            System.out.println("Invalid S3 URL format: " + imageUrl);
            return;
        }

        // Extract key from URL
        String key = imageUrl.substring(bucketUrl.length());

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
            System.out.println("Deleted S3 Image: " + key);
        } catch (Exception e) {
            System.out.println("Failed to delete S3 Image: " + e.getMessage());
        }
    }
}
