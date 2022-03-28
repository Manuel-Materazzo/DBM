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
    @Autowired
    private ServiceArtisti serviceArtisti;
    @Autowired
    private IRepositoryAlbum iRepositoryAlbum;
    @Autowired
    private  ServiceAlbum serviceAlbum;

    @Override
    public Artisti inserisciArtista(ArtistiDTO dto) {
        /*
        Artisti artista = new Artisti();
        artista.setIdArtista(dto.getIdArtista());
        if(dto.getNomeArte() == null)
            return null;
        else
            artista.setNomeArte(dto.getNomeArte());

        if(dto.getBannato() == null)
            return null;
        else
            artista.setBannato(dto.getBannato());

        if(dto.getUrlAvatar() == null)
            return null;
        else
            artista.setUrlAvatar(dto.getUrlAvatar());

        if(dto.getBio() == null)
            return null;
        else
            artista.setBio(dto.getBio());

        if(dto.getEmail() == null)
            return null;
        else
            artista.setEmail(dto.getEmail());

         */

        var res = new ModelMapper().map(dto,Artisti.class);


        return iRepositoryArtisti.save(res);
    }

    @Override
    public Artisti modificaArtista(ArtistiDTO dto) {

        var oldDao = iRepositoryArtisti.findById(dto.getIdArtista()).orElse(null);

        if (oldDao == null) return null;

        var newDao = new ModelMapper().map(dto,Artisti.class);

        if(newDao.getUrlAvatar() == null)
            newDao.setUrlAvatar(oldDao.getUrlAvatar());
        newDao.setListAlbum(oldDao.getListAlbum());

        return iRepositoryArtisti.save(newDao);
    }

    @Override
    public String eliminaArtista(String nomeArte) {
        if(nomeArte == null)
            return null;
        iRepositoryArtisti.delete(iRepositoryArtisti.findByNomeArteIgnoreCase(nomeArte));
        return "deleted";
    }

    @Override
    @Transactional
    public String eliminaArtistaById(Integer id) {
        if(id == null)
            return null;
        Artisti artisti = iRepositoryArtisti.findById(id).get();
        Set<Album> albums = new HashSet<>(artisti.getListAlbum());
        for(Album album : albums){
            serviceAlbum.eliminaAlbumById(album.getIdAlbum());
        }

        iRepositoryArtisti.deleteById(id);
        return "deleted";
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
    public Artisti getArtista(String nomeArte) {
        if(nomeArte == null)
            return null;
        return iRepositoryArtisti.findByNomeArteIgnoreCase(nomeArte);
    }

    @Override
    public Artisti getArtistaById(Integer id) {
        if(id == null)
            return null;

        var artista = iRepositoryArtisti.findById(id);

        return artista.orElse(null);
    }
}
