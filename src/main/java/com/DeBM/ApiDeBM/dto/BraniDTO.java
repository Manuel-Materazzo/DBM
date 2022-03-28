package com.DeBM.ApiDeBM.dto;

import lombok.*;
import org.springframework.lang.Nullable;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class BraniDTO {

    private int idBrano;
    private String titolo;
    private int durata;
    private String url;
    private Boolean approvato;
    private int albumID;
    @Nullable Set<GeneriDTO> listGeneri;
}
