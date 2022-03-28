package com.DeBM.ApiDeBM.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GeneriDTO {

    private int idGenere;
    private String tipologia;
    private String descrizione;
    @Nullable @JsonIgnore Set<BraniDTO> listBrani;
}
