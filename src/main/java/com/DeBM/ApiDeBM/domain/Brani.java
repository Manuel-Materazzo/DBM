package com.DeBM.ApiDeBM.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Brani {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBrano;

    private String titolo;

    private int durata;

    private String url;

    private Boolean approvato;


    //------------- RELAZIONI ------------------- N-1 Brani-Album + N-N Brani-Generi
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "Album")
    @JsonBackReference
    private Album album;

    @OneToMany(mappedBy = "brano" , fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    Set<BraniGeneri> braniGeneri = new HashSet<>();
}
