package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Artisti;
import com.DeBM.ApiDeBM.domain.Generi;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import com.DeBM.ApiDeBM.dto.GeneriDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.repository.IRepositoryGeneri;
import com.DeBM.ApiDeBM.service.impl.ServiceGeneri;
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

    @Autowired
    private ServiceGeneri serviceGeneri;
    @Autowired
    private IRepositoryGeneri iRepositoryGeneri;

    @PostMapping("/addGenere")
    @Secured("ROLE_Admin")
    public ResponseEntity inserisciGenere(@RequestBody GeneriDTO dto, HttpServletRequest request) {

        Generi dao = serviceGeneri.inserisciGenere(dto);

        if(dao == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile inserire genere");

        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/deleteGenereById/{id}"})
    @Secured("ROLE_Admin")
    public ResponseEntity deleteGenereById(@PathVariable(value = "id", required = false) int id,HttpServletRequest request) {

        boolean risposta = serviceGeneri.eliminaGenereById(id);

        if (!risposta)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile eliminare genere");

        return ResponseEntity.ok("");
    }

    @GetMapping("/getAllGeneri")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<GeneriDTO>> getAllGeneri(HttpServletRequest request) {

        Set<Generi> listGeneri = serviceGeneri.getAllGeneri();

        Set<GeneriDTO> res = new ModelMapper().map(listGeneri, new TypeToken<Set<GeneriDTO>>(){}.getType());

        return ResponseEntity.ok(res);
    }

}
