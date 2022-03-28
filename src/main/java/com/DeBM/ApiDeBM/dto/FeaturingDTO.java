package com.DeBM.ApiDeBM.dto;

import lombok.*;
import org.springframework.lang.Nullable;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class FeaturingDTO {

    private int idFeaturing;
    private Date inizio;
    private Date scadenza;
    @Nullable ArtistiDTO artisti;
}
