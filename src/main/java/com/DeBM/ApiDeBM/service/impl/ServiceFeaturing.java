package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.domain.Artisti;
import com.DeBM.ApiDeBM.domain.Featuring;
import com.DeBM.ApiDeBM.dto.FeaturingDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.repository.IRepositoryArtisti;
import com.DeBM.ApiDeBM.repository.IRepositoryFeaturing;
import com.DeBM.ApiDeBM.service.Interface.InterfaceFeaturing;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ServiceFeaturing implements InterfaceFeaturing {

    @Autowired
    private IRepositoryFeaturing iRepositoryFeaturing;
    @Autowired
    private IRepositoryArtisti iRepositoryArtisti;

    @Override
    public Featuring inserisciFeaturing(FeaturingDTO dto) {
        Featuring featuring = new Featuring();
        featuring.setIdFeaturing(0);//evito un possibile tentativo di update

        if(dto.getScadenza() == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Devi inserire la data di scadenza");
        else
            featuring.setScadenza(dto.getScadenza());
        if(dto.getInizio() == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Devi inserire la data di inizio");
        else
            featuring.setInizio(dto.getInizio());

        Artisti artista = iRepositoryArtisti.getById(dto.getArtisti().getIdArtista());
        featuring.setArtista(artista);

        return iRepositoryFeaturing.save(featuring);
    }

    @Override
    public Featuring modificaFeaturing(FeaturingDTO dto) {
        Featuring featuring = iRepositoryFeaturing.findById(dto.getIdFeaturing()).orElse(null);

        if(featuring == null){
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Featuring non trovato");
        }

        featuring.setInizio(dto.getInizio());
        featuring.setScadenza(dto.getScadenza());

        return iRepositoryFeaturing.save(featuring);
    }

    @Override
    public boolean eliminaFeaturing(int id) {
        if(id == 0)
            return false;

        iRepositoryFeaturing.deleteById(id);
        return true;
    }

    @Override
    public Set<Featuring> getallFeaturing() {
        Set<Featuring> listaFeaturing = new HashSet<>(iRepositoryFeaturing.findAll());

        if(listaFeaturing.isEmpty())
            return null;

        return listaFeaturing;
    }

    @Override
    public Set<Featuring> getActiveFeaturings(Date data) {
        Set<Featuring> listaFeaturing = new HashSet<>(
                iRepositoryFeaturing.findFeaturingByInizioLessThanEqualAndScadenzaGreaterThanEqual(data, data));

        if(listaFeaturing.isEmpty())
            return null;

        return listaFeaturing;
    }

}
