package com.DeBM.ApiDeBM.dto;

import lombok.*;
import java.util.Set;


@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class SearchDTO {

    Set<ArtistiDTO> artistiList;
    Set<AlbumDTO> albumList;
    Set<BraniDTO> braniList;

}
