package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.domain.Album;
import com.DeBM.ApiDeBM.domain.Brani;
import com.DeBM.ApiDeBM.domain.BraniGeneri;
import com.DeBM.ApiDeBM.domain.Generi;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import com.DeBM.ApiDeBM.dto.GeneriDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.repository.IRepositoryAlbum;
import com.DeBM.ApiDeBM.repository.IRepositoryArtisti;
import com.DeBM.ApiDeBM.repository.IRepositoryBrani;
import com.DeBM.ApiDeBM.repository.IRepositoryGeneri;
import com.DeBM.ApiDeBM.service.Interface.InterfaceBrani;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class ServiceBrani implements InterfaceBrani {

    @Autowired
    private IRepositoryBrani iRepositoryBrani;
    @Autowired
    private IRepositoryAlbum iRepositoryAlbum;
    @Autowired
    private IRepositoryGeneri iRepositoryGeneri;

    @Override
    public Brani inserisciBrano(BraniDTO dto) {

        Brani brano = new ModelMapper().map(dto,Brani.class);
        brano.setIdBrano(0);//evito un possibile tentativo di update
        brano.setApprovato(false);

        Album album = iRepositoryAlbum.getById(brano.getAlbum().getIdAlbum());
        brano.setAlbum(album);

        for(BraniGeneri braniGeneri: brano.getBraniGeneri()){
            Generi genere = iRepositoryGeneri.getById(braniGeneri.getGenere().getIdGenere());
            braniGeneri.setId(0);//evito un possibile tentativo di update
            braniGeneri.setBrano(brano);
            braniGeneri.setGenere(genere);
        }

        return iRepositoryBrani.save(brano);
    }

    @Override
    public Brani modificaBrano(BraniDTO dto) {

        Brani branoOld = iRepositoryBrani.findById(dto.getIdBrano()).orElse(null);

        if(branoOld == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Brano non trovato");

        Brani branoNew = new ModelMapper().map(dto,Brani.class);

        //impedisco il cambio di alcune variabili
        branoNew.setAlbum(branoOld.getAlbum());
        branoNew.setBraniGeneri(branoOld.getBraniGeneri());

        return iRepositoryBrani.save(branoNew);
    }

    public Brani approvaBrano(BraniDTO dto) {

        Brani brano = iRepositoryBrani.findById(dto.getIdBrano()).orElse(null);

        if(brano == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Brano non trovato");

        brano.setApprovato(dto.getApprovato());//modifico solo lo stato di approvazione

        return iRepositoryBrani.save(brano);
    }

    @Override
    public boolean eliminaBrano(String titolo) {
        iRepositoryBrani.deleteByTitoloIgnoreCase(titolo);
        return true;
    }

    @Override
    public boolean eliminaBranoById(int id) {
        iRepositoryBrani.deleteById(id);
        return true;
    }

    @Override
    public Set<Brani> getAllBrani() {
        Set<Brani> listaBrani = new HashSet<>(iRepositoryBrani.findAll());
        if (listaBrani.isEmpty()) //se è vuota
            return null;
        return listaBrani;
    }

    @Override
    public Brani getBrano(String titolo) {
        if (titolo == null)
            return null;
        return iRepositoryBrani.findByTitoloIgnoreCase(titolo);
    }

    public Brani getBranoById(Integer id) {
        var optional = iRepositoryBrani.findById(id);
        return optional.orElse(null);
    }

    @Override
    public Set<Brani> getBraniByIdAlbum(int id) {

        if(!iRepositoryAlbum.findById(id).isPresent())
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Album non trovato");

        return iRepositoryBrani.findBraniByAlbum_IdAlbum(id);
    }

    @Override
    public Set<Brani> getBraniByApprovato(boolean approvato) {
        return iRepositoryBrani.findBraniByApprovato(approvato);
    }

    @Override
    public Set<Brani> getBraniNotApprovedInAlbumApproved(boolean approvatoBrani, boolean approvatoAlbum) {
        return iRepositoryBrani.findBraniByApprovatoAndAlbum_Approvato(approvatoBrani, approvatoAlbum);
    }

    @Override
    public Set<Brani> getBraniBySearchValue(String searchValue, Pageable page) {
        return iRepositoryBrani.findBraniByTitoloContainingIgnoreCase(searchValue, page).toSet();
    }
}
