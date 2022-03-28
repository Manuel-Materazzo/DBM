package com.DeBM.ApiDeBM.domain;

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
    @ManyToMany
    @JoinTable(
            name = "Brani_Generi",
            joinColumns = @JoinColumn(name = "generiId"),
            inverseJoinColumns = @JoinColumn(name = "braniId"))
    Set<Brani> listBrani = new HashSet<>();
}
