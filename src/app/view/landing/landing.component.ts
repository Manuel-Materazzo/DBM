import {Component, OnInit} from '@angular/core';
import {ArtistAlbumDialogComponent} from '../../components/artist-album-dialog/artist-album-dialog.component';
import {NbDialogService} from '@nebular/theme';
import {FeaturedDialogType} from '../../util/FeaturedDialogType';
import {Artista} from '../../util/DTO/Artista';
import {Album} from '../../util/DTO/Album';
import {Brano} from '../../util/DTO/Brano';
import {AuthService} from '../../services/auth/auth.service';
import {Featuring} from '../../util/DTO/Featuring';
import {FeaturedService} from '../../services/featured.service';
import {SearchService} from '../../services/search.service';
import {ToastService} from '../../services/toast.service';
import {BraniService} from "../../services/brani.service";
import {coerceStringArray} from "@angular/cdk/coercion";


@Component({
  selector: 'ngx-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['landing.component.scss'],
})
export class LandingComponent {

  constructor(private dialogService: NbDialogService,
              private searchService: SearchService,
              private authService: AuthService,
              private featuredService: FeaturedService,
              private toastService: ToastService,
              private braniService: BraniService) {

    this.authService.isAuthenticated().subscribe(authenticated => {
      this.logged = authenticated;
    });

    //popolo la barra featurings
    this.featuredService.getActiveFeaturings().subscribe(featurings => {
      this.featured = featurings;
    }, error => this.toastService.showGenericConnectionErrorToast());

    //popolo la lista esplora
    //this.loadNext();

  }

  featured: Set<Featuring>;

  exploreListArtisti: Artista[] = [];
  exploreListAlbum: Album[] = [];
  exploreListBrani: Brano[] = [];

  audioList = [];
  audioPlaceholder = [
    {
      url: "https://ia800206.us.archive.org/16/items/SilentRingtone/silence_64kb.mp3",
      title: "\t",
      cover: "https://i.gyazo.com/03663d4a6638db5ba3a24b6c6bfdb04b.png"
    }
  ];

  loading: boolean = false;
  stopLoading: boolean = false;
  logged: boolean = false;
  empty: boolean = false;
  playing: boolean = true;

  pageToLoadNext: number = 0;
  pageSize = 12;

  previousSearch: string = '';
  currentSearch: string = '';


  loadNext() {
    if (this.loading || this.stopLoading) {
      return;
    }
    this.loading = true;


    this.searchService.getSearchResults(this.currentSearch, this.pageToLoadNext).subscribe( searchResult => {

      if(this.pageToLoadNext > 0){
        this.exploreListAlbum.push(...searchResult.albumList);
        this.exploreListArtisti.push(...searchResult.artistiList);
        this.exploreListBrani.push(...searchResult.braniList);
      }
      else{
        this.exploreListAlbum = searchResult.albumList;
        this.exploreListArtisti = searchResult.artistiList;
        this.exploreListBrani = searchResult.braniList;
      }

      //se le liste son vuote, deve apparire il coso del "non ci sono risultati"
      this.empty = this.exploreListAlbum.length == 0
        && this.exploreListArtisti.length == 0
        && this.exploreListBrani.length == 0

      if(this.pageToLoadNext > 0 &&
        (searchResult.albumList == null || searchResult.albumList?.length == 0) &&
        searchResult.braniList == null || searchResult.braniList?.length == 0 &&
        searchResult.artistiList == null || searchResult.artistiList?.length == 0){
        this.stopLoading = true;
      }

      this.loading = false;
      this.pageToLoadNext++;
    }, error => this.toastService.showGenericConnectionErrorToast());

  }

  openArtista(artista: Artista) {
    let dialogRef = this.dialogService.open(ArtistAlbumDialogComponent, {
      context: {
        firstTextLine: artista.nomeArte,
        secondTextLine: artista.bio,
        imageUrl: artista.urlAvatar,
        dialogType: FeaturedDialogType.Artist,
        id: artista.idArtista,
        admin: false
      },
      hasScroll: true
    });
    const sub = dialogRef.componentRef.instance.playSongEvent.subscribe(() => {
      this.pausePlayer()
    });
    dialogRef.onClose.subscribe(() => {
      sub.unsubscribe();
    });
  }

  openAlbum(album: Album) {
    let dialogRef = this.dialogService.open(ArtistAlbumDialogComponent, {
      context: {
        firstTextLine: album.nome,
        secondTextLine: album.descrizione,
        imageUrl: album.urlCopertina,
        dialogType: FeaturedDialogType.Album,
        id: album.idAlbum,
        admin: false
      },
      hasScroll: true
    });
    const sub = dialogRef.componentRef.instance.playSongEvent.subscribe(() => {
      this.pausePlayer()
    });
    dialogRef.onClose.subscribe(() => {
      sub.unsubscribe();
    });
  }

  search(text: string) {

    if (text == this.previousSearch || text.length < 3)
      return;

    this.previousSearch = text;

    //resetto il contatore della pagina della tabella ogni volta che la ricerca cambia
    this.pageToLoadNext = 0;

    this.stopLoading = false;

    //resetto le liste per svuotare la tabella e triggerare il loadnext
    this.exploreListAlbum = [];
    this.exploreListBrani = [];
    this.exploreListArtisti = [];
  }

  resetSearch() {
    if (this.currentSearch.length == 0)
      return;

    this.currentSearch = "";
    this.previousSearch = "";
    this.pageToLoadNext = 0;
    this.stopLoading = false;
    this.loadNext();
  }

  playSong(id: number) {
    this.playing = false;
    this.braniService.getBranoById(id).subscribe(brano => {
      this.audioList = [
        {
          url: brano.url,
          title: brano.titolo,
          cover: brano.urlCopertina
        }
      ];
      this.playing = true;
    });
  }

  pausePlayer(){
    //TODO
    console.log("Beneh!")
  }

}
