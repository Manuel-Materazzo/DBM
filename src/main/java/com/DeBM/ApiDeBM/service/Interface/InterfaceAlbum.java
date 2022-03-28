package com.DeBM.ApiDeBM.service.Interface;

import com.DeBM.ApiDeBM.domain.Album;
import com.DeBM.ApiDeBM.dto.AlbumDTO;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface InterfaceAlbum {

    Album inserisciAlbum(AlbumDTO dto);
    Album updateAlbumById(AlbumDTO dto);
    Set<Album> getAllAlbum();
    Set<Album> getAlbumByIdArtista(int id);
    Album getAlbumWithBraniById(int id);
    Set<Album> getAlbumNotApprovedWithBrani(boolean approvato);
    Set<Album> getAlbumNotApprovedWithBraniById(boolean approvato, int id);
    Set<Album> getAlbumBySearchValue(String searchValue, Pageable page);
    Set<Album> getAlbumNotApprovedBySearchValue(String searchValue, Pageable page);
    Set<Album> getAlbumMoreListened(Pageable page);
    Set<Album> getTopAlbumByIdArtista(int id);
    boolean eliminaAlbumById(Integer id);
}