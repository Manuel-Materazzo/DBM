package com.DeBM.ApiDeBM.service.Interface;

import com.DeBM.ApiDeBM.domain.Featuring;
import com.DeBM.ApiDeBM.domain.Generi;
import com.DeBM.ApiDeBM.dto.GeneriDTO;

import java.util.Set;

public interface InterfaceGeneri {

    Generi inserisciGenere(GeneriDTO dto);
    Generi modificaGenere(GeneriDTO dto, String tipologia);
    boolean eliminaGenere(String tipologia);
    Set<Generi> getAllGeneri();
    Generi getGenere(String tipologia);
    Set<Generi> getGeneriByTitoloBrano(String titolo);
    boolean eliminaGenereById(int id);
}
