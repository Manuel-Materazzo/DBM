package com.DeBM.ApiDeBM.mockup;

import com.DeBM.ApiDeBM.domain.*;
import com.DeBM.ApiDeBM.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.HashSet;

@Component
public class Mockup implements CommandLineRunner {

    @Autowired
    IRepositoryArtisti iRepositoryArtisti;
    @Autowired
    IRepositoryBrani iRepositoryBrani;
    @Autowired
    IRepositoryAlbum iRepositoryAlbum;
    @Autowired
    IRepositoryFeaturing iRepositoryFeaturing;
    @Autowired
    IRepositoryGeneri iRepositoryGeneri;
    @Autowired
    IRepositoryStatistiche iRepositoryStatistiche;

    @Override
    public void run(String... args) throws Exception {

        /*iRepositoryStatistiche.save(Statistiche.builder().build());

        //TODO: brano 15 e 16 si spacca nei generi
        //---------------------------------------------------------------------------
        //                              GENERI
        //---------------------------------------------------------------------------

        Generi pop = Generi.builder()
                .tipologia("pop")
                .descrizione("Genere pop")
                .listBrani(new HashSet<>())
                .build();

        Generi rock = Generi.builder()
                .tipologia("rock")
                .descrizione("Genere rock")
                .listBrani(new HashSet<>())
                .build();

        Generi metal = Generi.builder()
                .tipologia("metal")
                .descrizione("Genere metal")
                .listBrani(new HashSet<>())
                .build();


        Artisti dbm = Artisti.builder()
                .nomeArte("DBM")
                .bannato(false)
                .urlAvatar("https://cdn.discordapp.com/attachments/919522472519434291/919533959556263946/logo_trans.png")
                .bio("Account amministrativo di DBM")
                .email("no-reply@dbm.com")
                .listAlbum(new HashSet<>())
                .featuringList(new HashSet<>())
                .build();

        iRepositoryArtisti.save(dbm);

        //---------------------------------------------------------------------------
        //                              MichaelJackson
        //---------------------------------------------------------------------------

        Artisti michaelJackson = Artisti.builder()
                .nomeArte("Michael Jackson")
                .bannato(false)
                .urlAvatar("https://i.gyazo.com/e64780b06c5ba1dfa2aa78f9da7aae80.png")
                .bio("Sono il Re del pop")
                .email("michael.jackson@gmail.com")
                .listAlbum(new HashSet<>())
                .featuringList(new HashSet<>())
                .build();

        //ALBUM
        Album dangerous = Album.builder()
                .nome("dangerous")
                .dataPubblicazione(new SimpleDateFormat("yyyy/MM/dd").parse("1987/10/25"))
                .descrizione("Molto bello")
                .casaDiscografica("Casa Discografica 1")
                .urlCopertina("https://legendarycover.it/wp-content/uploads/2019/01/thriller-michael-jackson-copertina-e1548770138210.jpg")
                .approvato(true)
                .discoOro(100)
                .discoPlatino(45)
                .listArtisti(new HashSet<>())
                .braniList(new HashSet<>())
                .build();


        Album bad = Album.builder()
                .nome("bad")
                .dataPubblicazione(new SimpleDateFormat("yyyy/MM/dd").parse("1999/05/18"))
                .descrizione("Nuovissimo album!")
                .casaDiscografica("Casa Discografica 2")
                .urlCopertina("https://m.media-amazon.com/images/I/6148jblbNqL._AC_SL1500_.jpg")
                .approvato(true)
                .discoOro(80)
                .discoPlatino(40)
                .listArtisti(new HashSet<>())
                .braniList(new HashSet<>())
                .build();

        Album offTheWall = Album.builder()
                .nome("offTheWall")
                .dataPubblicazione(new SimpleDateFormat("yyyy/MM/dd").parse("1979/10/21"))
                .descrizione("Piantato al muro!")
                .casaDiscografica("Casa Discografica IL MURO")
                .urlCopertina("https://i.gyazo.com/aac5fdf5a01c066850c8476e9d0b945c.png")
                .approvato(true)
                .discoOro(80)
                .discoPlatino(60)
                .listArtisti(new HashSet<>())
                .braniList(new HashSet<>())
                .build();

        Album twelves = Album.builder()
                .nome("twelves")
                .dataPubblicazione(new SimpleDateFormat("yyyy/MM/dd").parse("1997/04/18"))
                .descrizione("Sono una descrizione!")
                .casaDiscografica("Casa Discografica tTelves")
                .urlCopertina("https://i.gyazo.com/c5b9ca1f1627efbca27054f05d8d9896.png")
                .approvato(true)
                .discoOro(80)
                .discoPlatino(95)
                .listArtisti(new HashSet<>())
                .braniList(new HashSet<>())
                .build();

        Album clown = Album.builder()
                .nome("clown")
                .dataPubblicazione(new SimpleDateFormat("yyyy/MM/dd").parse("2022/01/01"))
                .descrizione("Sono un album fantoccio")
                .casaDiscografica("Da Pippo e Pluto Spa")
                .urlCopertina("https://i.gyazo.com/045a7ab3470fb2b4ace4abff9983c61d.png")
                .approvato(true)
                .discoOro(1)
                .discoPlatino(1)
                .listArtisti(new HashSet<>())
                .braniList(new HashSet<>())
                .build();

        Album immortal = Album.builder()
                .nome("immortal")
                .dataPubblicazione(new SimpleDateFormat("yyyy/MM/dd").parse("1985/03/27"))
                .descrizione("Album IMMORTALE!")
                .casaDiscografica("Casa Discografica IMMORTALE")
                .urlCopertina("https://i.gyazo.com/66b9cb14df084ef9c29e64846e676e58.png")
                .approvato(true)
                .discoOro(150)
                .discoPlatino(150)
                .listArtisti(new HashSet<>())
                .braniList(new HashSet<>())
                .build();

        //BRANI
        Brani dirtyDiana = Brani.builder()
                .idBrano(1)
                .titolo("Dirty Diana")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(186)
                .approvato(true)
                .listGeneri(new HashSet<>())
                .album(dangerous)
                .build();

        Brani billieJean = Brani.builder()
                .idBrano(2)
                .titolo("Billie Jean")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(195)
                .approvato(true)
                .listGeneri(new HashSet<>())
                .album(dangerous)
                .build();

        Brani beatIt = Brani.builder()
                .idBrano(3)
                .titolo("Beat It")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(210)
                .approvato(true)
                .listGeneri(new HashSet<>())
                .album(bad)
                .build();

        Brani blackOrWhite = Brani.builder()
                .idBrano(4)
                .titolo("Black Or White")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(191)
                .approvato(true)
                .listGeneri(new HashSet<>())
                .album(bad)
                .build();

        //GET
        pop.getListBrani().add(dirtyDiana);
        pop.getListBrani().add(billieJean);
        pop.getListBrani().add(beatIt);
        pop.getListBrani().add(blackOrWhite);

        michaelJackson.getListAlbum().add(offTheWall);
        michaelJackson.getListAlbum().add(twelves);
        michaelJackson.getListAlbum().add(clown);
        michaelJackson.getListAlbum().add(immortal);
        michaelJackson.getListAlbum().add(dangerous);
        michaelJackson.getListAlbum().add(bad);

        //SAVE
        iRepositoryAlbum.save(dangerous);
        iRepositoryAlbum.save(bad);
        iRepositoryAlbum.save(immortal);
        iRepositoryAlbum.save(clown);
        iRepositoryAlbum.save(twelves);
        iRepositoryAlbum.save(offTheWall);

        iRepositoryArtisti.save(michaelJackson);

        iRepositoryBrani.save(dirtyDiana);
        iRepositoryBrani.save(billieJean);
        iRepositoryBrani.save(beatIt);
        iRepositoryBrani.save(blackOrWhite);

        //---------------------------------------------------------------------------
        //                                  Queen
        //---------------------------------------------------------------------------

        Artisti queen = Artisti.builder()
                .nomeArte("Queen")
                .bannato(false)
                .urlAvatar("https://i.gyazo.com/5e4c3853d094aa145c38bdfd123240a4.png")
                .bio("Siamo unici, i migliori!")
                .email("queen.rock@gmail.com")
                .listAlbum(new HashSet<>())
                .featuringList(new HashSet<>())
                .build();

        //ALBUM
        Album nightAtTheOpera = Album.builder()
                .nome("Night At The Opera")
                .dataPubblicazione(new SimpleDateFormat("yyyy/MM/dd").parse("1986/06/01"))
                .descrizione("Qualcosa di mai visto!")
                .casaDiscografica("Casa discografica fratelli Marx")
                .urlCopertina("https://i.gyazo.com/3da6d5dec151fc428dbee72e8707b580.png")
                .approvato(true)
                .discoOro(250)
                .discoPlatino(100)
                .listArtisti(new HashSet<>())
                .braniList(new HashSet<>())
                .build();

        Album greatestHits = Album.builder()
                .nome("Greatest Hits")
                .dataPubblicazione(new SimpleDateFormat("yyyy/MM/dd").parse("1995/03/17"))
                .descrizione("The Best")
                .casaDiscografica("La miglior casa disgcografica")
                .urlCopertina("https://i.gyazo.com/1efaabb8205b3f7bf54a5c15f9c8c4a0.png")
                .approvato(true)
                .discoOro(95)
                .discoPlatino(38)
                .listArtisti(new HashSet<>())
                .braniList(new HashSet<>())
                .build();

        //BRANI
        Brani bohemianRhapsody = Brani.builder()
                .idBrano(5)
                .titolo("Bohemian Rhapsody")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(230)
                .approvato(true)
                .listGeneri(new HashSet<>())
                .album(nightAtTheOpera)
                .build();

        Brani somebodyToLove = Brani.builder()
                .idBrano(6)
                .titolo("Somebody To Love")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(195)
                .approvato(true)
                .listGeneri(new HashSet<>())
                .album(nightAtTheOpera)
                .build();

        Brani loveOfMyLife = Brani.builder()
                .idBrano(7)
                .titolo("Love Of My Life")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(125)
                .approvato(true)
                .listGeneri(new HashSet<>())
                .album(greatestHits)
                .build();

        Brani radioGaGa = Brani.builder()
                .idBrano(8)
                .titolo("Radio Ga Ga")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(136)
                .approvato(true)
                .listGeneri(new HashSet<>())
                .album(greatestHits)
                .build();

        //GET
        metal.getListBrani().add(bohemianRhapsody);
        metal.getListBrani().add(somebodyToLove);
        metal.getListBrani().add(loveOfMyLife);
        metal.getListBrani().add(radioGaGa);

        queen.getListAlbum().add(greatestHits);
        queen.getListAlbum().add(nightAtTheOpera);

        //SAVE
        iRepositoryAlbum.save(nightAtTheOpera);
        iRepositoryAlbum.save(greatestHits);

        iRepositoryArtisti.save(queen);

        iRepositoryBrani.save(bohemianRhapsody);
        iRepositoryBrani.save(somebodyToLove);
        iRepositoryBrani.save(loveOfMyLife);
        iRepositoryBrani.save(radioGaGa);

        //---------------------------------------------------------------------------
        //                               TizianoFerro
        //---------------------------------------------------------------------------
        Artisti tizianoFerro = Artisti.builder()
                .nomeArte("Tiziano Ferro")
                .bannato(false)
                .urlAvatar("https://i.gyazo.com/a945c7c7b984f601805eec025255bf6c.png")
                .bio("Sono molto deprimenti HAHAHA")
                .email("tiziano.ferro@gmail.com")
                .listAlbum(new HashSet<>())
                .featuringList(new HashSet<>())
                .build();

        //ALBUM
        Album theBestOf = Album.builder()
                .nome("The Best Of")
                .dataPubblicazione(new SimpleDateFormat("yyyy/MM/dd").parse("2005/09/18"))
                .descrizione("Qui troverai le mie migliori e famose canzoni!")
                .casaDiscografica("Marvel")
                .urlCopertina("https://i.gyazo.com/21d91d51a81fd91343145814e8419706.png")
                .approvato(true)
                .discoOro(65)
                .discoPlatino(21)
                .listArtisti(new HashSet<>())
                .braniList(new HashSet<>())
                .build();

        Album accettoMiracoli = Album.builder()
                .nome("Accetto miracoli")
                .dataPubblicazione(new SimpleDateFormat("yyyy/MM/dd").parse("2019/03/20"))
                .descrizione("Ll settimo album in studio")
                .casaDiscografica("TF")
                .urlCopertina("https://i.gyazo.com/f710717c1c8b86f8dea57cf33e8f8143.png")
                .approvato(true)
                .discoOro(25)
                .discoPlatino(13)
                .listArtisti(new HashSet<>())
                .braniList(new HashSet<>())
                .build();

        //BRANI
        Brani indietro = Brani.builder()
                .idBrano(9)
                .titolo("Indietro")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(187)
                .approvato(true)
                .listGeneri(new HashSet<>())
                .album(theBestOf)
                .build();

        Brani sereNere = Brani.builder()
                .idBrano(10)
                .titolo("Sere Nere")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(199)
                .approvato(true)
                .listGeneri(new HashSet<>())
                .album(theBestOf)
                .build();

        Brani perdona = Brani.builder()
                .idBrano(13)
                .titolo("Perdona")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(214)
                .approvato(false)
                .listGeneri(new HashSet<>())
                .album(accettoMiracoli)
                .build();

        Brani troppoBuono = Brani.builder()
                .idBrano(14)
                .titolo("Troppo buono")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(223)
                .approvato(false)
                .listGeneri(new HashSet<>())
                .album(accettoMiracoli)
                .build();

        //GET
        pop.getListBrani().add(indietro);
        pop.getListBrani().add(sereNere);
        pop.getListBrani().add(perdona);
        pop.getListBrani().add(troppoBuono);

        tizianoFerro.getListAlbum().add(theBestOf);
        tizianoFerro.getListAlbum().add(accettoMiracoli);

        //SAVE
        iRepositoryAlbum.save(accettoMiracoli);
        iRepositoryAlbum.save(theBestOf);

        iRepositoryArtisti.save(tizianoFerro);

        iRepositoryBrani.save(indietro);
        iRepositoryBrani.save(sereNere);
        iRepositoryBrani.save(perdona);
        iRepositoryBrani.save(troppoBuono);

        //---------------------------------------------------------------------------
        //                               RollingStones
        //---------------------------------------------------------------------------
        Artisti rollingStones = Artisti.builder()
                .nomeArte("Rolling Stones")
                .bannato(false)
                .urlAvatar("https://i.gyazo.com/be9b596114002221f8e41fcbfad6547a.png")
                .bio("Eravamo i rivali dei Beatles")
                .email("rolling.stones@gmail.com")
                .listAlbum(new HashSet<>())
                .featuringList(new HashSet<>())
                .build();

        //ALBUM
        Album blackAndBlue = Album.builder()
                .nome("Black And Blue")
                .dataPubblicazione(new SimpleDateFormat("yyyy/MM/dd").parse("1986/11/03"))
                .descrizione("W O W")
                .casaDiscografica("Quella non scelta dai Beatles")
                .urlCopertina("https://i.gyazo.com/d44e8f7f57e424c15faf6a83df48a478.jpg")
                .approvato(true)
                .discoOro(80)
                .discoPlatino(36)
                .listArtisti(new HashSet<>())
                .braniList(new HashSet<>())
                .build();

        Album tattooYou = Album.builder()
                .nome("Tattoo You")
                .dataPubblicazione(new SimpleDateFormat("yyyy/MM/dd").parse("1981/05/24"))
                .descrizione("Tattoo You è un album discografico del gruppo rock britannico The Rolling Stones")
                .casaDiscografica("Quella non scelta dai Queen")
                .urlCopertina("https://i.gyazo.com/a29ca4ebf1a92d5e48e44c956d51f0e0.png")
                .approvato(false)
                .discoOro(58)
                .discoPlatino(21)
                .listArtisti(new HashSet<>())
                .braniList(new HashSet<>())
                .build();

        //BRANI
        Brani satisfaction = Brani.builder()
                .idBrano(11)
                .titolo("Satisfaction")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(168)
                .approvato(true)
                .listGeneri(new HashSet<>())
                .album(blackAndBlue)
                .build();

        Brani startMeUp = Brani.builder()
                .idBrano(12)
                .titolo("Start Me Up")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(400)
                .approvato(true)
                .listGeneri(new HashSet<>())
                .album(blackAndBlue)
                .build();

        Brani missYou = Brani.builder()
                .idBrano(15)
                .titolo("Miss you")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(295)
                .approvato(false)
                .listGeneri(new HashSet<>())
                .album(tattooYou)
                .build();

        Brani angie = Brani.builder()
                .idBrano(16)
                .titolo("Angie")
                .url("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                .durata(256)
                .approvato(false)
                .listGeneri(new HashSet<>())
                .album(tattooYou)
                .build();

        //GET
        rock.getListBrani().add(satisfaction);
        rock.getListBrani().add(startMeUp);
        //pop.getListBrani().add(missYou);
        //pop.getListBrani().add(angie);

        rollingStones.getListAlbum().add(blackAndBlue);
        rollingStones.getListAlbum().add(tattooYou);

        //SAVE
        iRepositoryAlbum.save(blackAndBlue);
        iRepositoryAlbum.save(tattooYou);

        iRepositoryArtisti.save(rollingStones);

        iRepositoryBrani.save(satisfaction);
        iRepositoryBrani.save(startMeUp);
        iRepositoryBrani.save(missYou);
        iRepositoryBrani.save(angie);

        //----------------------- SAVE GENERI -----------------------
        iRepositoryGeneri.save(pop);
        iRepositoryGeneri.save(rock);
        iRepositoryGeneri.save(metal);
        //-----------------------------------------------------------

        //---------------------------------------------------------------------------
        //                               FEATURING
        //---------------------------------------------------------------------------
        Featuring featuring1 = Featuring.builder()
                .inizio(new SimpleDateFormat("yyyy/MM/dd").parse("2020/05/01"))
                .scadenza(new SimpleDateFormat("yyyy/MM/dd").parse("2021/01/20"))
                .artisti(tizianoFerro)
                .build();


        Featuring featuring2 = Featuring.builder()
                .inizio(new SimpleDateFormat("yyyy/MM/dd").parse("2020/03/01"))
                .scadenza(new SimpleDateFormat("yyyy/MM/dd").parse("2021/04/31"))
                .artisti(queen)
                .build();

        Featuring featuring3 = Featuring.builder()
                .inizio(new SimpleDateFormat("yyyy/MM/dd").parse("2020/01/20"))
                .scadenza(new SimpleDateFormat("yyyy/MM/dd").parse("2021/01/03"))
                .artisti(michaelJackson)
                .build();

        Featuring featuring4 = Featuring.builder()
                .inizio(new SimpleDateFormat("yyyy/MM/dd").parse("2020/01/05"))
                .scadenza(new SimpleDateFormat("yyyy/MM/dd").parse("2020/10/20"))
                .artisti(rollingStones)
                .build();

        Featuring featuring5 = Featuring.builder()
                .inizio(new SimpleDateFormat("yyyy/MM/dd").parse("2021/05/01"))
                .scadenza(new SimpleDateFormat("yyyy/MM/dd").parse("2021/09/20"))
                .artisti(tizianoFerro)
                .build();

        Featuring featuring6 = Featuring.builder()
                .inizio(new SimpleDateFormat("yyyy/MM/dd").parse("2021/09/01"))
                .scadenza(new SimpleDateFormat("yyyy/MM/dd").parse("2022/03/20"))
                .artisti(michaelJackson)
                .build();

        //SAVE
        iRepositoryFeaturing.save(featuring1);
        iRepositoryFeaturing.save(featuring2);
        iRepositoryFeaturing.save(featuring3);
        iRepositoryFeaturing.save(featuring4);
        iRepositoryFeaturing.save(featuring5);
        iRepositoryFeaturing.save(featuring6);*/


    }
}
