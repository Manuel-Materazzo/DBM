package com.DeBM.ApiDeBM.service.impl;

import com.DeBM.ApiDeBM.domain.Artisti;
import com.DeBM.ApiDeBM.domain.Featuring;
import com.DeBM.ApiDeBM.dto.FeaturingDTO;
import com.DeBM.ApiDeBM.repository.IRepositoryFeaturing;
import com.DeBM.ApiDeBM.service.Interface.InterfaceFeaturing;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ServiceFeaturing implements InterfaceFeaturing {

    @Autowired
    private IRepositoryFeaturing iRepositoryFeaturing;
    @Autowired
    private ServiceFeaturing serviceFeaturing;

    @Override
    public Featuring inserisciFeaturing(FeaturingDTO dto) {
        Featuring featuring = new Featuring();
        featuring.setIdFeaturing(dto.getIdFeaturing());
        if(dto.getScadenza() == null)
            return null;
        else
            featuring.setScadenza(dto.getScadenza());
        if(dto.getInizio() == null)
            return null;
        else
            featuring.setInizio(dto.getInizio());
        

        var artisti = new ModelMapper().map(dto.getArtisti(), Artisti.class);
        featuring.setArtisti(artisti);



        return iRepositoryFeaturing.save(featuring); //le informazioni sono uguali a quelle salvate
    }

    @Override
    public Featuring modificaFeaturing(FeaturingDTO dto, int id) {
        if(id == 0)
            return null;
        Optional<Featuring> featuringOptional = iRepositoryFeaturing.findById(id); //cerco con quello vecchio
        if(!featuringOptional.isPresent()){
            return null;
        }
        Featuring featuring=featuringOptional.get();
        //utente.setNumeriTelefonoList(dto.getNumeroTelefono());
        featuring.setIdFeaturing(dto.getIdFeaturing());  //setta lo username nuovo
        featuring.setScadenza(dto.getScadenza());
        iRepositoryFeaturing.save(featuring);
        return featuring;
    }

    @Override
    public String eliminaFeaturing(int id) {
        if(id == 0)
            return null;
        iRepositoryFeaturing.deleteById(id);
        return "deleted";
    }

    @Override
    public Set<Featuring> getallFeaturing() {
        Set<Featuring> listaFeaturing = new HashSet<>(iRepositoryFeaturing.findAll());
        if(listaFeaturing.isEmpty()) //se è vuota
            return null;
        return listaFeaturing;
    }

    @Override
    public Set<Featuring> getFeaturedByNomeArtista(String nomeArte) {
        return iRepositoryFeaturing.findFeaturingByArtisti_NomeArteIgnoreCase(nomeArte);
    }

    @Override
    public Set<Featuring> getActiveFeaturingsByInzioAndScadenza(Date inizio, Date scadenza) {
        return iRepositoryFeaturing.findByInizioAfterAndScadenzaBefore(inizio, scadenza);
    }

    @Override
    public Set<Featuring> getActiveFeaturings(Date dataOdierna1, Date dataOdierna2) {
        Set<Featuring> listaFeaturing = new HashSet<>(iRepositoryFeaturing.findFeaturingByInizioLessThanEqualAndScadenzaGreaterThanEqual(dataOdierna1 , dataOdierna2));
        if(listaFeaturing.isEmpty()) //se è vuota
            return null;
        return listaFeaturing;
    }

}
