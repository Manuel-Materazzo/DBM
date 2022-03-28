package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Album;
import com.DeBM.ApiDeBM.domain.Artisti;
import com.DeBM.ApiDeBM.domain.Brani;
import com.DeBM.ApiDeBM.dto.AlbumDTO;
import com.DeBM.ApiDeBM.dto.ArtistiDTO;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import com.DeBM.ApiDeBM.dto.SearchDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.service.impl.ServiceAlbum;
import com.DeBM.ApiDeBM.service.impl.ServiceArtisti;
import com.DeBM.ApiDeBM.service.impl.ServiceBrani;
import com.DeBM.ApiDeBM.service.impl.ServiceStatistiche;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(
        path = "/search",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ControllerSearch {

    @Autowired
    private ServiceAlbum serviceAlbum;
    @Autowired
    private ServiceBrani serviceBrani;
    @Autowired
    private ServiceArtisti serviceArtisti;
    @Autowired
    private ServiceStatistiche serviceStatistiche;

    @GetMapping({"/featuredSearch/{searchValue}/{page}", "/featuredSearch/{page}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<SearchDTO> featuredSearch(@PathVariable(value = "searchValue", required = false) String searchValue,
                                                    @PathVariable(value="page")Integer page,
                                                    HttpServletRequest request) {

        Pageable requestedPage = PageRequest.of(page, 12);

        SearchDTO searchDTO = new SearchDTO();
        Set<Album> listAlbum = new HashSet<>();
        Set<Artisti> listArtisti = new HashSet<>();
        Set<Brani> listBrani = new HashSet<>();

        if(searchValue == null)
        {
            listAlbum = serviceAlbum.getAlbumMoreListened(requestedPage);
            if (listAlbum == null)
                throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");
        }
        else
        {
            listAlbum = serviceAlbum.getAlbumBySearchValue(searchValue, requestedPage);
            listArtisti = serviceArtisti.getArtistiBySearchValue(searchValue, requestedPage);
            listBrani = serviceBrani.getBraniBySearchValue(searchValue, requestedPage);
            if (listBrani == null && listArtisti == null && listAlbum == null)
                throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");
        }

        //creo una lista di albumDto dalla lista di album
        Set<AlbumDTO> listAlbumDto = new HashSet<>();
        for (Album album : listAlbum) {
            AlbumDTO dto = new AlbumDTO();
            dto.setIdAlbum(album.getIdAlbum());
            dto.setApprovato(album.getApprovato());
            dto.setCasaDiscografica(album.getCasaDiscografica());
            dto.setDescrizione(album.getDescrizione());
            dto.setDiscoOro(album.getDiscoOro());
            dto.setDataPubblicazione(album.getDataPubblicazione());
            dto.setDiscoPlatino(album.getDiscoPlatino());
            dto.setNome(album.getNome());
            dto.setUrlCopertina(album.getUrlCopertina());
            listAlbumDto.add(dto);
        }

        //creo una lista di ArtistiDto dalla lista di artisti
        Set<ArtistiDTO> listArtistiDto = new HashSet<>();
        for (Artisti artisti : listArtisti) {
            ArtistiDTO dto = new ArtistiDTO();
            dto.setIdArtista(artisti.getIdArtista());
            dto.setBio(artisti.getBio());
            dto.setNomeArte(artisti.getNomeArte());
            dto.setEmail(artisti.getEmail());
            dto.setUrlAvatar(artisti.getUrlAvatar());
            dto.setBannato(artisti.getBannato());
            listArtistiDto.add(dto);
        }

        //creo una lista di BraniDto dalla lista di brani
        Set<BraniDTO> listBraniDTO = new HashSet<>();
        for (Brani brani : listBrani) {
            BraniDTO dto = new BraniDTO();
            dto.setIdBrano(brani.getIdBrano());
            dto.setTitolo(brani.getTitolo());
            dto.setDurata(brani.getDurata());
            dto.setUrl(brani.getUrl());
            dto.setApprovato(brani.getApprovato());
            listBraniDTO.add(dto);
        }

        searchDTO.setAlbumList(listAlbumDto);
        searchDTO.setArtistiList(listArtistiDto);
        searchDTO.setBraniList(listBraniDTO);

        return ResponseEntity.ok(searchDTO);
    }

    @GetMapping({"/artistSearch/{searchValue}/{page}", "/artistSearch/{page}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<SearchDTO> artistSearch(@PathVariable(value = "searchValue", required = false) String searchValue,
                                                  @PathVariable(value="page")Integer page,
                                                  HttpServletRequest request) {

        Pageable requestedPage = PageRequest.of(page, 12);

        if(searchValue == null) searchValue = "";

        SearchDTO searchDTO = new SearchDTO();
        Set<Artisti> listArtisti = serviceArtisti.getArtistiBySearchValue(searchValue, requestedPage);
        serviceStatistiche.ricercaArtisti(); //STATISTICHE

        Set<ArtistiDTO> listArtistiDto = new HashSet<>();
        for (Artisti artisti : listArtisti) {
            ArtistiDTO dto = new ArtistiDTO();
            dto.setIdArtista(artisti.getIdArtista());
            dto.setBio(artisti.getBio());
            dto.setNomeArte(artisti.getNomeArte());
            dto.setEmail(artisti.getEmail());
            dto.setUrlAvatar(artisti.getUrlAvatar());
            dto.setBannato(artisti.getBannato());
            listArtistiDto.add(dto);
        }

        searchDTO.setArtistiList(listArtistiDto);


        return ResponseEntity.ok(searchDTO);

    }

    @GetMapping({"/albumSearch/{searchValue}/{page}", "/albumSearch/{page}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<SearchDTO> albumSearch(@PathVariable(value = "searchValue", required = false) String searchValue,
                                                 @PathVariable(value="page")Integer page,
                                                 HttpServletRequest request) {

        Pageable requestedPage = PageRequest.of(page, 12);

        if(searchValue == null) searchValue = "";

        SearchDTO searchDTO = new SearchDTO();
        Set<Album> listAlbum = serviceAlbum.getAlbumBySearchValue(searchValue, requestedPage);
        serviceStatistiche.ricercaAlbum(); //STATISTICHE

        //creo una lista di albumDto dalla lista di album
        Set<AlbumDTO> listAlbumDto = new HashSet<>();
        for (Album album : listAlbum) {
            AlbumDTO dto = new AlbumDTO();
            dto.setIdAlbum(album.getIdAlbum());
            dto.setApprovato(album.getApprovato());
            dto.setCasaDiscografica(album.getCasaDiscografica());
            dto.setDescrizione(album.getDescrizione());
            dto.setDiscoOro(album.getDiscoOro());
            dto.setDataPubblicazione(album.getDataPubblicazione());
            dto.setDiscoPlatino(album.getDiscoPlatino());
            dto.setNome(album.getNome());
            dto.setUrlCopertina(album.getUrlCopertina());
            listAlbumDto.add(dto);
        }


        searchDTO.setAlbumList(listAlbumDto);

        return ResponseEntity.ok(searchDTO);
    }

    @GetMapping({"/albumNotApprovedSearch/{searchValue}/{page}", "/albumNotApprovedSearch/{page}"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SearchDTO> albumNotApprovedSearch(@PathVariable(value = "searchValue", required = false) String searchValue,
                                                            @PathVariable(value="page")Integer page,
                                                            HttpServletRequest request) {

        Pageable requestedPage = PageRequest.of(page, 12);

        if(searchValue == null) searchValue = "";
        SearchDTO searchDTO = new SearchDTO();
        Set<Album> listAlbum = serviceAlbum.getAlbumNotApprovedBySearchValue(searchValue, requestedPage);
        //creo una lista di albumDto dalla lista di album
        Set<AlbumDTO> listAlbumDto = new HashSet<>();
        for (Album album : listAlbum) {
            AlbumDTO dto = new AlbumDTO();
            dto.setIdAlbum(album.getIdAlbum());
            dto.setApprovato(album.getApprovato());
            dto.setCasaDiscografica(album.getCasaDiscografica());
            dto.setDescrizione(album.getDescrizione());
            dto.setDiscoOro(album.getDiscoOro());
            dto.setDataPubblicazione(album.getDataPubblicazione());
            dto.setDiscoPlatino(album.getDiscoPlatino());
            dto.setNome(album.getNome());
            dto.setUrlCopertina(album.getUrlCopertina());
            listAlbumDto.add(dto);
        }

        searchDTO.setAlbumList(listAlbumDto);

        return ResponseEntity.ok(searchDTO);
    }
}
