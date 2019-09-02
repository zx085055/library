package com.tgfc.library.service.imp;

import com.tgfc.library.service.IPhotoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

@Service
public class PhotoService implements IPhotoService {

    private String imageRuel = File.separator + "image" + File.separator;

    @Value("${file.root.path}")
    String filePath;

    @Value("${file.path}")
    private String filePath1;

    @Override
    public void uploadPhoto(MultipartFile file, String newName) {
        String fileName = file.getOriginalFilename();
        fileName = newName + ".jpg";
        File dest = new File(this.filePath + fileName);
        try {
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPhotoUrl(String photoFileName) {
        String a = imageRuel;
        String urlString="";
        try {
            InetAddress address = InetAddress.getLocalHost();

            try {
                String protocol = "http";
                String host = address.getHostAddress().toString();
                int port = 8080;
                String path = imageRuel + photoFileName;
                URL url = new URL(protocol, host, port, path);
                System.out.println(url.toString() + "?");
                urlString = url.toString();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return urlString;

    }
    @Override
    public boolean  deletePhoto(MultipartFile files, String newName) {
        File file = new File(newName);
        if (file.isFile() && file.exists()) {
            file.delete();//"刪除單個檔案"+name+"成功！"
            return true;
        }//"刪除單個檔案"+name+"失敗！"
        return false;

    }

}
