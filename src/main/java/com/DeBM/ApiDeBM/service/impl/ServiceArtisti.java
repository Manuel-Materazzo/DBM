package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.domain.Album;
import com.DeBM.ApiDeBM.domain.Artisti;
import com.DeBM.ApiDeBM.dto.ArtistiDTO;
import com.DeBM.ApiDeBM.repository.IRepositoryAlbum;
import com.DeBM.ApiDeBM.repository.IRepositoryArtisti;
import com.DeBM.ApiDeBM.service.Interface.InterfaceArtisti;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class ServiceArtisti implements InterfaceArtisti {

    @Autowired
    private IRepositoryArtisti iRepositoryArtisti;

    @Override
    public Artisti inserisciArtista(ArtistiDTO dto) {

        //default values
        dto.setIdArtista(0);
        dto.setBannato(false);
        dto.setAdmin(false);

        var res = new ModelMapper().map(dto,Artisti.class);

        return iRepositoryArtisti.save(res);
    }

    @Override
    public Artisti modificaArtista(ArtistiDTO dto) {

        Artisti artistaOld = iRepositoryArtisti.getById(dto.getIdArtista());

        Artisti artistaNew = new ModelMapper().map(dto,Artisti.class);

        if(artistaNew.getUrlAvatar() == null || artistaNew.getUrlAvatar().length() < 3)
            artistaNew.setUrlAvatar(artistaOld.getUrlAvatar());

        artistaNew.setFeaturingList(artistaOld.getFeaturingList());
        artistaNew.setAlbumArtisti(artistaOld.getAlbumArtisti());

        iRepositoryArtisti.save(artistaNew);

        return artistaNew;
    }


    @Override
    @Transactional
    public boolean eliminaArtistaById(Integer id) {
        iRepositoryArtisti.deleteById(id);
        return true;
    }

    @Override
    public Set<Artisti> getArtistiBySearchValue(String searchValue, Pageable page) {
        return iRepositoryArtisti.findArtistiByNomeArteContainingIgnoreCase(searchValue, page).toSet();
    }

    @Override
    public Set<Artisti> getAllArtisti() {
        Set<Artisti> listaArtisti = new HashSet<>(iRepositoryArtisti.findAll());
        if(listaArtisti.isEmpty()) //se è vuota
            return null;
        return listaArtisti;

    }

    @Override
    public Artisti getArtistaById(Integer id) {
        if(id == null)
            return null;

        var artista = iRepositoryArtisti.findById(id);

        return artista.orElse(null);
    }
}
