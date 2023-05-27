package com.shop.service;


import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{

        UUID uuid = UUID.randomUUID(); //서로 다른객체에 이름 부여
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // .이후부터 가져와서 extension에 넣음(확장자)
        String savedFileName = uuid.toString() + extension; //UUID+확장자를 savedFileName에 집어넣음
        String fileUploadFullUrl = uploadPath + "/" +savedFileName; // (업로드 경로+/+UUID+확장자)를 fileUploadFullUrl에 넣음
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); //FileOutputStream:바이트 단위의 출력을 내보내는 클래스, 생성자로 파일이 저장될 위치와 파일의 이름을 주어 파일에쓸 출력스트림 제작
        fos.write(fileData); //fileData를 파일 출력 스트림에 입력
        fos.close();
        return savedFileName; //업로드된 파일의 이름을 반환

    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);

        if(deleteFile.exists()) //filePath를 받아와서 File생성후 File이 존재하면
        {
            deleteFile.delete();  //해당 파일 삭제
            log.info("파일을 삭제하였습니다.");

        }
        else //존재하지않으면
        {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}
