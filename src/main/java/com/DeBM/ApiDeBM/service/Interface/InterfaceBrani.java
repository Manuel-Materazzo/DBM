package com.DeBM.ApiDeBM.service.Interface;

import com.DeBM.ApiDeBM.domain.Brani;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface InterfaceBrani {

    Brani inserisciBrano(BraniDTO dto);
    Brani modificaBrano(BraniDTO dto);
    boolean eliminaBrano(String titolo);
    Set<Brani> getAllBrani();
    Brani getBrano(String titolo);
    Set<Brani> getBraniByIdAlbum(int id);
    Set<Brani> getBraniByApprovato(boolean approvato);
    Set<Brani> getBraniNotApprovedInAlbumApproved(boolean approvatoBrani, boolean approvatoAlbum);
    Set<Brani> getBraniBySearchValue(String searchValue, Pageable page);
    boolean eliminaBranoById(int id);
}
