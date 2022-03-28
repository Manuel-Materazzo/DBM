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
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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

        Set<AlbumDTO> listAlbumDto = new ModelMapper().map(listAlbum, new TypeToken<Set<AlbumDTO>>(){}.getType());
        Set<ArtistiDTO> listArtistiDto = new ModelMapper().map(listArtisti, new TypeToken<Set<ArtistiDTO>>(){}.getType());
        Set<BraniDTO> listBraniDTO = new ModelMapper().map(listBrani, new TypeToken<Set<BraniDTO>>(){}.getType());

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

        Set<ArtistiDTO> listArtistiDto = new ModelMapper().map(listArtisti, new TypeToken<Set<ArtistiDTO>>(){}.getType());

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

        Set<AlbumDTO> listAlbumDto = new ModelMapper().map(listAlbum, new TypeToken<Set<AlbumDTO>>(){}.getType());

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
        Set<AlbumDTO> listAlbumDto = new ModelMapper().map(listAlbum, new TypeToken<Set<AlbumDTO>>(){}.getType());

        searchDTO.setAlbumList(listAlbumDto);

        return ResponseEntity.ok(searchDTO);
    }
}
