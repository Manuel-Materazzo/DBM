package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Artisti;
import com.DeBM.ApiDeBM.dto.ArtistiDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.repository.IRepositoryArtisti;
import com.DeBM.ApiDeBM.service.impl.ServiceArtisti;
import lombok.var;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
        path = "/artisti",
        produces = MediaType.APPLICATION_JSON_VALUE
)

public class ControllerArtisti {

    @Autowired
    private ServiceArtisti serviceArtisti;
    @Autowired
    private IRepositoryArtisti iRepositoryArtisti;

    @PostMapping("/addArtista")
    @Secured("ROLE_Admin")
    public ResponseEntity inserisciArtista(@RequestBody ArtistiDTO dto) {
        Artisti dao = serviceArtisti.inserisciArtista(dto);
        if (dao == null) throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore durante l'inserimento");
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/updateArtista")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity modificaArtista(@RequestBody ArtistiDTO artistaDto, HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            Artisti artista = iRepositoryArtisti.findById(id).get();

            boolean approvato = artista.getIdArtista() == artistaDto.getIdArtista() && !artista.getBannato();

            if (!approvato)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Non sei autorizzato a modificare questo profilo");
        }

        serviceArtisti.modificaArtista(artistaDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteArtistaById/{id}")
    @Secured("ROLE_Admin")
    public ResponseEntity deleteArtista(@PathVariable(value = "id") Integer id, HttpServletRequest request) {
        boolean risposta = serviceArtisti.eliminaArtistaById(id);
        if (!risposta)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile eliminare il profilo");
        return ResponseEntity.ok("");
    }

    @GetMapping("/getAllArtisti")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Set<ArtistiDTO>> getAllArtisti() {

        Set<Artisti> listArtisti = serviceArtisti.getAllArtisti();

        if (listArtisti == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");

        Set<ArtistiDTO> res = new ModelMapper().map(listArtisti, new TypeToken<Set<ArtistiDTO>>(){}.getType());

        return ResponseEntity.ok(res);
    }

    @GetMapping("/getArtistaById/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ArtistiDTO> getArtistaById(@PathVariable(value = "id") int id) {

        Artisti artista = serviceArtisti.getArtistaById(id);

        if (artista == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Artista non trovato");

        var dto = new ModelMapper().map(artista, ArtistiDTO.class);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getProfilo")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArtistiDTO> getProfilo(HttpServletRequest request) {

        RefreshableKeycloakSecurityContext context =
                (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

        AccessToken accessToken = context.getToken();

        Integer id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

        Artisti artista = serviceArtisti.getArtistaById(id);

        var dto = new ModelMapper().map(artista, ArtistiDTO.class);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //imposto se admin
        dto.setAdmin(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin")));

        return ResponseEntity.ok(dto);
    }
}
