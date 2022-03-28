package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.domain.Brani;
import com.DeBM.ApiDeBM.domain.Generi;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import com.DeBM.ApiDeBM.dto.GeneriDTO;
import com.DeBM.ApiDeBM.repository.IRepositoryBrani;
import com.DeBM.ApiDeBM.repository.IRepositoryGeneri;
import com.DeBM.ApiDeBM.service.Interface.InterfaceGeneri;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ServiceGeneri implements InterfaceGeneri {

    @Autowired
    private IRepositoryGeneri iRepositoryGeneri;

    @Override
    public Generi inserisciGenere(GeneriDTO dto) {

        Generi genere = new ModelMapper().map(dto,Generi.class);
        genere.setIdGenere(0);//evito un possibile tentativo di update

        return iRepositoryGeneri.save(genere);
    }

    @Override
    public Generi modificaGenere(GeneriDTO dto, String tipologia) {
        if (tipologia == null)
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
    public boolean eliminaGenere(String tipologia) {
        if (tipologia == null)
            return false;
        iRepositoryGeneri.delete(iRepositoryGeneri.findByTipologiaIgnoreCase(tipologia));
        return true;
    }

    @Override
    public Set<Generi> getAllGeneri() {
        Set<Generi> listaGeneri = new HashSet<>(iRepositoryGeneri.findAll());
        if (listaGeneri.isEmpty()) //se è vuota
            return null;
        return listaGeneri;
    }

    @Override
    public Generi getGenere(String tipologia) {
        if (tipologia == null)
            return null;
        return iRepositoryGeneri.findByTipologiaIgnoreCase(tipologia);
    }

    @Override
    public Set<Generi> getGeneriByTitoloBrano(String titolo) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean eliminaGenereById(int id) {
        if (id == 0)
            return true;
        iRepositoryGeneri.deleteById(id);
        return false;
    }
}
