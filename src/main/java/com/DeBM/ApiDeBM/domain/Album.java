package com.DeBM.ApiDeBM.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAlbum;

    private String nome;

    private Date dataPubblicazione;

    private String descrizione;

    private String casaDiscografica;

    private String urlCopertina;

    private Boolean approvato;

    private int discoOro;

    private int discoPlatino;


    //------------- RELAZIONI ------------------- 1-N Album-Brani + N-N Album-Artisti
    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Brani> braniList = new HashSet<>();

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<AlbumArtisti> albumArtisti = new HashSet<>();
}
