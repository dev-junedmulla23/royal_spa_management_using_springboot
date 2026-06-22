package com.example.crm.uploads;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CloudinaryImageUpload {
//    private final Cloudinary cloudinary;
//
//    //====================================================================================
//
//
//    @SuppressWarnings("rawtypes")
//    public Map<String,String> uploadUserImage(MultipartFile file) throws IOException {
//        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
//                ObjectUtils.asMap("folder", "user_images")); // optional: store in folder
//
//        return Map.of(
//                "url", uploadResult.get("secure_url").toString(),
//                "publicId", uploadResult.get("public_id").toString()
//        ); // Cloudinary returns a public image URL
//    }
//
//    // Overload for File
//    @SuppressWarnings("rawtypes")
//    public Map<String,String> uploadUserImage(File file) throws IOException {
//        Map uploadResult = cloudinary.uploader().upload(file,
//                ObjectUtils.asMap("folder", "user_images"));
//        return Map.of(
//                "url", uploadResult.get("secure_url").toString(),
//                "publicId", uploadResult.get("public_id").toString()
//        );
//    }
//
//
//    public void deleteImage(String publicId) throws IOException {
//        if (publicId != null && !publicId.isEmpty()) {
//            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//        }
//    }
}

