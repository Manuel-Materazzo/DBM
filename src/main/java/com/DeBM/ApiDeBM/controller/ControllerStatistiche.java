package com.DeBM.ApiDeBM.controller;

import com.DeBM.ApiDeBM.dto.StatisticheDTO;
import com.DeBM.ApiDeBM.service.impl.ServiceStatistiche;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return serviceStatistiche.getStatistiche();
    }
}
