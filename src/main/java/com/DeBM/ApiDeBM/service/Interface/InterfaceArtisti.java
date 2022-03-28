package com.DeBM.ApiDeBM.service.Interface;

import com.DeBM.ApiDeBM.domain.Album;
import com.DeBM.ApiDeBM.domain.Artisti;
import com.DeBM.ApiDeBM.dto.ArtistiDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface InterfaceArtisti {


    Artisti inserisciArtista(ArtistiDTO dto);
    Artisti modificaArtista(ArtistiDTO dto);
    Set<Artisti> getAllArtisti();
    Artisti getArtistaById(Integer id);
    boolean eliminaArtistaById(Integer id);
    Set<Artisti> getArtistiBySearchValue(String searchValue, Pageable page);

}
