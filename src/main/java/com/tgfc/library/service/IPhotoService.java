package com.tgfc.library.service;

import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IPhotoService  {
    void uploadPhoto(MultipartFile file,String newName) ;
    String getPhotoUrl( String Photo) throws IOException;
    public boolean deletePhoto( String newName);
    public byte[] getPhoto(String fileName) throws FileNotFoundException;
    public String getApiPhotoUrl(String photoFileName);
}
