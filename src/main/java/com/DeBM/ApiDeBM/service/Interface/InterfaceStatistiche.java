package com.DeBM.ApiDeBM.service.Interface;

import com.DeBM.ApiDeBM.domain.Statistiche;

public interface InterfaceStatistiche {

    Statistiche getAumentoGeneri();
    Statistiche getDiminuzioneGeneri();
    Statistiche getTotaleGeneri();

    Statistiche getAumentoBrani();
    Statistiche getDiminuzioneBrani();
    Statistiche getTotaleBrani();

    Statistiche getAumentoArtisti();
    Statistiche getDiminuzioneArtisti();
    Statistiche getTotaleArtisti();

    Statistiche getAumentoAlbum();
    Statistiche getDiminuzioneAlbum();
    Statistiche getTotaleAlbum();

    Statistiche ricercaAlbum();
    Statistiche ricercaArtisti();
}
