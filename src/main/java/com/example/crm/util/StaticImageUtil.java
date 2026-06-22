package com.example.crm.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class StaticImageUtil {

    private StaticImageUtil() {}

    public static MultipartFile getMultipartFileFromStatic(String imagePath) throws IOException {

        ClassPathResource resource = new ClassPathResource(imagePath);
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());

        String filename = resource.getFilename();
        String contentType = detectContentType(filename);

        return new ByteArrayMultipartFile(
                bytes,
                "file",
                filename,
                contentType
        );
    }

    private static String detectContentType(String filename) {
        if (filename == null) return "application/octet-stream";
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) return "image/jpeg";
        if (filename.endsWith(".png")) return "image/png";
        if (filename.endsWith(".gif")) return "image/gif";
        return "application/octet-stream";
    }
}


