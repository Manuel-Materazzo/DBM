package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.dto.StatisticheDTO;
import com.DeBM.ApiDeBM.repository.*;
import com.DeBM.ApiDeBM.service.Interface.InterfaceStatistiche;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceStatistiche implements InterfaceStatistiche {

    @Autowired
    private IRepositoryGeneri iRepositoryGeneri;
    @Autowired
    private IRepositoryAlbum iRepositoryAlbum;
    @Autowired
    private IRepositoryBrani iRepositoryBrani;
    @Autowired
    private IRepositoryArtisti iRepositoryArtisti;


    public StatisticheDTO getStatistiche() {
        return StatisticheDTO.builder()
                .totaleArtisti((int) iRepositoryArtisti.count())
                .totaleBrani((int) iRepositoryBrani.count())
                .totaleALbum((int) iRepositoryAlbum.count())
                .totaleGeneri((int) iRepositoryGeneri.count())
                .build();
    }


}
