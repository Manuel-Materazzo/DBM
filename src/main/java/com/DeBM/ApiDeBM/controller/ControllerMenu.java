package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.dto.MenuDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
public class ControllerMenu {

    @GetMapping("/getmenu")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MenuDTO>> getMenu(HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<MenuDTO> menu = new ArrayList<>();

        menu.add(MenuDTO.MenuItem.builder()
                .title("Home")
                .icon("home-outline")
                .link("/pages/dashboard")
                .build());

        menu.add(MenuDTO.MenuSpacer.builder()
                .title("AREA ARTISTA")
                .group(true)
                .build());

        menu.add(MenuDTO.MenuItem.builder()
                .title("Il mio Profilo")
                .icon("person")
                .link("/pages/profile")
                .build());
        menu.add(MenuDTO.MenuItem.builder()
                .title("La mia discografia")
                .icon("music-outline")
                .link("/pages/mydiscography")
                .build());
        menu.add(MenuDTO.MenuItem.builder()
                .title("Nuovo Album")
                .icon("plus-circle")
                .link("/pages/newalbum")
                .build());

        //se admin, aggiungo l'area amministrativa
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {

            menu.add(MenuDTO.MenuSpacer.builder()
                    .title("AREA AMMINISTRATORE")
                    .group(true)
                    .build());

            menu.add(MenuDTO.MenuItem.builder()
                    .title("Amministra Artisti")
                    .icon("person")
                    .link("/pages/artistadministration")
                    .build());
            menu.add(MenuDTO.MenuItem.builder()
                    .title("Amministra Discografia")
                    .icon("music-outline")
                    .link("/pages/discographyadministration")
                    .build());
            menu.add(MenuDTO.MenuItem.builder()
                    .title("Approva Brani")
                    .icon("music-outline")
                    .link("/pages/approvesongs")
                    .build());
        }

        return ResponseEntity.ok(menu);
    }

}
