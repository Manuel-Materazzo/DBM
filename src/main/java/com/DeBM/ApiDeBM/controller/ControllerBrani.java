package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Brani;
import com.DeBM.ApiDeBM.domain.BraniGeneri;
import com.DeBM.ApiDeBM.domain.Generi;
import com.DeBM.ApiDeBM.dto.BraniDTO;
import com.DeBM.ApiDeBM.dto.GeneriDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.service.impl.ServiceAlbum;
import com.DeBM.ApiDeBM.service.impl.ServiceBrani;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private ServiceBrani serviceBrani;
    @Autowired
    private ServiceAlbum serviceAlbum;

    @PostMapping("/addBrano")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity inserisciBrano(@RequestBody BraniDTO dto, HttpServletRequest request) {

        Brani brano = serviceBrani.inserisciBrano(dto);

        if(brano == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile creare il brano");

        return ResponseEntity.ok().build();
    }

    @PatchMapping({"/updateBrano"}) //Patch per UPDATE
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity modificaBrano(@RequestBody BraniDTO dto, HttpServletRequest request) {

        Brani brano = serviceBrani.modificaBrano(dto);

        if(brano == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile modificare il brano");

        return ResponseEntity.ok().build();
    }

    @PatchMapping({"/approvaBrano"}) //Patch per UPDATE
    @Secured("ROLE_Admin")
    public ResponseEntity approvaBrano(@RequestBody BraniDTO branoDto, HttpServletRequest request) {

        Brani brani = serviceBrani.approvaBrano(branoDto);

        Boolean isAlbumApprovato = null;

        //controllo se tutti i brani di un album son stati approvati
        for(Brani brano : brani.getAlbum().getBraniList()){
            if(isAlbumApprovato == null)
                isAlbumApprovato = brano.getApprovato();
            isAlbumApprovato = isAlbumApprovato && brano.getApprovato();
        }

        //se si, approvo l'album automaticamente
        if(Boolean.TRUE.equals(isAlbumApprovato)){
            serviceAlbum.approvaAlbum(brani.getAlbum());
        }

        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/deleteBranoByTitolo/{titolo}"})
    @Transactional
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity deleteBrano(@PathVariable(value = "titolo", required = false) String titolo,HttpServletRequest request) {
        boolean risposta = serviceBrani.eliminaBrano(titolo);
        if (!risposta)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile eliminare il brano");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/deleteBranoById/{id}"})
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity deleteBranoById(@PathVariable(value = "id", required = false) int id,HttpServletRequest request) {

        boolean risposta = serviceBrani.eliminaBranoById(id);

        if (!risposta)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile eliminare il brano");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAllBrani")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<BraniDTO>> getAllBrani(HttpServletRequest request) {

        Set<Brani> listBrani = serviceBrani.getAllBrani();

        if (listBrani == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");

        Set<BraniDTO> listBraniDto = new ModelMapper().map(listBrani, new TypeToken<Set<BraniDTO>>(){}.getType());

        return ResponseEntity.ok(listBraniDto);
    }

    @GetMapping({"/getBranoById/{id}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<BraniDTO> getBranoById(@PathVariable(value = "id") int id) {

        Brani brani = serviceBrani.getBranoById(id);

        if (brani == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Brano non trovato");

        BraniDTO dto = new ModelMapper().map(brani,BraniDTO.class);

        return ResponseEntity.ok(dto);
    }

    @GetMapping({"/getBraniByIdAlbum/{id}"})
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<BraniDTO>> getBrano(@PathVariable(value = "id") int id,HttpServletRequest request) {

        Set<Brani> daoSet = serviceBrani.getBraniByIdAlbum(id);

        //rispondo con lista vuota se l'album esiste ma non ha brani
        if(daoSet == null)
            return ResponseEntity.ok(new HashSet<>());

        Set<BraniDTO> res = new HashSet<>();

        for(Brani brano: daoSet){
            BraniDTO dto = new ModelMapper().map(brano,BraniDTO.class);
            Set<GeneriDTO> generiDTO = new HashSet<>();
            for(BraniGeneri braniGeneri: brano.getBraniGeneri()){
                generiDTO.add(new ModelMapper().map(braniGeneri.getGenere(),GeneriDTO.class));
            }
            dto.setListGeneri(generiDTO);
            res.add(dto);
        }

        return ResponseEntity.ok(res);
    }

    @GetMapping({"/getBraniByApprovato/{approvato}"})
    @Secured("ROLE_Admin")
    public ResponseEntity<Set<BraniDTO>> getBraniByApprovato(@PathVariable(value = "approvato", required = false) boolean approvato,HttpServletRequest request) {

        Set<Brani> listBrani = serviceBrani.getBraniByApprovato(approvato);

        if (listBrani == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");

        Set<BraniDTO> listBraniDto = new ModelMapper().map(listBrani, new TypeToken<Set<BraniDTO>>(){}.getType());

        return ResponseEntity.ok(listBraniDto);

    }
}
