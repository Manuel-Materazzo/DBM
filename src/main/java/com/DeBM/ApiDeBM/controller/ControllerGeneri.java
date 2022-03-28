package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Artisti;
import com.DeBM.ApiDeBM.domain.Generi;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import com.DeBM.ApiDeBM.dto.GeneriDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.repository.IRepositoryGeneri;
import com.DeBM.ApiDeBM.service.impl.ServiceGeneri;
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
import java.util.Set;

@RestController
@RequestMapping(
        path = "/generi",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ControllerGeneri {
    //---------------------------------------------------------------------------
    //                               GENERI
    //---------------------------------------------------------------------------

    @Autowired
    private ServiceGeneri serviceGeneri;
    @Autowired
    private IRepositoryGeneri iRepositoryGeneri;
    @Autowired
    private ServiceStatistiche serviceStatistiche;

    @PostMapping("/addGenere")
    @Secured("ROLE_Admin")
    public ResponseEntity inserisciGenere(@RequestBody GeneriDTO dto, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer idAuth = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (idAuth == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            boolean approvato=false;
            if(auth.getAuthorities().stream().anyMatch(a ->a.getAuthority().equals("ROLE_Admin"))){
                approvato=true;
            }
            if (approvato == false)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        }
        Generi dao = serviceGeneri.inserisciGenere(dto);
        serviceStatistiche.getAumentoGeneri(); //STATISTICHE
        if(dao == null) throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok().build(); //servizio che ritorna dto e che prende come input un dto
    }

    @PatchMapping({"/updateGenere/{tipologia}", "/updateGenere/"}) //Patch per UPDATE
    @Secured("ROLE_Admin")
    public ResponseEntity modificaGenere(@RequestBody GeneriDTO generiDTO, @PathVariable(value = "tipologia", required = false) String tipologia, HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer idAuth = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (idAuth == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            boolean approvato=false;
            if(auth.getAuthorities().stream().anyMatch(a ->a.getAuthority().equals("ROLE_Admin"))){
                approvato=true;
            }
            if (approvato == false)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        }

        if (tipologia == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        if(generiDTO == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

        Generi generi = serviceGeneri.modificaGenere(generiDTO, tipologia);

        GeneriDTO dto = new GeneriDTO();
        dto.setIdGenere(generi.getIdGenere());
        dto.setDescrizione(generi.getDescrizione());
        dto.setTipologia(generi.getTipologia());
        //dto.setNumeroTelefono(utente.getNumeriTelefonoList());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/deleteGenereByTipologia/{tipologia}"})
    @Secured("ROLE_Admin")
    public ResponseEntity deleteGenere(@PathVariable(value = "tipologia", required = false) String tipologia,HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer idAuth = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (idAuth == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            boolean approvato=false;
            if(auth.getAuthorities().stream().anyMatch(a ->a.getAuthority().equals("ROLE_Admin"))){
                approvato=true;
            }
            if (approvato == false)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        }
        String risposta = serviceGeneri.eliminaGenere(tipologia);
        serviceStatistiche.getDiminuzioneGeneri(); //STATISTICHE
        if (risposta == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok("");
    }

    @DeleteMapping({"/deleteGenereById/{id}"})
    @Secured("ROLE_Admin")
    public ResponseEntity deleteGenereById(@PathVariable(value = "id", required = false) int id,HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer idAuth = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (idAuth == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            boolean approvato=false;
            if(auth.getAuthorities().stream().anyMatch(a ->a.getAuthority().equals("ROLE_Admin"))){
                approvato=true;
            }
            if (approvato == false)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        }
        String risposta = serviceGeneri.eliminaGenereById(id);
        serviceStatistiche.getDiminuzioneGeneri(); //STATISTICHE
        if (risposta == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok("");
    }

    @GetMapping("/getAllGeneri")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<GeneriDTO>> getAllGeneri(HttpServletRequest request) {

        Set<Generi> listGeneri = serviceGeneri.getAllGeneri();
        Set<GeneriDTO> res = new ModelMapper().map(listGeneri, new TypeToken<Set<GeneriDTO>>(){}.getType());
        for (GeneriDTO genereDTO:res) {
            for (BraniDTO braniDTO:genereDTO.getListBrani()) {
                braniDTO.getListGeneri().clear();

            }

        }
        return ResponseEntity.ok(res);
    }

    @GetMapping({"/getGenereByTipologia/{tipologia}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<GeneriDTO> getGenere(@PathVariable(value = "tipologia", required = false) String tipologia,HttpServletRequest request) {


        if (tipologia == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        Generi generi = serviceGeneri.getGenere(tipologia);
        var res = new ModelMapper().map(generi,GeneriDTO.class);
        res.getListBrani().clear();
        return ResponseEntity.ok(res);
    }

    @GetMapping({"/getGeneriByTitoloCanzone/{titolo}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<GeneriDTO>> getGeneri(@PathVariable(value = "titolo", required = false) String titolo,HttpServletRequest request) {

        if (titolo == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

        var daoSet = serviceGeneri.getGeneriByTitoloBrano(titolo);

        if(daoSet.isEmpty())
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");

        Set<GeneriDTO> res = new ModelMapper().map(daoSet, new TypeToken<Set<GeneriDTO>>() {}.getType());
        for (GeneriDTO genereDTO:res) {
            genereDTO.getListBrani().clear();

        }



        return ResponseEntity.ok(res);
    }

}
