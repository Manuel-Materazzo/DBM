package com.DeBM.ApiDeBM.repository;

import com.DeBM.ApiDeBM.domain.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface IRepositoryAlbum extends JpaRepository<Album, Integer> {

    Album findByNomeIgnoreCase(String nome);
    Album findAlbumAndBraniByIdAlbum(int id);
    Set<Album> findAlbumByApprovato(boolean approvato);
    Set<Album> findAlbumByApprovatoAndAlbumArtisti_Artista_IdArtista(boolean approvato, int id);
    Set<Album> findAlbumByAlbumArtisti_Artista_IdArtista(int id);
    Set<Album> findTop5ByAlbumArtisti_Artista_IdArtistaOrderByDiscoOroDescDiscoPlatinoDesc(int id);
    Page<Album> findAlbumByNomeContainingIgnoreCase(String searchValue, Pageable page);
    Page<Album> findAlbumByNomeContainingIgnoreCaseAndApprovato(String searchValue, boolean approvato, Pageable page);
    Page<Album> findByOrderByDiscoPlatinoDesc(Pageable page);

}