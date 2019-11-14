package com.tgfc.library.service.imp;

import com.tgfc.library.service.IPhotoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PhotoService implements IPhotoService {

    private String imageRootUrl = "/image/";

    @Value("${file.root.path}")
    String filePath;

    @Value("${nginx.port}")
    String filePort;

    @Value("${file.photo.protocol}")
    String protocol;

    @Value("${file.photo.host}")
    String host;

    @Override
    public void uploadPhoto(MultipartFile file, String newName) {
        String fileName = newName;
        File dest = new File(this.filePath + fileName);


        try {
            if (!dest.getParentFile().exists()) {
                if (!dest.getParentFile().mkdirs()) {
                    throw new IOException();
                }
            }
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPhotoUrl(String photoFileName) {

        String urlString = "";
        try {
            String path = imageRootUrl + photoFileName;
            URL url = new URL(protocol, host, Integer.valueOf(filePort), path);
            System.out.println(url.toString() + "?");
            urlString = url.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlString;

    }

    @Override
    public boolean deletePhoto(String newName) {
        File file = new File(this.filePath + newName);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }


}
