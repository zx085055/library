package com.tgfc.library.service;

import org.springframework.web.multipart.MultipartFile;


import java.io.FileNotFoundException;
import java.io.IOException;

public interface IPhotoService {

    void uploadPhoto(MultipartFile file, String newName);

    String getPhotoUrl(String Photo) throws IOException;

    boolean deletePhoto(String newName);

    byte[] getPhoto(String fileName) throws FileNotFoundException;

    String getApiPhotoUrl(String photoFileName);
}
