package com.DeBM.ApiDeBM.repository;

import com.DeBM.ApiDeBM.domain.Featuring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

public interface IRepositoryFeaturing extends JpaRepository<Featuring, Integer> {

    Set<Featuring> findFeaturingByArtisti_NomeArteIgnoreCase(String nomeArte);

    //Set<Featuring> findByInizioAfterAndScadenzaBefore(Date inizio, Date scadenza);

    Set<Featuring> findByInizioAfterAndScadenzaBefore(Date scadenza, Date inizio);

    Set<Featuring> findFeaturingByInizioLessThanEqualAndScadenzaGreaterThanEqual(Date dataOdierna1, Date dataOdierna2);



}
