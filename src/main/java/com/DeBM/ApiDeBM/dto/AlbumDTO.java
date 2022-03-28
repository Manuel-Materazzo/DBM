package com.DeBM.ApiDeBM.dto;

import lombok.*;
import org.springframework.lang.Nullable;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AlbumDTO {

    private int idAlbum;
    private String nome;
    private Date dataPubblicazione;
    private String descrizione;
    private String casaDiscografica;
    private String urlCopertina;
    private Boolean approvato;
    private int discoOro;
    private int discoPlatino;
    @Nullable private Set<ArtistiDTO> listArtisti;
    @Nullable private Set<BraniDTO> braniList;
}
