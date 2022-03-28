package com.DeBM.ApiDeBM.repository;

import com.DeBM.ApiDeBM.domain.Featuring;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

public interface IRepositoryFeaturing extends JpaRepository<Featuring, Integer> {

    @EntityGraph(attributePaths = {"artista"})
    Set<Featuring> findFeaturingByInizioLessThanEqualAndScadenzaGreaterThanEqual(Date dataInizioIntervallo,Date dataFineIntervallo);

}
