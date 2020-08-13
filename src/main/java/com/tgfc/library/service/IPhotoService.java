package com.tgfc.library.service;

import org.springframework.web.multipart.MultipartFile;

public interface IPhotoService {

    void uploadPhoto(MultipartFile file, String newName);

    String getPhotoUrl(String Photo);

    boolean deletePhoto(String newName);

}
