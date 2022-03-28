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
public class ArtistiDTO {

    private int idArtista;
    private String nomeArte;
    private Boolean bannato;
    private String urlAvatar;
    private String bio;
    private String email;
    private boolean admin = false;
    @Nullable Set<AlbumDTO> listAlbum;
    @Nullable @JsonIgnore Set<FeaturingDTO> featuringList;
}
