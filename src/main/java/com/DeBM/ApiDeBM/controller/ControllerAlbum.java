package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Album;
import com.DeBM.ApiDeBM.domain.Brani;
import com.DeBM.ApiDeBM.dto.AlbumDTO;
import com.DeBM.ApiDeBM.dto.ArtistiDTO;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import com.DeBM.ApiDeBM.dto.GeneriDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.repository.IRepositoryBrani;
import com.DeBM.ApiDeBM.service.impl.ServiceAlbum;
import com.DeBM.ApiDeBM.service.impl.ServiceBrani;
import com.DeBM.ApiDeBM.service.impl.ServiceStatistiche;
import io.swagger.annotations.ApiOperation;
import lombok.var;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(
        path = "/album",
        produces = MediaType.APPLICATION_JSON_VALUE
)

public class ControllerAlbum {

    //---------------------------------------------------------------------------
    //                               ALBUM
    //---------------------------------------------------------------------------
    @Autowired
    private ServiceAlbum serviceAlbum;
    @Autowired
    private ServiceBrani serviceBrani;
    @Autowired
    private IRepositoryBrani iRepositoryBrani;
    @Autowired
    private ServiceStatistiche serviceStatistiche;

    @PostMapping("/addAlbum")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity inserisciAlbum(@RequestBody AlbumDTO dto, HttpServletRequest request) {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (id == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            dto.setListArtisti(new HashSet<>());
            dto.getListArtisti().add(ArtistiDTO.builder().idArtista(id).build());
        }

        Album dao = serviceAlbum.inserisciAlbum(dto);
        if (dao == null) throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok().build(); //servizio che ritorna dto e che prende come input un dto
    }

    @PostMapping("/addAlbumWithSongs")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AlbumDTO> inserisciAlbumWithSongs(@RequestBody AlbumDTO dto, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && (dto.getListArtisti().isEmpty() || !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin")))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (id == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            dto.setListArtisti(new HashSet<>());
            dto.getListArtisti().add(ArtistiDTO.builder().idArtista(id).build());
        }

        Album dao = serviceAlbum.inserisciAlbumWithSongs(dto);
        serviceStatistiche.getAumentoAlbum(); //STATISTICHE
        if (dao == null) throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok(dto); //servizio che ritorna dto e che prende come input un dto
    }

    @PatchMapping({"/updateAlbum/{nome}"}) //Patch per UPDATE
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity modificaAlbum(@RequestBody AlbumDTO dto, @PathVariable(value = "nome", required = false) String nome, HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            //UUID keycloakUUID = UUID.fromString(accessToken.getOtherClaims().get("user_id").toString());
            Integer id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (id == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            dto.setListArtisti(new HashSet<>());
            dto.getListArtisti().add(ArtistiDTO.builder().idArtista(id).build());
        }

        Album album = serviceAlbum.modificaAlbum(dto, nome);
        album.setIdAlbum(dto.getIdAlbum());  //setta lo username nuovo
        album.setNome(dto.getNome());
        album.setDataPubblicazione(dto.getDataPubblicazione());
        album.setDescrizione(dto.getDescrizione());
        album.setCasaDiscografica(dto.getCasaDiscografica());
        album.setUrlCopertina(dto.getUrlCopertina());
        album.setApprovato(dto.getApprovato());
        album.setDiscoOro(dto.getDiscoOro());
        album.setDiscoPlatino(dto.getDiscoPlatino());
        return ResponseEntity.ok().build();
    }

