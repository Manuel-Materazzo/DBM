package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Artisti;
import com.DeBM.ApiDeBM.dto.ArtistiDTO;
import com.DeBM.ApiDeBM.dto.LoginDTO;
import com.DeBM.ApiDeBM.dto.RegisterDTO;
import com.DeBM.ApiDeBM.dto.TokenDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.service.Interface.InterfaceArtisti;
import com.DeBM.ApiDeBM.service.Interface.InterfaceAutenticazione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class ControllerAutenticazione {

    @Autowired
    InterfaceAutenticazione iServiceAutenticazione;
    @Autowired
    InterfaceArtisti iServiceArtisti;


    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO credentials) {


        TokenDTO token = iServiceAutenticazione.getToken(credentials.getEmail(),credentials.getPassword());

        if(token == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Username o password errati");

        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    @PreAuthorize("permitAll()")
    public ResponseEntity<TokenDTO> register(@RequestBody RegisterDTO credentials) {

        if(!credentials.getPassword().contentEquals(credentials.getConfirmPassword()))
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Le password non corrispondono");
        if(!credentials.isTerms())
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "devi accettare i termini e condizioni");

        ArtistiDTO artistaDTO = ArtistiDTO.builder()
                .bannato(false)
                .bio("Hey, sono nuovo su DBM")
                .email(credentials.getEmail())
                .nomeArte(credentials.getNomeArte())
                .urlAvatar("https://clinicforspecialchildren.org/wp-content/uploads/2016/08/avatar-placeholder.gif")
                .build();

        Artisti artista = iServiceArtisti.inserisciArtista(artistaDTO);

        boolean success = iServiceAutenticazione.addUser(
                            credentials.getEmail(), credentials.getPassword(), artista.getIdArtista());

        if(!success)
            throw new GeneralDataException(HttpStatus.INTERNAL_SERVER_ERROR, "Impossibile registrare un nuovo utente");

        TokenDTO token = iServiceAutenticazione.getToken(credentials.getEmail(), credentials.getPassword());

        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    @PreAuthorize("permitAll()")
    public ResponseEntity<TokenDTO> refresh(@RequestBody TokenDTO credentials) {

        TokenDTO token = iServiceAutenticazione.doRefreshToken(credentials.getToken().getRefresh_token());

        return ResponseEntity.ok(token);
    }

}