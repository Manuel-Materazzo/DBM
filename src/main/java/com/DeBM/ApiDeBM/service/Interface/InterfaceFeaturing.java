package com.DeBM.ApiDeBM.service.Interface;

import com.DeBM.ApiDeBM.domain.Featuring;
import com.DeBM.ApiDeBM.dto.FeaturingDTO;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

public interface InterfaceFeaturing {

    Featuring inserisciFeaturing(FeaturingDTO dto);
    Featuring modificaFeaturing(FeaturingDTO dto, int id);
    String eliminaFeaturing(int id);
    Set<Featuring> getallFeaturing();
    Set<Featuring> getFeaturedByNomeArtista(String nomeArte);
    Set<Featuring> getActiveFeaturingsByInzioAndScadenza(Date inizio, Date scadenza);
    Set<Featuring> getActiveFeaturings(Date dataOdierna1,Date dataOdierna2);
}
