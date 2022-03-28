package com.DeBM.ApiDeBM.service.Interface;

import com.DeBM.ApiDeBM.domain.Album;
import com.DeBM.ApiDeBM.dto.AlbumDTO;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface InterfaceAlbum {

    Album inserisciAlbum(AlbumDTO dto);
    Album inserisciAlbumWithSongs(AlbumDTO dto);
    Album modificaAlbum(AlbumDTO dto, String nome);
    Album updateAlbumById(AlbumDTO dto);
    String eliminaAlbum(String nome);
    Set<Album> getAllAlbum();
    Album getAlbum(String nome);
    Set<Album> getAlbumByIdAutore(int id);
    Album getAlbumWithBraniById(int id);
    Set<Album> getAlbumNotApprovedWithBrani(boolean approvato);
    Set<Album> getAlbumNotApprovedWithBraniById(boolean approvato, int id);
    Set<Album> getAlbumBySearchValue(String searchValue, Pageable page);
    Set<Album> getAlbumNotApprovedBySearchValue(String searchValue, Pageable page);
    Set<Album> getAlbumMoreListened(Pageable page);
    Set<Album> getTopAlbumByIdArtista(int id);
    String eliminaAlbumById(Integer id);
}