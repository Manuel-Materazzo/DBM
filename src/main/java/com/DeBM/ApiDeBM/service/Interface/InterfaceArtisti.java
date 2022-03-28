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
    String eliminaArtista(String nomeArte);
    Set<Artisti> getAllArtisti();
    Artisti getArtista(String nomeArte);
    Artisti getArtistaById(Integer id);
    String eliminaArtistaById(Integer id);
    Set<Artisti> getArtistiBySearchValue(String searchValue, Pageable page);

}
