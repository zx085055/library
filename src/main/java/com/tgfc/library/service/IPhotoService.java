package com.tgfc.library.service;

import com.tgfc.library.request.AddBook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IPhotoService  {

    void uploadPhoto(MultipartFile file,String newName) ;
    String getPhotoUrl( String Photo) throws IOException;
    public boolean deletePhoto( String newName);
    public byte[] getPhoto(String fileName) throws FileNotFoundException;
}
