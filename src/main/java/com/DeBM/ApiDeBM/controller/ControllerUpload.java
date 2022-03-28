package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.service.impl.ServiceFile;
import com.DeBM.ApiDeBM.dto.FileDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

import static org.apache.http.entity.ContentType.*;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;

@RestController
@RequestMapping(path = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
public class ControllerUpload {

    @Autowired
    ServiceFile service;


    @PostMapping("/image")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileDTO> handleImageUpload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Il file è vuoto");
        }

        if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                IMAGE_BMP.getMimeType(),
                IMAGE_GIF.getMimeType(),
                IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Il file non è un'immagine");
        }

        String result = service.upload(file);

        if (result == null)
            throw new GeneralDataException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload fallito");

        return ResponseEntity.ok(FileDTO.builder().path("https://s3.eu-west-3.amazonaws.com/" + result).build());
    }

    @PostMapping("/song")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileDTO> handleSongUpload(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Il file è vuoto");
        }

        String result = service.upload(file);

        if (result == null)
            throw new GeneralDataException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload fallito");

        Integer length = service.getAudioDuration(file);

        if (length == null)
            throw new GeneralDataException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Impossibile calcolare la lunghezza del file audio");

        return ResponseEntity.ok(FileDTO.builder()
                .path("https://s3.eu-west-3.amazonaws.com/" + result)
                .length(length)
                .build());
    }


}
