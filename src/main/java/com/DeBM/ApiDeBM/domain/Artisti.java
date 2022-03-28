package com.DeBM.ApiDeBM.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Artisti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idArtista;

    private String nomeArte;

    private Boolean bannato;

    private String urlAvatar;

    private String bio;

    private String email;


    //------------- RELAZIONI ------------------- N-1 Artisti-Featuring + N-N Artisti-Album
    @OneToMany(mappedBy = "artista", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Featuring> featuringList = new HashSet<>();

    @OneToMany(mappedBy = "artista", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    Set<AlbumArtisti> albumArtisti = new HashSet<>();
}
