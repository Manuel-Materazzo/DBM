package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Artisti;
import com.DeBM.ApiDeBM.dto.AlbumDTO;
import com.DeBM.ApiDeBM.dto.ArtistiDTO;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.repository.IRepositoryAlbum;
import com.DeBM.ApiDeBM.repository.IRepositoryArtisti;
import com.DeBM.ApiDeBM.repository.IRepositoryBrani;
import com.DeBM.ApiDeBM.service.impl.ServiceArtisti;
import com.DeBM.ApiDeBM.service.impl.ServiceStatistiche;
import io.swagger.annotations.ApiOperation;
import lombok.var;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(
        path = "/artisti",
        produces = MediaType.APPLICATION_JSON_VALUE
)

public class ControllerArtisti {

    //---------------------------------------------------------------------------
    //                             ARTISTI
    //---------------------------------------------------------------------------

    @Autowired
    private ServiceArtisti serviceArtisti;
    @Autowired
    private IRepositoryArtisti iRepositoryArtisti;
    @Autowired
    private IRepositoryAlbum iRepositoryAlbum;
    @Autowired
    private IRepositoryBrani iRepositoryBrani;
    @Autowired
    private ServiceStatistiche serviceStatistiche;

    @PostMapping("/addArtista")
    public ResponseEntity inserisciArtista(@RequestBody ArtistiDTO dto) {
        Artisti dao = serviceArtisti.inserisciArtista(dto);
        serviceStatistiche.getAumentoArtisti(); //STATISTICHE
        if (dao == null) throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok().build(); //servizio che ritorna dto e che prende come input un dto
    }

    @PatchMapping("/updateArtista") //Patch per UPDATE
    public ResponseEntity modificaArtista(@RequestBody ArtistiDTO artistaDto, HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (id == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            Artisti artista = iRepositoryArtisti.findById(id).get();
            boolean approvato=false;
            if(artista.getIdArtista()==artistaDto.getIdArtista() && artista.getBannato()==false) {
                approvato = true;
            }

            if (approvato == false)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        }


        //TODO: controllo token per id artista e bannato

        Artisti artista = serviceArtisti.modificaArtista(artistaDto);

        var dto = new ModelMapper().map(artista, ArtistiDTO.class);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteArtistaById/{id}")
    public ResponseEntity deleteArtista(@PathVariable(value = "id") Integer id, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer idAuth = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (idAuth == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            Artisti artista = iRepositoryArtisti.findById(id).get();
            boolean approvato=false;
            if(auth.getAuthorities().stream().anyMatch(a ->a.getAuthority().equals("ROLE_Admin"))){
                approvato=true;
            }
            if (approvato == false)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        }
        String risposta = serviceArtisti.eliminaArtistaById(id);
        serviceStatistiche.getDiminuzioneArtisti(); //STATISTICHE
        if (risposta == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok("");
    }

    @GetMapping("/getAllArtisti")
    @ApiOperation("Ritorna la lista di tutti gli artisti")
    public ResponseEntity<Set<ArtistiDTO>> getAllArtisti() {

        Set<Artisti> listArtisti = serviceArtisti.getAllArtisti();
        Set<ArtistiDTO> listArtistaDto = new HashSet<>();
        if (listArtisti == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");
        for (Artisti artista : listArtisti) {
            ArtistiDTO dto = new ArtistiDTO();
            dto.setIdArtista(artista.getIdArtista());
            dto.setBio(artista.getBio());
            dto.setNomeArte(artista.getNomeArte());
            dto.setEmail(artista.getEmail());
            dto.setUrlAvatar(artista.getUrlAvatar());
            dto.setBannato(artista.getBannato());
            listArtistaDto.add(dto);
        }
        return ResponseEntity.ok(listArtistaDto);
    }

    @GetMapping("/getArtistaById/{id}")
    public ResponseEntity<ArtistiDTO> getArtistaById(@PathVariable(value = "id") Integer id) {

        if (id == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        Artisti artista = serviceArtisti.getArtistaById(id);

        var dto = new ModelMapper().map(artista, ArtistiDTO.class);
        dto.setFeaturingList(null);

        for(AlbumDTO album : dto.getListAlbum()){
            album.getListArtisti().clear();
            for(BraniDTO braniDTO : album.getBraniList()){
                braniDTO.getListGeneri().clear();
            }
        }

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/getProfilo")
    public ResponseEntity<ArtistiDTO> getProfilo(HttpServletRequest request) {

        RefreshableKeycloakSecurityContext context =
                (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

        AccessToken accessToken = context.getToken();

        Integer id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

        Artisti artista = serviceArtisti.getArtistaById(id);

        var dto = new ModelMapper().map(artista, ArtistiDTO.class);

        dto.getListAlbum().clear();
        dto.getFeaturingList().clear();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        //imposto se admin
        dto.setAdmin(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin")));

        return ResponseEntity.ok(dto);
    }

    //--------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/getArtistaByNomeArte/{nomeArte}")
    @ApiOperation("Ritorna un artista")
    public ResponseEntity<ArtistiDTO> getArtistaByNomeArte(@PathVariable(value = "nomeArte") String nomeArte) {

        ArtistiDTO dto = new ArtistiDTO();
        if (nomeArte == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        Artisti artisti = serviceArtisti.getArtista(nomeArte);
        dto.setIdArtista(artisti.getIdArtista());
        dto.setBio(artisti.getBio());
        dto.setNomeArte(artisti.getNomeArte());
        dto.setEmail(artisti.getEmail());
        dto.setUrlAvatar(artisti.getUrlAvatar());
        dto.setBannato(artisti.getBannato());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/deleteArtistaByNomeArte/{nomeArte}")
    public ResponseEntity deleteArtista(@PathVariable(value = "nomeArte") String nomeArte, HttpServletRequest request) {
        String nomeArteToken = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {

            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            //UUID keycloakUUID = UUID.fromString(accessToken.getOtherClaims().get("user_id").toString());
            nomeArteToken = accessToken.getOtherClaims().get("idArtista").toString();

        }

        if (nomeArteToken == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");


        String risposta = serviceArtisti.eliminaArtista(nomeArte);
        serviceStatistiche.getDiminuzioneArtisti(); //STATISTICHE
        if (risposta == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok("");
    }
}
