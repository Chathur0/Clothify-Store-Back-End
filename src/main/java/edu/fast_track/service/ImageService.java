package edu.fast_track.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String uploadProfile(MultipartFile multipartFile) throws IOException;

    boolean deleteProfile(String image) throws Exception;

    boolean deleteProduct(String image) throws IOException;

    String uploadProduct(MultipartFile image) throws IOException;
}
