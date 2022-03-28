package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Album;
import com.DeBM.ApiDeBM.domain.Artisti;
import com.DeBM.ApiDeBM.domain.Brani;
import com.DeBM.ApiDeBM.dto.AlbumDTO;
import com.DeBM.ApiDeBM.dto.ArtistiDTO;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import com.DeBM.ApiDeBM.dto.GeneriDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.repository.IRepositoryAlbum;
import com.DeBM.ApiDeBM.repository.IRepositoryBrani;
import com.DeBM.ApiDeBM.service.impl.ServiceAlbum;
import com.DeBM.ApiDeBM.service.impl.ServiceBrani;
import com.DeBM.ApiDeBM.service.impl.ServiceStatistiche;
import com.google.common.reflect.TypeToken;
import io.swagger.annotations.ApiOperation;
import lombok.var;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(
            path = "/brani",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ControllerBrani {

    //---------------------------------------------------------------------------
    //                               BRANI
    //---------------------------------------------------------------------------
    @Autowired
    private ServiceBrani serviceBrani;
    @Autowired
    private IRepositoryBrani iRepositoryBrani;
    @Autowired
    private ServiceAlbum serviceAlbum;
    @Autowired
    private ServiceStatistiche serviceStatistiche;
    @Autowired
    private IRepositoryAlbum iRepositoryAlbum;

    @PostMapping("/addBrano")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity inserisciBrano(@RequestBody BraniDTO dto, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer idArtista = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (idArtista == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            Album album= iRepositoryAlbum.findById(dto.getAlbumID()).get();
            boolean trovato=false;
            for(Artisti artisti:album.getListArtisti()){
                if(artisti.getIdArtista()==idArtista){
                    trovato=true;
                }

            }
            if (trovato == false)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

        }
        Brani dao = serviceBrani.inserisciBrano(dto);
        serviceStatistiche.getAumentoBrani(); //STATISTICHE
        if(dao == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok().build();//servizio che ritorna dto e che prende come input un dto
    }

    @PatchMapping({"/updateBrano"}) //Patch per UPDATE
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity modificaBrano(@RequestBody BraniDTO branoDto, HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer idArtista = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (idArtista == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            Album album= iRepositoryAlbum.findById(branoDto.getAlbumID()).get();
            boolean trovato=false;
            for(Artisti artisti:album.getListArtisti()){
                if(artisti.getIdArtista()==idArtista){
                    trovato=true;
                }

            }
            if (trovato == false)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

        }

        Brani brani = serviceBrani.modificaBrano(branoDto);
        BraniDTO dto = new BraniDTO();
        dto.setIdBrano(brani.getIdBrano());
        dto.setTitolo(brani.getTitolo());
        dto.setDurata(brani.getDurata());
        dto.setUrl(brani.getUrl());
        dto.setApprovato(brani.getApprovato());
        return ResponseEntity.ok().build();
    }

    @PatchMapping({"/approvaBrano"}) //Patch per UPDATE
    @Secured("ROLE_Admin")
    public ResponseEntity approvaBrano(@RequestBody BraniDTO branoDto, HttpServletRequest request) {

        Brani brani = serviceBrani.approvaBrano(branoDto);

        Boolean isAlbumApprovato = null;

        for(Brani brano : brani.getAlbum().getBraniList()){
            if(isAlbumApprovato == null)
                isAlbumApprovato = brano.getApprovato();
            isAlbumApprovato = isAlbumApprovato && brano.getApprovato();
        }

        if(isAlbumApprovato){
            serviceAlbum.approvaAlbum(brani.getAlbum());
        }

        BraniDTO dto = new BraniDTO();
        dto.setIdBrano(brani.getIdBrano());
        dto.setTitolo(brani.getTitolo());
        dto.setDurata(brani.getDurata());
        dto.setUrl(brani.getUrl());
        dto.setApprovato(brani.getApprovato());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/deleteBranoByTitolo/{titolo}"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity deleteBrano(@PathVariable(value = "titolo", required = false) String titolo,HttpServletRequest request) {
        String risposta = serviceBrani.eliminaBrano(titolo);
        serviceStatistiche.getDiminuzioneBrani(); //STATISTICHE
        if (risposta == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok("");
    }

    @DeleteMapping({"/deleteBranoById/{id}"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity deleteBranoById(@PathVariable(value = "id", required = false) int id,HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer idArtista = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (idArtista == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            Brani brani = iRepositoryBrani.findById(id).get();
            Album album = brani.getAlbum();
            boolean trovato=false;
            for(Artisti artisti:album.getListArtisti()){
                if(artisti.getIdArtista()==idArtista){
                    trovato=true;
                }

            }
            if (trovato == false)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

        }

        String risposta = serviceBrani.eliminaBranoById(id);
        serviceStatistiche.getDiminuzioneBrani(); //STATISTICHE
        if (risposta == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok("");
    }

    @GetMapping("/getAllBrani")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<BraniDTO>> getAllBrani(HttpServletRequest request) {

        Set<Brani> listBrani = serviceBrani.getAllBrani();
        Set<BraniDTO> listBraniDto = new HashSet<>();
        if (listBrani == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");
        for (Brani brani : listBrani) {
            BraniDTO dto = new BraniDTO();
            dto.setIdBrano(brani.getIdBrano());
            dto.setTitolo(brani.getTitolo());
            dto.setDurata(brani.getDurata());
            dto.setUrl(brani.getUrl());
            dto.setApprovato(brani.getApprovato());
            listBraniDto.add(dto);
        }
        return ResponseEntity.ok(listBraniDto);
    }

    @GetMapping({"/getBranoByTitolo/{titolo}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<BraniDTO> getBrano(@PathVariable(value = "titolo", required = false) String titolo,HttpServletRequest request) {

        BraniDTO dto = new BraniDTO();
        if (titolo == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        Brani brani = serviceBrani.getBrano(titolo);
        dto.setIdBrano(brani.getIdBrano());
        dto.setTitolo(brani.getTitolo());
        dto.setDurata(brani.getDurata());
        dto.setUrl(brani.getUrl());
        dto.setApprovato(brani.getApprovato());
        return ResponseEntity.ok(dto);
    }

    @GetMapping({"/getBranoById/{id}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<BraniDTO> getBranoById(@PathVariable(value = "id") Integer id) {

        BraniDTO dto = new BraniDTO();

        Brani brani = serviceBrani.getBranoById(id);
        dto.setIdBrano(brani.getIdBrano());
        dto.setTitolo(brani.getTitolo());
        dto.setDurata(brani.getDurata());
        dto.setUrl(brani.getUrl());
        dto.setApprovato(brani.getApprovato());
        return ResponseEntity.ok(dto);
    }

    @GetMapping({"/getBraniByIdAlbum/{id}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<BraniDTO>> getBrano(@PathVariable(value = "id", required = false) Integer id,HttpServletRequest request) {

        if (id == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

        var daoSet = serviceBrani.getBraniByIdAlbum(id);

        if(daoSet.isEmpty())
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");

        Set<BraniDTO> res = new ModelMapper().map(daoSet, new TypeToken<Set<BraniDTO>>() {}.getType());
        for (BraniDTO branoDTO : res) {
            for (GeneriDTO generiDTO : branoDTO.getListGeneri()) {
                generiDTO.getListBrani().clear();
            }
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping({"/getBraniNotApprovedInAlbumApproved/"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Set<BraniDTO>> getBraniNotApprovedInAlbumApproved(HttpServletRequest request) {

        //ritorna tutti i brani che non sono ancora stati approvati che sono all'interno di un'album già approvato
        //TODO: verificare funzionamento caso Album NON approvati/Brani non approvati
        boolean approvatoBrani = false;
        boolean approvatoAlbum = true;
        Set<Brani> listBrani = serviceBrani.getBraniNotApprovedInAlbumApproved(approvatoBrani, approvatoAlbum);
        Set<BraniDTO> listBraniDto = new HashSet<>();
        if (listBrani == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");
        for (Brani brani : listBrani) {
            BraniDTO dto = new BraniDTO();
            dto.setIdBrano(brani.getIdBrano());
            dto.setTitolo(brani.getTitolo());
            dto.setDurata(brani.getDurata());
            dto.setUrl(brani.getUrl());
            dto.setApprovato(brani.getApprovato());
            listBraniDto.add(dto);
        }
        return ResponseEntity.ok(listBraniDto);
    }

    @GetMapping({"/getBraniByApprovato/{approvato}"})
    @Secured("ROLE_Admin")
    public ResponseEntity<Set<BraniDTO>> getBraniByApprovato(@PathVariable(value = "approvato", required = false) boolean approvato,HttpServletRequest request) {

        Set<Brani> listBrani = serviceBrani.getBraniByApprovato(approvato);
        Set<BraniDTO> listBraniDto = new HashSet<>();
        if (listBrani == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");
        for (Brani brani : listBrani) {
            BraniDTO dto = new BraniDTO();
            dto.setIdBrano(brani.getIdBrano());
            dto.setTitolo(brani.getTitolo());
            dto.setDurata(brani.getDurata());
            dto.setUrl(brani.getUrl());
            dto.setApprovato(brani.getApprovato());
            listBraniDto.add(dto);
        }
        return ResponseEntity.ok(listBraniDto);
    }
}
