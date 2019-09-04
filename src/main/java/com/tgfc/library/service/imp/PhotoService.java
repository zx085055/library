package com.tgfc.library.service.imp;

import com.mysql.fabric.Server;
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

    private String imageRuel = File.separator + "image" + File.separator;

    @Value("${file.root.path}")
    String filePath;
    @Value("${file.root.port}")
    String filePort;

    @Value("${file.path}")
    private String filePath1;

    @Value("${file.photo.path}")
     String urlString;

    @Override
    public void uploadPhoto(MultipartFile file, String newName) {
        String fileName = newName + ".jpg";
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
    public String getPhotoUrl(String photoFileName)throws IOException {

        String urlString="";
        try {
            InetAddress address = InetAddress.getLocalHost();

            try {

                String protocol = "http";
                String host = address.getHostAddress();
                Socket  point = new Socket();
                //point.getLocalPort();


                String path = imageRuel + photoFileName;
                URL url = new URL(protocol, host, point.getLocalPort(), path);
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
    public boolean  deletePhoto( String newName) {
        File file = new File(this.filePath + newName);
        if (file.isFile() && file.exists()) {
            file.delete();//"刪除單個檔案"+name+"成功！"
            return true;
        }//"刪除單個檔案"+name+"失敗！"
        return false;

    }
    @Override
    public byte[] getPhoto(String fileName) throws FileNotFoundException {
        String rpath = this.filePath + fileName;
        Path path = Paths.get(rpath);
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
            return data;
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }
    @Override
    public String getApiPhotoUrl(String photoFileName)throws IOException {
       String url =urlString+photoFileName;


        return url;

    }

}
