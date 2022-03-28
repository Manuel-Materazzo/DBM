package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.dto.BucketNameDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@AllArgsConstructor
public class ServiceFile {

    @Autowired
    private final ServiceFileStore fileStore;


    public String upload(MultipartFile file) {

        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        String path = String.format("%s/%s", BucketNameDTO.IMAGE.getBucketName(), "Immagine auto");
        String fileName = String.format("%s", file.getOriginalFilename());

        try {
            fileStore.upload(path, fileName, Optional.of(metadata), file.getInputStream());
            return path + "/" + fileName;
        } catch (IOException e) {
            return null;
        }

    }

    public Integer getAudioDuration(MultipartFile file){
        try {
            InputStream is = file.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
            AudioFormat format = audioInputStream.getFormat();
            long audioFileLength = audioInputStream.getFrameLength();
            int frameSize = format.getFrameSize();
            float frameRate = format.getFrameRate();
            return Math.round(audioFileLength / (frameSize * frameRate));
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

}
