package com.DeBM.ApiDeBM.domain;

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

    //@Column(length = 4096)
    private String urlCopertina;

    private Boolean approvato;

    private int discoOro;

    private int discoPlatino;


    //------------- RELAZIONI ------------------- 1-N Album-Brani + N-N Album-Artisti
    @OneToMany(mappedBy = "album",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Brani> braniList = new HashSet<>();

    @ManyToMany(mappedBy = "listAlbum", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    Set<Artisti> listArtisti = new HashSet<>();
}