    @PatchMapping({"/updateAlbumById"}) //Patch per UPDATE
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AlbumDTO> modificaAlbumById(@RequestBody AlbumDTO dto, HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            //UUID keycloakUUID = UUID.fromString(accessToken.getOtherClaims().get("user_id").toString());
            Integer id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (id == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            dto.setListArtisti(new HashSet<>());
            dto.getListArtisti().add(ArtistiDTO.builder().idArtista(id).build());
        }

        Album album = serviceAlbum.updateAlbumById(dto);
        album.setNome(dto.getNome());
        album.setDataPubblicazione(dto.getDataPubblicazione());
        album.setDescrizione(dto.getDescrizione());
        album.setCasaDiscografica(dto.getCasaDiscografica());
        album.setUrlCopertina(dto.getUrlCopertina());
        album.setApprovato(dto.getApprovato());
        album.setDiscoOro(dto.getDiscoOro());
        album.setDiscoPlatino(dto.getDiscoPlatino());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping({"/deleteAlbumByNome/{nome}"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity eliminaAlbum(@PathVariable(value = "nome", required = false) String nome, HttpServletRequest request) {

        String risposta = serviceAlbum.eliminaAlbum(nome);
        serviceStatistiche.getDiminuzioneAlbum(); //STATISTICHE
        if (risposta == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/deleteAlbumById/{id}"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity eliminaAlbumById(@PathVariable(value = "id", required = false) int id, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {


            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            //UUID keycloakUUID = UUID.fromString(accessToken.getOtherClaims().get("user_id").toString());
            int idAuth = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (idAuth == 0)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        }


        String risposta = serviceAlbum.eliminaAlbumById(id);
        serviceStatistiche.getDiminuzioneAlbum(); //STATISTICHE
        if (risposta == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAllAlbum")
    @Secured("ROLE_Admin")
    @ApiOperation("Ritorna la lista gli album")
    public ResponseEntity<Set<AlbumDTO>> getAllAlbum(HttpServletRequest request) {
        Set<Album> listAlbum = serviceAlbum.getAllAlbum();
        Set<AlbumDTO> listAlbumDto = new HashSet<>();
        if (listAlbum == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");
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
        return ResponseEntity.ok(listAlbumDto);
    }

    @GetMapping({"/getAlbumByNome/{nome}", "/getAlbumByNome/"})
    @ApiOperation("Ritorna un album")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AlbumDTO> getAlbum(@PathVariable(value = "nome", required = false) String nome, HttpServletRequest request) {

        AlbumDTO dto = new AlbumDTO();
        if (nome == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");
        Album album = serviceAlbum.getAlbum(nome);
        dto.setIdAlbum(album.getIdAlbum());
        dto.setApprovato(album.getApprovato());
        dto.setCasaDiscografica(album.getCasaDiscografica());
        dto.setDescrizione(album.getDescrizione());
        dto.setDiscoOro(album.getDiscoOro());
        dto.setDataPubblicazione(album.getDataPubblicazione());
        dto.setDiscoPlatino(album.getDiscoPlatino());
        dto.setNome(album.getNome());
        dto.setUrlCopertina(album.getUrlCopertina());
        return ResponseEntity.ok(dto);
    }

    @GetMapping({"/getAlbumByIdArtista/{id}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<AlbumDTO>> getAlbumByIdArtista(@PathVariable(value = "id", required = false) Integer id, HttpServletRequest request) {

        //TODO: template model mapper
        if (id == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

        Set<Album> listAlbum = serviceAlbum.getAlbumByIdAutore(id);
        Set<AlbumDTO> listAlbumDto = new HashSet<>();
        if (listAlbum == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");

        Set<AlbumDTO> res = new ModelMapper().map(listAlbum, new TypeToken<Set<AlbumDTO>>() {
        }.getType());
        for (AlbumDTO albumDTO : res) {
            for (BraniDTO braniDTO : albumDTO.getBraniList()){
                for(GeneriDTO generiDTO : braniDTO.getListGeneri()){
                    generiDTO.getListBrani().clear();
                }

            }
            albumDTO.getListArtisti().clear();
        }


        return ResponseEntity.ok(res);
    }

    @GetMapping({"/getTopAlbumByIdArtista"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<AlbumDTO>> getTopAlbumByIdArtista(HttpServletRequest request) {

        Set<AlbumDTO> res = new HashSet<>();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {

            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (id == null)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            Set<Album> listAlbum = serviceAlbum.getTopAlbumByIdArtista(id);
            res = new ModelMapper().map(listAlbum, new TypeToken<Set<AlbumDTO>>(){}.getType());
            for (AlbumDTO albumDTO : res) {
                for (BraniDTO braniDTO : albumDTO.getBraniList()){
                    for(GeneriDTO generiDTO : braniDTO.getListGeneri()){
                        generiDTO.getListBrani().clear();
                    }

                }
                albumDTO.getListArtisti().clear();
            }
        }

        return ResponseEntity.ok(res);
    }

    @GetMapping({"/getAlbumNotApprovedWithBrani/{approvato}"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Set<AlbumDTO>> getAlbumNotApprovedWithBrani(@PathVariable(value = "approvato", required = false)
                                                                              boolean approvato, HttpServletRequest request) {
        Set<Album> listAlbum = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"));
        if (!admin) {

            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            //UUID keycloakUUID = UUID.fromString(accessToken.getOtherClaims().get("user_id").toString());
            int id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());
            listAlbum = serviceAlbum.getAlbumNotApprovedWithBraniById(approvato, id);

        } else {
            listAlbum = serviceAlbum.getAlbumNotApprovedWithBrani(approvato);

        }

        Set<AlbumDTO> res = new ModelMapper().map(listAlbum, new TypeToken<Set<AlbumDTO>>() {
        }.getType());
        for (AlbumDTO albumDTO : res) {
            for (BraniDTO braniDTO : albumDTO.getBraniList()){
                for(GeneriDTO generiDTO : braniDTO.getListGeneri()){
                    generiDTO.getListBrani().clear();
                }

            }
            albumDTO.getListArtisti().clear();
        }
        return ResponseEntity.ok(res);
    }
    @GetMapping({"/getAlbumWithBraniById/{id}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<AlbumDTO> getAlbumWithBraniById(@PathVariable(value = "id") int id) {

        Album album = serviceAlbum.getAlbumWithBraniById(id);
        if (album == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");
        var res = new ModelMapper().map(album, AlbumDTO.class);
        res.getListArtisti().clear();
        for (BraniDTO braniDTO: res.getBraniList()) {
            braniDTO.getListGeneri().clear();
        }
        return ResponseEntity.ok(res);
    }

}
