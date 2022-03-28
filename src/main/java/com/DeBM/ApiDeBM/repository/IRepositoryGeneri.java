package com.DeBM.ApiDeBM.repository;

import com.DeBM.ApiDeBM.domain.Generi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface IRepositoryGeneri extends JpaRepository<Generi, Integer> {

    Generi findByTipologiaIgnoreCase(String tipologia);

    //Integer countByIdGenere();
}
