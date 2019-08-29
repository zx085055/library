package com.tgfc.library.service.imp;

import com.tgfc.library.service.IPhotoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;

@Service
public class PhotoService implements IPhotoService {

    @Value("${file.root.path}")
    String filePath;

    @Value("${file.path}")
    private String filePath1;

    @Override
    public void uploadPhoto(MultipartFile file,String newName) {
//        if(file == null || file.length == 0)
//            throw new FileNotFoundException();


            String fileName = file.getOriginalFilename();
         fileName=newName+".jpg";
            File dest = new File( this.filePath + fileName);
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
    public String getPhoto( HttpServletResponse  response,String Photo) {


        String path = this.filePath +"/"+ Photo;

        return path ;


//        @Override
//        public byte[] getPhoto( HttpServletResponse  response,String Photo) {
//            String path = this.filePath + Photo;
//            InetAddress address = InetAddress.getLocalHost();
//            address.getHostAddress();
//            InputStream in = null;
//            FileInputStream fis = null;
//            OutputStream os = null;
//            byte[] buffer = new byte[1024 * 8];
//            try {
//                fis = new FileInputStream(path);
//                os = response.getOutputStream();
//                int count = 0;
//                while ((count = fis.read(buffer)) != -1) {
//                    os.write(buffer, 0, count);
//                    os.flush();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                fis.close();
//                os.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return buffer ;


//        Path path = Paths.get(rpath);
//        byte[] data = new byte[0];
//
//        try {
//            data = Files.readAllBytes(path);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return data;


    }


}
