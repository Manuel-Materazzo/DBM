package com.DeBM.ApiDeBM.repository;

import com.DeBM.ApiDeBM.domain.Artisti;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface IRepositoryArtisti extends JpaRepository<Artisti, Integer> {

    Artisti findByNomeArteIgnoreCase(String nomeArte);
    Page<Artisti> findArtistiByNomeArteContainingIgnoreCase(String searchValue, Pageable page);

}
