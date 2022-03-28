package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.domain.Brani;
import com.DeBM.ApiDeBM.domain.Generi;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import com.DeBM.ApiDeBM.dto.GeneriDTO;
import com.DeBM.ApiDeBM.repository.IRepositoryBrani;
import com.DeBM.ApiDeBM.repository.IRepositoryGeneri;
import com.DeBM.ApiDeBM.service.Interface.InterfaceGeneri;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ServiceGeneri implements InterfaceGeneri{

    @Autowired
    private IRepositoryGeneri iRepositoryGeneri;
    @Autowired
    private ServiceGeneri serviceGeneri;
    @Autowired
    private IRepositoryBrani iRepositoryBrani;

    @Override
    public Generi inserisciGenere(GeneriDTO dto) {
        Generi generi = new Generi();
        generi.setIdGenere(dto.getIdGenere());
        if(dto.getDescrizione() == null)
            return null;
        else
            generi.setDescrizione(dto.getDescrizione());

        if(dto.getTipologia() == null)
            return null;
        else
            generi.setTipologia(dto.getTipologia());

        for(BraniDTO brano : dto.getListBrani()){
            Optional<Brani> brani = iRepositoryBrani.findById(brano.getIdBrano());
            if(brani.isPresent()){
                iRepositoryBrani.save(brani.get());
            }
        }

        return iRepositoryGeneri.save(generi); //le informazioni sono uguali a quelle salvate
    }

    @Override
    public Generi modificaGenere(GeneriDTO dto, String tipologia) {
        if(tipologia == null)
            return null;
        Generi generi = iRepositoryGeneri.findByTipologiaIgnoreCase(tipologia); //cerco con quello vecchio
        //utente.setNumeriTelefonoList(dto.getNumeroTelefono());
        generi.setIdGenere(dto.getIdGenere());  //setta lo username nuovo
        generi.setDescrizione(dto.getDescrizione());
        generi.setTipologia(dto.getTipologia());
        iRepositoryGeneri.save(generi);
        return generi;
    }

    @Override
    public String eliminaGenere(String tipologia) {
        if(tipologia == null)
            return null;
        iRepositoryGeneri.delete(iRepositoryGeneri.findByTipologiaIgnoreCase(tipologia));
        return "deleted";
    }

    @Override
    public Set<Generi> getAllGeneri() {
        Set<Generi> listaGeneri = new HashSet<>(iRepositoryGeneri.findAll());
        if(listaGeneri.isEmpty()) //se è vuota
            return null;
        return listaGeneri;
    }

    @Override
    public Generi getGenere(String tipologia) {
        if(tipologia == null)
            return null;
        return iRepositoryGeneri.findByTipologiaIgnoreCase(tipologia);
    }

    @Override
    public Set<Generi> getGeneriByTitoloBrano(String titolo) {
        return iRepositoryGeneri.findGeneriByListBrani_TitoloIgnoreCase(titolo);
    }
    @Override
    public String eliminaGenereById(int id) {
        if(id == 0)
            return null;
        iRepositoryGeneri.deleteById(id);
        return "deleted";
    }
}
