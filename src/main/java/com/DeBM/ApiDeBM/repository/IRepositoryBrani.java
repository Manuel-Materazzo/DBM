package com.DeBM.ApiDeBM.repository;

import com.DeBM.ApiDeBM.domain.Brani;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Set;

public interface IRepositoryBrani extends JpaRepository<Brani, Integer> {

    Brani findByTitoloIgnoreCase(String titolo);
    @EntityGraph(attributePaths = {"braniGeneri.genere"})
    Set<Brani> findBraniByAlbum_IdAlbum(int id);
    Set<Brani> findBraniByApprovatoAndAlbum_Approvato(boolean approvatoBrani, boolean approvatoAlbum);
    Page<Brani> findBraniByTitoloContainingIgnoreCase(String searchValue, Pageable page);
    Set<Brani> findBraniByApprovato(boolean approvato);
    void deleteByTitoloIgnoreCase(String titolo);

}
