package com.DeBM.ApiDeBM.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class StatisticheDTO {

    private int idStatistica;

    //ALBUM
    private int aumentoAlbum;
    private int diminuzioneAlbum;
    private int totaleALbum;

    //ARTISTI
    private int aumentoArtisti;
    private int diminuzioneArtisti;
    private int totaleArtisti;

    //GENERI
    private int aumentoGeneri;
    private int diminuzioneGeneri;
    private int totaleGeneri;

    //BRANI
    private int aumentoBrani;
    private int diminuzioneBrani;
    private int totaleBrani;

    //RICERCHE
    private int ricercaAlbum;
    private int ricercaArtisti;
}
