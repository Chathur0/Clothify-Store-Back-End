package edu.fast_track.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final Cloudinary cloudinary;
    @Override
    public String uploadProfile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader().upload(
                multipartFile.getBytes(),
                Map.of(
                        "public_id", UUID.randomUUID().toString(),
                        "folder", "profile-image"
                )
        ).get("url").toString();
    }
    @Override
    public boolean deleteProfile(String image) throws Exception {
        return "ok".equals(cloudinary.uploader().destroy(
                "profile-image/" + getPublicIdFromUrl(image), Map.of()
        ).get("result"));
    }
    @Override
    public String uploadProduct(MultipartFile image) throws IOException {
        return cloudinary.uploader().upload(
                image.getBytes(),
                Map.of(
                        "public_id", UUID.randomUUID().toString(),
                        "folder", "product-image"
                )
        ).get("url").toString();
    }
    @Override
    public boolean deleteProduct(String image) throws IOException {
        return "ok".equals(cloudinary.uploader().destroy(
                "product-image/" + getPublicIdFromUrl(image), Map.of()
        ).get("result"));
    }
    private String getPublicIdFromUrl(String imageUrl) {
        String[] parts = imageUrl.split("/");
        String fileNameWithExtension = parts[parts.length - 1];
        return  fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf('.'));
    }
}
