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
public class Generi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idGenere;

    private String tipologia;

    private String descrizione;


    //------------- RELAZIONI ------------------- N-N Generi-Brani
    @OneToMany(mappedBy = "genere", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    Set<BraniGeneri> braniGeneri = new HashSet<>();
}
