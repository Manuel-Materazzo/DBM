package com.DeBM.ApiDeBM.mockup;

import com.DeBM.ApiDeBM.controller.ControllerGeneri;
import com.DeBM.ApiDeBM.domain.*;
import com.DeBM.ApiDeBM.dto.*;
import com.DeBM.ApiDeBM.repository.*;
import com.DeBM.ApiDeBM.service.impl.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
public class Mockup implements CommandLineRunner {

    @Autowired
    ServiceGeneri serviceGeneri;
    @Autowired
    ServiceArtisti serviceArtisti;
    @Autowired
    ServiceAlbum serviceAlbum;
    @Autowired
    ServiceBrani serviceBrani;
    @Autowired
    ServiceFeaturing serviceFeaturing;

    @Override
    public void run(String... args) throws Exception {

        //----------------------generi----------------------
        serviceGeneri.inserisciGenere(
                GeneriDTO.builder()
                        .tipologia("metal")
                        .descrizione("metal")
                        .build()
        );
        serviceGeneri.inserisciGenere(
                GeneriDTO.builder()
                        .tipologia("rock")
                        .descrizione("rock")
                        .build()
        );
        serviceGeneri.inserisciGenere(
                GeneriDTO.builder()
                        .tipologia("pop")
                        .descrizione("pop")
                        .build()
        );

        //preparo una lista di generi da assegnare alle canzoni
        Set<GeneriDTO> generiSet = new HashSet<>();
        GeneriDTO genere = GeneriDTO.builder()
                .idGenere(1)
                .build();
        generiSet.add(genere);


        //----------------------artisti----------------------
        serviceArtisti.inserisciArtista(
                ArtistiDTO.builder()
                        .nomeArte("DBM")
                        .urlAvatar("https://cdn.vegaooparty.it/images/rep_art/gra/232/9/232918/disco-vinile-gigante.jpg")
                        .email("admin@dbm.it")
                        .bio("admin della casa discografica")
                        .build()
        );

        Artisti ironMaiden = serviceArtisti.inserisciArtista(
                ArtistiDTO.builder()
                        .nomeArte("Iron Maiden")
                        .urlAvatar("https://m.media-amazon.com/images/I/61obX0851zL._AC_SX450_.jpg")
                        .email("artista1@dbm.it")
                        .bio("Ciao, sono nuovo su DBM")
                        .build()
        );

        //preparo i set dell'artista in vista della creazione dell'album
        Set<ArtistiDTO> ironmaidenSet = new HashSet();
        ArtistiDTO ironmaidenDto = new ModelMapper().map(ironMaiden, ArtistiDTO.class);
        ironmaidenSet.add(ironmaidenDto);


        Artisti gyze = serviceArtisti.inserisciArtista(
                ArtistiDTO.builder()
                        .nomeArte("Gyze")
                        .urlAvatar("https://loudandproud.it/site/wp-content/uploads/2021/01/Gyze-samurai-metal-1-1024x1024.jpg")
                        .email("artista2@dbm.it")
                        .bio("Ciao, sono nuovo su DBM")
                        .build()
        );

        //preparo i set dell'artista in vista della creazione dell'album
        Set<ArtistiDTO> gyzeSet = new HashSet();
        ArtistiDTO gyzeDTO = new ModelMapper().map(gyze, ArtistiDTO.class);
        gyzeSet.add(gyzeDTO);



        Artisti suidakra = serviceArtisti.inserisciArtista(
                ArtistiDTO.builder()
                        .nomeArte("SuidAkrA")
                        .urlAvatar("https://metalitalia.com/wp-content/uploads/2021/06/Suidakra_Wolfbite.jpg")
                        .email("artista3@dbm.it")
                        .bio("Ciao, sono nuovo su DBM")
                        .build()
        );

        //preparo i set dell'artista in vista della creazione dell'album
        Set<ArtistiDTO> suidakraSet = new HashSet();
        ArtistiDTO suidakraDTO = new ModelMapper().map(suidakra, ArtistiDTO.class);
        suidakraSet.add(suidakraDTO);



        Artisti eluveitie = serviceArtisti.inserisciArtista(
                ArtistiDTO.builder()
                        .nomeArte("Eluveitie")
                        .urlAvatar("https://www.season-of-mist.com/wp-content/uploads/2020/03/Eluvetie.jpg")
                        .email("artista4@dbm.it")
                        .bio("Ciao, sono nuovo su DBM")
                        .build()
        );

        //preparo i set dell'artista in vista della creazione dell'album
        Set<ArtistiDTO> eluveitieSet = new HashSet();
        ArtistiDTO eluveitieDTO = new ModelMapper().map(eluveitie, ArtistiDTO.class);
        eluveitieSet.add(eluveitieDTO);



        //----------------------brani----------------------
        //li preparo per aggiungerli direttamente agli album

        //killer, iron maiden
        Set<BraniDTO> killersSongs = new HashSet<>();
        BraniDTO wrathchild = BraniDTO.builder()
                .durata(200)
                .titolo("Wrathchild")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .listGeneri(generiSet)
                .build();
        killersSongs.add(wrathchild);

        BraniDTO drifters = BraniDTO.builder()
                .durata(200)
                .titolo("Drifters")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3")
                .listGeneri(generiSet)
                .build();
        killersSongs.add(drifters);

        BraniDTO purgatory = BraniDTO.builder()
                .durata(200)
                .titolo("Purgatory")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3")
                .listGeneri(generiSet)
                .build();
        killersSongs.add(purgatory);


        //powerslave, iron maiden
        Set<BraniDTO> powerslaveSongs = new HashSet<>();
        BraniDTO aceshigh = BraniDTO.builder()
                .durata(200)
                .titolo("Aces High")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3")
                .listGeneri(generiSet)
                .build();
        powerslaveSongs.add(aceshigh);

        BraniDTO theduellists = BraniDTO.builder()
                .durata(200)
                .titolo("The Duellists")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3")
                .listGeneri(generiSet)
                .build();
        powerslaveSongs.add(theduellists);

        //senjutsu, iron maiden
        Set<BraniDTO> senjutsuSongs = new HashSet<>();
        BraniDTO stratego = BraniDTO.builder()
                .durata(200)
                .titolo("Stratego")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3")
                .listGeneri(generiSet)
                .build();
        senjutsuSongs.add(stratego);

        BraniDTO theparchment = BraniDTO.builder()
                .durata(200)
                .titolo("The Parchment")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3")
                .listGeneri(generiSet)
                .build();
        senjutsuSongs.add(theparchment);

        //Brave New World, iron maiden
        Set<BraniDTO> bravenewworldSongs = new HashSet<>();
        BraniDTO thefallenangel = BraniDTO.builder()
                .durata(200)
                .titolo("The Fallen Angel")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3")
                .listGeneri(generiSet)
                .build();
        bravenewworldSongs.add(thefallenangel);

        BraniDTO thenomad = BraniDTO.builder()
                .durata(200)
                .titolo("The Nomad")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3")
                .listGeneri(generiSet)
                .build();
        bravenewworldSongs.add(thenomad);

        //Dance of Death, iron maiden
        Set<BraniDTO> danceofdeathSongs = new HashSet<>();
        BraniDTO montsegur = BraniDTO.builder()
                .durata(200)
                .titolo("Montségur ")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3")
                .listGeneri(generiSet)
                .build();
        danceofdeathSongs.add(montsegur);

        BraniDTO newfrontier = BraniDTO.builder()
                .durata(200)
                .titolo("New Frontier")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-11.mp3")
                .listGeneri(generiSet)
                .build();
        danceofdeathSongs.add(newfrontier);


        //black bride, gyze
        Set<BraniDTO> blackbrideSongs = new HashSet<>();
        BraniDTO ingrief = BraniDTO.builder()
                .durata(200)
                .titolo("In Grief")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-12.mp3")
                .listGeneri(generiSet)
                .build();
        blackbrideSongs.add(ingrief);

        BraniDTO twilight = BraniDTO.builder()
                .durata(200)
                .titolo("Twilight")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-13.mp3")
                .listGeneri(generiSet)
                .build();
        blackbrideSongs.add(twilight);

        //caledonia, suidakra
        Set<BraniDTO> caledoniaSongs = new HashSet<>();
        BraniDTO ramble = BraniDTO.builder()
                .durata(200)
                .titolo("Ramble")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-14.mp3")
                .listGeneri(generiSet)
                .build();
        caledoniaSongs.add(ramble);

        //helvetios, eluveitie
        Set<BraniDTO> helvetiosSongs = new HashSet<>();
        BraniDTO luxtos = BraniDTO.builder()
                .durata(200)
                .titolo("Luxtos")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-15.mp3")
                .listGeneri(generiSet)
                .build();
        helvetiosSongs.add(luxtos);


        //----------------------album----------------------
        serviceAlbum.inserisciAlbum(
                AlbumDTO.builder()
                        .casaDiscografica("DBM")
                        .descrizione("testalbum")
                        .nome("killers")
                        .dataPubblicazione(new Date())
                        .urlCopertina("https://www.ibs.it/images/0190295567750_0_536_0_75.jpg")
                        .listArtisti(ironmaidenSet)
                        .braniList(killersSongs)
                        .build()
        );

        serviceAlbum.inserisciAlbum(
                AlbumDTO.builder()
                        .casaDiscografica("DBM")
                        .descrizione("testalbum")
                        .nome("Powerslaves")
                        .dataPubblicazione(new Date())
                        .urlCopertina("https://upload.wikimedia.org/wikipedia/en/1/1c/Iron_Maiden_-_Powerslave.jpg")
                        .listArtisti(ironmaidenSet)
                        .braniList(powerslaveSongs)
                        .build()
        );

        serviceAlbum.inserisciAlbum(
                AlbumDTO.builder()
                        .casaDiscografica("DBM")
                        .descrizione("testalbum")
                        .nome("Senjutsu")
                        .dataPubblicazione(new Date())
                        .urlCopertina("https://metalitalia.com/wp-content/uploads/2021/07/iron-maiden-Senjutsu-2021.jpg")
                        .listArtisti(ironmaidenSet)
                        .braniList(senjutsuSongs)
                        .build()
        );

        serviceAlbum.inserisciAlbum(
                AlbumDTO.builder()
                        .casaDiscografica("DBM")
                        .descrizione("testalbum")
                        .nome("Brave New World")
                        .dataPubblicazione(new Date())
                        .urlCopertina("https://i2.wp.com/fotografierock.it/wp-content/uploads/2019/12/FB_IMG_1576144221963-1.jpg")
                        .listArtisti(ironmaidenSet)
                        .braniList(bravenewworldSongs)
                        .build()
        );

        serviceAlbum.inserisciAlbum(
                AlbumDTO.builder()
                        .casaDiscografica("DBM")
                        .descrizione("testalbum")
                        .nome("Dance of Death")
                        .dataPubblicazione(new Date())
                        .urlCopertina("https://upload.wikimedia.org/wikipedia/en/thumb/c/cb/IronMaiden_DanceOfDeath_OriginalCoverArt.jpeg/220px-IronMaiden_DanceOfDeath_OriginalCoverArt.jpeg")
                        .listArtisti(ironmaidenSet)
                        .braniList(danceofdeathSongs)
                        .build()
        );

        serviceAlbum.inserisciAlbum(
                AlbumDTO.builder()
                        .casaDiscografica("DBM")
                        .descrizione("testalbum")
                        .nome("Black Bride")
                        .dataPubblicazione(new Date())
                        .urlCopertina("https://m.media-amazon.com/images/I/716+tAStqoL._AC_SY450_.jpg")
                        .listArtisti(gyzeSet)
                        .braniList(blackbrideSongs)
                        .build()
        );

        serviceAlbum.inserisciAlbum(
                AlbumDTO.builder()
                        .casaDiscografica("DBM")
                        .descrizione("testalbum")
                        .nome("Caledonia")
                        .dataPubblicazione(new Date())
                        .urlCopertina("https://metalitalia.com/wp-content/imported/cms/images/bands/s/suidakra/suidakra_caledonia.jpg")
                        .listArtisti(suidakraSet)
                        .braniList(caledoniaSongs)
                        .build()
        );

        serviceAlbum.inserisciAlbum(
                AlbumDTO.builder()
                        .casaDiscografica("DBM")
                        .descrizione("testalbum")
                        .nome("Helvetios")
                        .dataPubblicazione(new Date())
                        .urlCopertina("https://m.media-amazon.com/images/I/717jSmC7BmL._SS500_.jpg")
                        .listArtisti(eluveitieSet)
                        .braniList(helvetiosSongs)
                        .build()
        );


        //----------------------featuring----------------------

        //calcolo le date
        LocalDate ieriLocal = LocalDate.now(ZoneId.systemDefault()).minusDays(1);
        Date ieriDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(ieriLocal));
        LocalDate traUnMeseLocal = LocalDate.now(ZoneId.systemDefault()).plusMonths(1);
        Date traUnMeseDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(traUnMeseLocal));

        serviceFeaturing.inserisciFeaturing(
                FeaturingDTO.builder()
                        .inizio(ieriDate)
                        .scadenza(traUnMeseDate)
                        .artisti(
                                ArtistiDTO.builder()
                                        .idArtista(2)
                                        .build()
                        )
                        .build()
        );

    }
}
