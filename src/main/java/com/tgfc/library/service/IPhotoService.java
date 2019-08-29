package com.tgfc.library.service;

import com.tgfc.library.request.AddBook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface IPhotoService  {

    void uploadPhoto(MultipartFile file,String newName) ;
    byte[] getPhoto(HttpServletResponse response, String Photo) ;
}
