package com.DeBM.ApiDeBM.domain;

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

    //@Column(length = 4096)
    private String urlAvatar;

    private String bio;

    private String email;


    //------------- RELAZIONI ------------------- N-1 Artisti-Featuring + N-N Artisti-Album
    @OneToMany(mappedBy = "artisti", //pippo
            fetch = FetchType.EAGER, //EAGER = prende tutti
            cascade = CascadeType.ALL) //cascade=ripercursioni padre/figlio
    @JsonManagedReference
    private Set<Featuring> featuringList = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "Artisti_Album",
            joinColumns = @JoinColumn(name = "artistiId"),
            inverseJoinColumns = @JoinColumn(name = "albumId"))
    Set<Album> listAlbum = new HashSet<>();
}
