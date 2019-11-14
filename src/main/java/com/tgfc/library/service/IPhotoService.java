package com.tgfc.library.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;

public interface IPhotoService {

    void uploadPhoto(MultipartFile file, String newName);

    String getPhotoUrl(String Photo) ;

    boolean deletePhoto(String newName);

}
