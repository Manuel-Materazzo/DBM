package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.domain.Statistiche;
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
    @Autowired
    private IRepositoryStatistiche iRepositoryStatistiche;

    //---------------------------------------------------------------------------
    //                              GENERI
    //---------------------------------------------------------------------------
    @Override
    public Statistiche getAumentoGeneri() {
        int aumentoGeneri = (int) iRepositoryGeneri.count();
        aumentoGeneri += 1;
        if (aumentoGeneri == 0)
            return null;
        Statistiche statistiche = iRepositoryStatistiche.findAll().get(0);
        statistiche.setAumentoGeneri(aumentoGeneri);
        iRepositoryStatistiche.save(statistiche);
        return statistiche;
    }

    @Override
    public Statistiche getDiminuzioneGeneri() { //Verrà visualizzato solamente il numero di eliminati
        Statistiche statistiche = iRepositoryStatistiche.findAll().get(0);
        int diminuzioneGeneri = statistiche.getDiminuzioneGeneri();
        diminuzioneGeneri += 1;
        if (diminuzioneGeneri == 0)
            return null;
        statistiche.setDiminuzioneGeneri(diminuzioneGeneri);
        iRepositoryStatistiche.save(statistiche);
        return statistiche;
    }

    @Override
    public Statistiche getTotaleGeneri() {
        int totaleGeneri = (int) iRepositoryGeneri.count();
        if(totaleGeneri == 0)
            return null;
        Statistiche statistiche = iRepositoryStatistiche.findAll().get(0);
        statistiche.setTotaleGeneri(totaleGeneri);
        iRepositoryStatistiche.save(statistiche);
        return statistiche;
    }

    //---------------------------------------------------------------------------
    //                               BRANI
    //---------------------------------------------------------------------------
    @Override
    public Statistiche getAumentoBrani() {
        int aumentoBrani = (int) iRepositoryGeneri.count();
        aumentoBrani += 1;
        if (aumentoBrani == 0)
            return null;
        Statistiche statistiche = iRepositoryStatistiche.findAll().get(0);
        statistiche.setAumentoBrani(aumentoBrani);
        iRepositoryStatistiche.save(statistiche);
        return statistiche;
    }

    @Override
    public Statistiche getDiminuzioneBrani() {
        return null;
    }

    @Override
    public Statistiche getTotaleBrani() {
        int totaleBrani = (int) iRepositoryGeneri.count();
        if(totaleBrani == 0)
            return null;
        Statistiche statistiche = iRepositoryStatistiche.findAll().get(0);
        statistiche.setTotaleBrani(totaleBrani);
        iRepositoryStatistiche.save(statistiche);
        return statistiche;
    }

    //---------------------------------------------------------------------------
    //                              ARTISTI
    //---------------------------------------------------------------------------
    @Override
    public Statistiche getAumentoArtisti() {
        int aumentoArtisti = (int) iRepositoryGeneri.count();
        aumentoArtisti += 1;
        if (aumentoArtisti == 0)
            return null;
        Statistiche statistiche = iRepositoryStatistiche.findAll().get(0);
        statistiche.setAumentoArtisti(aumentoArtisti);
        iRepositoryStatistiche.save(statistiche);
        return statistiche;
    }

    @Override
    public Statistiche getDiminuzioneArtisti() {
        return null;
    }

    @Override
    public Statistiche getTotaleArtisti() {
        int totaleArtisti = (int) iRepositoryGeneri.count();
        if(totaleArtisti == 0)
            return null;
        Statistiche statistiche = iRepositoryStatistiche.findAll().get(0);
        statistiche.setTotaleArtisti(totaleArtisti);
        iRepositoryStatistiche.save(statistiche);
        return statistiche;
    }

    //---------------------------------------------------------------------------
    //                               ALBUM
    //---------------------------------------------------------------------------
    @Override
    public Statistiche getAumentoAlbum() {
        int aumentoAlbum = (int) iRepositoryGeneri.count();
        aumentoAlbum += 1;
        if (aumentoAlbum == 0)
            return null;
        Statistiche statistiche = iRepositoryStatistiche.findAll().get(0);
        statistiche.setAumentoAlbum(aumentoAlbum);
        iRepositoryStatistiche.save(statistiche);
        return statistiche;
    }

    @Override
    public Statistiche getDiminuzioneAlbum() {
        return null;
    }

    @Override
    public Statistiche getTotaleAlbum() {
        int totaleAlbum = (int) iRepositoryGeneri.count();
        if(totaleAlbum == 0)
            return null;
        Statistiche statistiche = iRepositoryStatistiche.findAll().get(0);
        statistiche.setTotaleALbum(totaleAlbum);
        iRepositoryStatistiche.save(statistiche);
        return statistiche;
    }


    public Statistiche getStatistiche() {
        return Statistiche.builder()
                .totaleArtisti((int) iRepositoryArtisti.count())
                .totaleBrani((int) iRepositoryBrani.count())
                .totaleALbum((int) iRepositoryAlbum.count())
                .totaleGeneri((int) iRepositoryGeneri.count())
                .build();
    }

    //---------------------------------------------------------------------------
    //                              RICERCHE
    //---------------------------------------------------------------------------
    @Override
    public Statistiche ricercaAlbum() {
        Statistiche statistiche = iRepositoryStatistiche.findAll().get(0);
        int ricercaAlbum = statistiche.getRicercaAlbum();
        ricercaAlbum += 1;
        if (ricercaAlbum == 0)
            return null;
        statistiche.setRicercaAlbum(ricercaAlbum);
        iRepositoryStatistiche.save(statistiche);
        return statistiche;

    }

    @Override
    public Statistiche ricercaArtisti() {
        Statistiche statistiche = iRepositoryStatistiche.findAll().get(0);
        int ricercaArtisti = statistiche.getRicercaArtisti();
        ricercaArtisti += 1;
        if (ricercaArtisti == 0)
            return null;
        statistiche.setRicercaArtisti(ricercaArtisti);
        iRepositoryStatistiche.save(statistiche);
        return statistiche;
    }

}
