package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Featuring;
import com.DeBM.ApiDeBM.dto.FeaturingDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.repository.IRepositoryArtisti;
import com.DeBM.ApiDeBM.repository.IRepositoryFeaturing;
import com.DeBM.ApiDeBM.service.impl.ServiceFeaturing;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(
        path = "/featuring",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ControllerFeaturing {
    //---------------------------------------------------------------------------
    //                               FEATURING
    //---------------------------------------------------------------------------
    @Autowired
    private ServiceFeaturing serviceFeaturing;
    @Autowired
    private IRepositoryFeaturing iRepositoryFeaturing;
    @Autowired
    private IRepositoryArtisti iRepositoryArtisti;

    @PostMapping("/addFeaturing")
    @Secured("ROLE_Admin")
    public ResponseEntity inserisciFeaturing(@RequestBody FeaturingDTO dto, HttpServletRequest request) {
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
        Featuring dao = serviceFeaturing.inserisciFeaturing(dto);
        if(dao == null) throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok().build(); //servizio che ritorna dto e che prende come input un dto
    }

    @GetMapping("/getAllFeaturing")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<FeaturingDTO>> getAllFeaturing(HttpServletRequest request) {

        Set<Featuring> listFeaturing = serviceFeaturing.getallFeaturing();
        Set<FeaturingDTO> res = new ModelMapper().map(listFeaturing, new TypeToken<Set<FeaturingDTO>>(){}.getType());
        for(FeaturingDTO featuringDTO : res){
            featuringDTO.getArtisti().getListAlbum().clear();
            featuringDTO.getArtisti().getFeaturingList().clear();

        }
        return ResponseEntity.ok(res);
    }

    @GetMapping({"/getFeaturingByNomeArte/{nomeArte}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<FeaturingDTO>> getFeaturedByNomeArtista(@PathVariable(value = "nomeArte", required = false) String nomeArte,HttpServletRequest request) {

        if (nomeArte == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

        var daoSet = serviceFeaturing.getFeaturedByNomeArtista(nomeArte);

        if(daoSet.isEmpty())
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");

        Set<FeaturingDTO> res = new ModelMapper().map(daoSet, new TypeToken<Set<FeaturingDTO>>() {}.getType());

        return ResponseEntity.ok(res);
    }

    @PatchMapping({"/updateFeaturing/{id}", "updateFeaturing/"}) //Patch per UPDATE
    @Secured("ROLE_Admin")
    public ResponseEntity modificaFeaturing(@RequestBody FeaturingDTO featuringDTO, @PathVariable(value = "id", required = false) int id,HttpServletRequest request) {
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

        //if (titolo == null)
        //throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

        Featuring featuring = serviceFeaturing.modificaFeaturing(featuringDTO, id);
        if(featuring == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Featuring non trovato");

        FeaturingDTO dto = new FeaturingDTO();
        dto.setIdFeaturing(featuring.getIdFeaturing());
        dto.setScadenza(featuring.getScadenza());
        //dto.setNumeroTelefono(utente.getNumeriTelefonoList());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/deleteFeaturingById/{id}"})
    @Secured("ROLE_Admin")
    public ResponseEntity deleteFeaturing(@PathVariable(value = "id", required = false) int id,HttpServletRequest request) {
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
        String risposta = serviceFeaturing.eliminaFeaturing(id);
        if (risposta == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok("");
    }

    @GetMapping({"/getActiveFeaturingByInizioAndScadenza/{inizio}/{scadenza}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<FeaturingDTO>> getActiveFeaturingsBy(@PathVariable(value = "inizio", required = false) String inizio, @PathVariable(value = "scadenza", required = false) String scadenza ,HttpServletRequest request) {

        try {
            Date inizioD = new SimpleDateFormat("yyyy-MM-dd").parse(inizio);
            Date scadenzaD = new SimpleDateFormat("yyyy-MM-dd").parse(scadenza);

            if (inizioD == null || scadenzaD == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            var daoSet = serviceFeaturing.getActiveFeaturingsByInzioAndScadenza(inizioD, scadenzaD);

            if (daoSet.isEmpty())
                throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");

            Set<FeaturingDTO> res = new ModelMapper().map(daoSet, new TypeToken<Set<FeaturingDTO>>() {
            }.getType());

            return ResponseEntity.ok(res);
        }
        catch(ParseException exception)
        {
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore: scrivi la data nel formato yyyy/MM/dd");
        }
    }

    @GetMapping("/getActiveFeaturing")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<FeaturingDTO>> getActiveFeaturings(HttpServletRequest request) throws ParseException {
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault()).plusDays(1);
        Date dataOdierna = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(localDate));
        Set<Featuring> listFeaturing = serviceFeaturing.getActiveFeaturings(dataOdierna,dataOdierna);
        Set<FeaturingDTO> listFeaturingDTO = new HashSet<>();
        Set<FeaturingDTO> res = new ModelMapper().map(listFeaturing, new TypeToken<Set<FeaturingDTO>>(){}.getType());
        for(FeaturingDTO featuringDTO : res){
           featuringDTO.getArtisti().getListAlbum().clear();
           featuringDTO.getArtisti().getFeaturingList().clear();

        }
        return ResponseEntity.ok(res);

    }

}
