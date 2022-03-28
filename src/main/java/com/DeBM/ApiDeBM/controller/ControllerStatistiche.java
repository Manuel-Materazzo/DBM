package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.domain.Statistiche;
import com.DeBM.ApiDeBM.dto.StatisticheDTO;
import com.DeBM.ApiDeBM.repository.IRepositoryStatistiche;
import com.DeBM.ApiDeBM.service.impl.ServiceStatistiche;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(
        path = "/statistiche",
        produces = MediaType.APPLICATION_JSON_VALUE
)

public class ControllerStatistiche {

    @Autowired
    private ServiceStatistiche serviceStatistiche;

    @GetMapping("/getStatistiche")
    @PreAuthorize("permitAll()")
    public StatisticheDTO getStatistiche() {

        Statistiche s = serviceStatistiche.getStatistiche();

        return new ModelMapper().map(s,StatisticheDTO.class);
    }
}
