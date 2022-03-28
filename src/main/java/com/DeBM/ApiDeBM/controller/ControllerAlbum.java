package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Album;
import com.DeBM.ApiDeBM.domain.Brani;
import com.DeBM.ApiDeBM.domain.BraniGeneri;
import com.DeBM.ApiDeBM.dto.AlbumDTO;
import com.DeBM.ApiDeBM.dto.ArtistiDTO;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import com.DeBM.ApiDeBM.dto.GeneriDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.service.impl.ServiceAlbum;
import lombok.var;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
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

    @Autowired
    private ServiceAlbum serviceAlbum;

    @PostMapping({"/addAlbum", "/addAlbumWithSongs"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity inserisciAlbum(@RequestBody AlbumDTO dto, HttpServletRequest request) {

        boolean invalidArtistCheck = false;
        //controllo se uno degli artisti ha id 0, e invalido la procedura
        if (dto.getListArtisti() != null)
            for (ArtistiDTO artisti : dto.getListArtisti()) {
                if (artisti.getIdArtista() == 0) {
                    invalidArtistCheck = true;
                    break;
                }
            }

        //cambia cambia l'artista nel dto nell'identita dell'utente se non è admin o l'id è stato impostato a 0
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && (invalidArtistCheck
                || !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin")))) {

            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            Integer id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            Set<ArtistiDTO> artisti = new HashSet<>();
            artisti.add(ArtistiDTO.builder().idArtista(id).build());
            dto.setListArtisti(artisti);
        }

        Album dao = serviceAlbum.inserisciAlbum(dto);
        if (dao == null) throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile aggiungere l'album");
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
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile aggiornare l'album");

            dto.setListArtisti(new HashSet<>());
            dto.getListArtisti().add(ArtistiDTO.builder().idArtista(id).build());
        }

        serviceAlbum.updateAlbumById(dto);

        return ResponseEntity.ok(dto);
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
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile eliminare l'album");
        }

        boolean risposta = serviceAlbum.eliminaAlbumById(id);
        if (!risposta)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile eliminare l'album");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAllAlbum")
    @Secured("ROLE_Admin")
    public ResponseEntity<Set<AlbumDTO>> getAllAlbum(HttpServletRequest request) {
        Set<Album> listAlbum = serviceAlbum.getAllAlbum();
        if (listAlbum == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");

        //non posso usare il modelmapper per le liste, trascurerebbe la lista di generi
        //Set<AlbumDTO> res = new ModelMapper().map(listAlbum, new TypeToken<Set<AlbumDTO>>() {}.getType());

        var res = albumListToDao(listAlbum);

        return ResponseEntity.ok(res);
    }

    @GetMapping({"/getAlbumByIdArtista/{id}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<AlbumDTO>> getAlbumByIdArtista(@PathVariable(value = "id") int id, HttpServletRequest request) {

        Set<Album> listAlbum = serviceAlbum.getAlbumByIdArtista(id);

        //rispondo con lista vuota se l'artista esiste ma non ha album
        if (listAlbum == null)
            return ResponseEntity.ok(new HashSet<>());

        //non posso usare il modelmapper per le liste, trascurerebbe la lista di generi
        //Set<AlbumDTO> res = new ModelMapper().map(listAlbum, new TypeToken<Set<AlbumDTO>>() {}.getType());

        var res = albumListToDao(listAlbum);

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

            int id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());

            if (id == 0)
                throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Errore");

            Set<Album> listAlbum = serviceAlbum.getTopAlbumByIdArtista(id);

            //non posso usare il modelmapper per le liste, trascurerebbe la lista di generi
            //Set<AlbumDTO> res = new ModelMapper().map(listAlbum, new TypeToken<Set<AlbumDTO>>() {}.getType());

            res = albumListToDao(listAlbum);

        }

        return ResponseEntity.ok(res);
    }

    @GetMapping({"/getAlbumNotApprovedWithBrani/{approvato}", "/getAlbumNotApprovedWithBrani"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Set<AlbumDTO>> getAlbumNotApprovedWithBrani(@PathVariable(value = "approvato", required = false)
                                                                              Boolean approvato, HttpServletRequest request) {
        Set<Album> listAlbum;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean admin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"));
        if (!admin) {

            RefreshableKeycloakSecurityContext context =
                    (RefreshableKeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());

            AccessToken accessToken = context.getToken();

            int id = Integer.parseInt(accessToken.getOtherClaims().get("idArtista").toString());
            listAlbum = serviceAlbum.getAlbumNotApprovedWithBraniById(Boolean.TRUE.equals(approvato), id);

        } else {
            listAlbum = serviceAlbum.getAlbumNotApprovedWithBrani(Boolean.TRUE.equals(approvato));

        }

        if (listAlbum == null) {
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Qualcosa è andato storto durante il recupero dei dati");
        }

        //non posso usare il modelmapper per le liste, trascurerebbe la lista di generi
        //Set<AlbumDTO> res = new ModelMapper().map(listAlbum, new TypeToken<Set<AlbumDTO>>() {}.getType());

        var res = albumListToDao(listAlbum);

        return ResponseEntity.ok(res);
    }

    @GetMapping({"/getAlbumWithBraniById/{id}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<AlbumDTO> getAlbumWithBraniById(@PathVariable(value = "id") int id) {

        Album album = serviceAlbum.getAlbumWithBraniById(id);
        if (album == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");
        var res = new ModelMapper().map(album, AlbumDTO.class);
        return ResponseEntity.ok(res);
    }

    private Set<AlbumDTO> albumListToDao(Set<Album> listAlbum) {
        //non posso usare il modelmapper per le liste, trascurerebbe la lista di generi

        Set<AlbumDTO> res = new HashSet<>();

        //Trasformo in DTO ciascun album
        for (Album album : listAlbum) {

            AlbumDTO albumDTO = new ModelMapper().map(album, AlbumDTO.class);
            Set<BraniDTO> braniDTOlist = new HashSet<>();

            //Trasformo in DTO ciascun brano di ogni album
            for (Brani brano : album.getBraniList()) {

                BraniDTO branoDTO = new ModelMapper().map(brano, BraniDTO.class);
                Set<GeneriDTO> generiDTO = new HashSet<>();

                //Trasformo in DTO ciascun genere di ogni brano di ogni album
                for (BraniGeneri braniGeneri : brano.getBraniGeneri()) {
                    generiDTO.add(new ModelMapper().map(braniGeneri.getGenere(), GeneriDTO.class));
                }

                branoDTO.setListGeneri(generiDTO);
                braniDTOlist.add(branoDTO);

            }
            albumDTO.setBraniList(braniDTOlist);
            res.add(albumDTO);
        }
        return res;
    }
}
