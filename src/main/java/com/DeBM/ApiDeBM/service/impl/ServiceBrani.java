package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.domain.Album;
import com.DeBM.ApiDeBM.domain.Brani;
import com.DeBM.ApiDeBM.domain.Generi;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import com.DeBM.ApiDeBM.dto.GeneriDTO;
import com.DeBM.ApiDeBM.repository.IRepositoryAlbum;
import com.DeBM.ApiDeBM.repository.IRepositoryBrani;
import com.DeBM.ApiDeBM.repository.IRepositoryGeneri;
import com.DeBM.ApiDeBM.service.Interface.InterfaceBrani;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    private ServiceBrani serviceBrani;
    @Autowired
    private IRepositoryAlbum iRepositoryAlbum;
    @Autowired
    private IRepositoryGeneri iRepositoryGeneri;

    @Override
    public Brani inserisciBrano(BraniDTO dto) {

        var res = new ModelMapper().map(dto, Brani.class);
        res.setApprovato(false); //defaulto l'approvazione
        var album = iRepositoryAlbum.findById(dto.getAlbumID());
        for (GeneriDTO generiDTO : dto.getListGeneri()) {
            Optional<Generi> generi = iRepositoryGeneri.findById(generiDTO.getIdGenere());
            generi.get().getListBrani().add(res);

        }

        if (!album.isPresent())
            return null;

        res.setAlbum(album.get());


        return iRepositoryBrani.save(res); //le informazioni sono uguali a quelle salvate
    }

    @Override
    public Brani modificaBrano(BraniDTO dto) {

        var optional = iRepositoryBrani.findById(dto.getIdBrano());

        if (!optional.isPresent()) return null;

        Brani brano = optional.get(); //cerco con quello vecchio

        for (Generi genere : brano.getListGeneri()) {
            Generi generi = iRepositoryGeneri.findById(genere.getIdGenere()).get();
            generi.getListBrani().remove(brano);
            iRepositoryGeneri.save(generi);
        }

        var res = new ModelMapper().map(dto, Brani.class);

        if (res.getTitolo() != null)
            brano.setTitolo(res.getTitolo());
        if (res.getDurata() != 0)
            brano.setDurata(res.getDurata());
        if (res.getApprovato() != null)
            brano.setApprovato(res.getApprovato());
        if (res.getListGeneri() != null)
            brano.setListGeneri(res.getListGeneri());
        if (res.getUrl() != null)
            brano.setUrl(res.getUrl());

        for (Generi genere : res.getListGeneri()) {
            Generi generi = iRepositoryGeneri.findById(genere.getIdGenere()).get();
            generi.getListBrani().add(brano);
            iRepositoryGeneri.save(generi);
        }

        return brano;
    }

    public Brani approvaBrano(BraniDTO dto) {

        var optional = iRepositoryBrani.findById(dto.getIdBrano());

        if (!optional.isPresent()) return null;

        Brani brano = optional.get(); //cerco con quello vecchio
        brano.setApprovato(dto.getApprovato());
        iRepositoryBrani.save(brano);
        return brano;
    }

    //TODO:gli elimina by titolo o nome non funzionano
    @Override
    public String eliminaBrano(String titolo) {
        if (titolo == null)
            return null;
        Brani brano = iRepositoryBrani.findByTitoloIgnoreCase(titolo);
        Album album = iRepositoryAlbum.findAlbumByBraniList_IdBrano(brano.getIdBrano());
        for (Brani brani : album.getBraniList()) {
            if (Objects.equals(brani.getTitolo(), titolo)) {
                for (Generi generi : brani.getListGeneri()) {
                    generi.getListBrani().remove(brani);
                }
                album.getBraniList().remove(brani);
                break;
            }
        }
        iRepositoryBrani.delete(brano);
        return "deleted";
    }

    @Override
    public String eliminaBranoById(int id) {
        if (id == 0)
            return null;
        Album album = iRepositoryAlbum.findAlbumByBraniList_IdBrano(id);
        for (Brani brani : album.getBraniList()) {
            if (brani.getIdBrano() == id) {
                for (Generi generi : brani.getListGeneri()) {
                    generi.getListBrani().remove(brani);
                }
                album.getBraniList().remove(brani);
                break;
            }
        }
        iRepositoryBrani.deleteById(id);
        return "deleted";
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
