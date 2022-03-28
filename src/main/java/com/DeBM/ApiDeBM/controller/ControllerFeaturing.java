package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Featuring;
import com.DeBM.ApiDeBM.dto.ArtistiDTO;
import com.DeBM.ApiDeBM.dto.FeaturingDTO;
import com.DeBM.ApiDeBM.exceptions.GeneralDataException;
import com.DeBM.ApiDeBM.service.impl.ServiceFeaturing;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

    @Autowired
    private ServiceFeaturing serviceFeaturing;

    @PostMapping("/addFeaturing")
    @Secured("ROLE_Admin")
    public ResponseEntity inserisciFeaturing(@RequestBody FeaturingDTO dto, HttpServletRequest request) {

        Featuring dao = serviceFeaturing.inserisciFeaturing(dto);

        if(dao == null)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile creare featuring");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAllFeaturing")
    @Secured("ROLE_Admin")
    public ResponseEntity<Set<FeaturingDTO>> getAllFeaturing(HttpServletRequest request) {

        Set<Featuring> listFeaturing = serviceFeaturing.getallFeaturing();

        if(listFeaturing == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Lista vuota");

        Set<FeaturingDTO> res = new ModelMapper().map(listFeaturing, new TypeToken<Set<FeaturingDTO>>(){}.getType());

        return ResponseEntity.ok(res);
    }

    @PatchMapping("updateFeaturing/")
    @Secured("ROLE_Admin")
    public ResponseEntity modificaFeaturing(@RequestBody FeaturingDTO featuringDTO,HttpServletRequest request) {

        Featuring featuring = serviceFeaturing.modificaFeaturing(featuringDTO);

        if(featuring == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "Featuring non trovato");

        return ResponseEntity.ok().build();
    }

    @DeleteMapping({"/deleteFeaturingById/{id}"})
    @Secured("ROLE_Admin")
    public ResponseEntity deleteFeaturing(@PathVariable(value = "id") int id, HttpServletRequest request) {

        boolean risposta = serviceFeaturing.eliminaFeaturing(id);

        if (!risposta)
            throw new GeneralDataException(HttpStatus.BAD_REQUEST, "Impossibile eliminare il featuring");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getActiveFeaturing")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Set<FeaturingDTO>> getActiveFeaturings(HttpServletRequest request) throws ParseException {

        LocalDate localDate = LocalDate.now(ZoneId.systemDefault()).plusDays(1);
        Date dataOdierna = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(localDate));
        Set<Featuring> listFeaturing = serviceFeaturing.getActiveFeaturings(dataOdierna);

        if(listFeaturing == null)
            throw new GeneralDataException(HttpStatus.NOT_FOUND, "lista vuota");

        //il modelmapper fallisce a convertire gli autori dentro la lista
//        Set<FeaturingDTO> res = new ModelMapper().map(listFeaturing, new TypeToken<Set<FeaturingDTO>>(){}.getType());

        Set<FeaturingDTO> res = new HashSet<>();

        for(Featuring featuring: listFeaturing){
            FeaturingDTO featuringDTO = new ModelMapper().map(featuring,FeaturingDTO.class);
            featuringDTO.setArtisti(new ModelMapper().map(featuring.getArtista(), ArtistiDTO.class));
            res.add(featuringDTO);
        }

        return ResponseEntity.ok(res);

    }

}
