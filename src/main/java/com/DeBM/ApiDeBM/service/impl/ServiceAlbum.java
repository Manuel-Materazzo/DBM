package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.domain.*;
import com.DeBM.ApiDeBM.dto.AlbumDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.repository.IRepositoryAlbum;
import com.DeBM.ApiDeBM.repository.IRepositoryArtisti;
import com.DeBM.ApiDeBM.repository.IRepositoryGeneri;
import com.DeBM.ApiDeBM.service.Interface.InterfaceAlbum;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class ServiceAlbum implements InterfaceAlbum {

    Logger logger = LoggerFactory.getLogger(ServiceAlbum.class);

    @Autowired
    private IRepositoryAlbum iRepositoryAlbum;
    @Autowired
    private IRepositoryArtisti iRepositoryArtisti;
    @Autowired
    private IRepositoryGeneri iRepositoryGeneri;

    @Override
    public Album inserisciAlbum(AlbumDTO dto) {

        Album album = new ModelMapper().map(dto, Album.class);
        album.setIdAlbum(0);//evito un possibile tentativo di update
        album.setApprovato(false);

        if(album.getBraniList() != null)
            for(Brani brano: album.getBraniList()){
                for(BraniGeneri braniGeneri: brano.getBraniGeneri()){
                    Generi genere = iRepositoryGeneri.getById(braniGeneri.getGenere().getIdGenere());
                    braniGeneri.setId(0);//evito un possibile tentativo di update
                    brano.setApprovato(false);
                    braniGeneri.setBrano(brano);
                    braniGeneri.setGenere(genere);
                }
                brano.setAlbum(album);
            }

        if(album.getAlbumArtisti() == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Non puoi creare un'album senza artista");

        for(AlbumArtisti albumArtisti: album.getAlbumArtisti()){
            Artisti artista = iRepositoryArtisti.getById(albumArtisti.getArtista().getIdArtista());
            albumArtisti.setId(0);//evito un possibile tentativo di update
            albumArtisti.setArtista(artista);
            albumArtisti.setAlbum(album);
        }


        return iRepositoryAlbum.save(album);

    }

    @Override
    public Album updateAlbumById(AlbumDTO dto) {

        var optional = iRepositoryAlbum.findById(dto.getIdAlbum());//cerco con quello vecchio

        if (!optional.isPresent()) return null;

        Album album = optional.get();

        //update parziali
        if (dto.getNome() != null && dto.getNome().length() > 0)
            album.setNome(dto.getNome());
        if (dto.getDataPubblicazione() != null)
            album.setDataPubblicazione(dto.getDataPubblicazione());
        if (dto.getDescrizione() != null && dto.getDescrizione().length() > 0)
            album.setDescrizione(dto.getDescrizione());
        if (dto.getCasaDiscografica() != null && dto.getCasaDiscografica().length() > 0)
            album.setCasaDiscografica(dto.getCasaDiscografica());
        if (dto.getUrlCopertina() != null && dto.getUrlCopertina().length() > 0)
            album.setUrlCopertina(dto.getUrlCopertina());
        if (Boolean.TRUE.equals(dto.getApprovato()))
            album.setApprovato(dto.getApprovato());
        if (dto.getDiscoOro() > 0)
            album.setDiscoOro(dto.getDiscoOro());
        if (dto.getDiscoPlatino() > 0)
            album.setDiscoPlatino(dto.getDiscoPlatino());

        iRepositoryAlbum.save(album);
        return album;
    }

    public Album approvaAlbum(Album dao) {
        dao.setApprovato(true);
        iRepositoryAlbum.save(dao);
        return dao;
    }

    @Override
    public Set<Album> getAllAlbum() {
        Set<Album> listaAlbum = new HashSet<>(iRepositoryAlbum.findAll());
        if (listaAlbum.isEmpty()) //se è vuota
            return null;
        return listaAlbum;
    }

    @Override
    public Set<Album> getAlbumByIdArtista(int id) {

        if(!iRepositoryArtisti.findById(id).isPresent())
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Artista non trovato");

        return iRepositoryAlbum.findAlbumByAlbumArtisti_Artista_IdArtista(id);
    }

    @Override
    public Album getAlbumWithBraniById(int id) {
        return iRepositoryAlbum.findAlbumAndBraniByIdAlbum(id);
    }

    @Override
    public Set<Album> getAlbumNotApprovedWithBrani(boolean approvato) {
        return iRepositoryAlbum.findAlbumByApprovato(approvato);
    }

    @Override
    public Set<Album> getAlbumNotApprovedWithBraniById(boolean approvato, int id) {
        return iRepositoryAlbum.findAlbumByApprovatoAndAlbumArtisti_Artista_IdArtista(approvato, id);
    }

    @Override
    public Set<Album> getAlbumBySearchValue(String searchValue, Pageable page) {
        return iRepositoryAlbum.findAlbumByNomeContainingIgnoreCase(searchValue, page).toSet();
    }

    @Override
    public Set<Album> getAlbumNotApprovedBySearchValue(String searchValue, Pageable page) {
        return iRepositoryAlbum.findAlbumByNomeContainingIgnoreCaseAndApprovato(searchValue, false, page).toSet();
    }

    @Override
    public Set<Album> getAlbumMoreListened(Pageable page) {
        return iRepositoryAlbum.findByOrderByDiscoPlatinoDesc(page).toSet();
    }

    @Override
    public Set<Album> getTopAlbumByIdArtista(int id) {
        return iRepositoryAlbum.findTop5ByAlbumArtisti_Artista_IdArtistaOrderByDiscoOroDescDiscoPlatinoDesc(id);
    }

    @Override
    @Transactional
    public boolean eliminaAlbumById(Integer id) {
        iRepositoryAlbum.deleteById(id);
        return true;
    }
}