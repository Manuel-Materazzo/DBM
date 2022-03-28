package com.DeBM.ApiDeBM.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Brani {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBrano;

    private String titolo;

    private int durata;

    //@Column(length = 4096)
    private String url;

    private Boolean approvato;


    //------------- RELAZIONI ------------------- N-1 Brani-Album + N-N Brani-Generi
    @ManyToOne(targetEntity = Album.class, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "Album")
    @JsonBackReference
    private Album album;

    @ManyToMany(mappedBy = "listBrani" , fetch = FetchType.EAGER)
    Set<Generi> listGeneri = new HashSet<>();
}
