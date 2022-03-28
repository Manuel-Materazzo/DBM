package com.DeBM.ApiDeBM.repository;

import com.DeBM.ApiDeBM.domain.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface IRepositoryAlbum extends JpaRepository<Album, Integer> {

    Album findByNomeIgnoreCase(String nome);
    Set<Album> findAlbumByListArtisti_IdArtista(int id);
    Album findAlbumAndBraniByIdAlbum(int id);
    Set<Album> findAlbumByApprovato(boolean approvato);
    Set<Album> findAlbumByApprovatoAndListArtisti_IdArtista(boolean approvato, int id);
    Page<Album> findAlbumByNomeContainingIgnoreCase(String searchValue, Pageable page);
    Page<Album> findAlbumByNomeContainingIgnoreCaseAndApprovato(String searchValue, boolean approvato, Pageable page);
    Page<Album> findByOrderByDiscoPlatinoDesc(Pageable page);
    Set<Album> findTop5AlbumByListArtisti_IdArtista(int id);
    Album findAlbumByBraniList_IdBrano(int id);

}