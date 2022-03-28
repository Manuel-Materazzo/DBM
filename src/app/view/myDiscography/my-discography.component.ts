import {Component} from '@angular/core';
import {AlbumEditDTO} from "../../util/DTO/AlbumEditDTO";
import {GenreRenderComponent} from "../newAlbum/genreComponent/genre-render.component";
import {GenreEditorComponent} from "../newAlbum/genreComponent/genre-editor.component";
import {FileRenderComponent} from "../newAlbum/fileComponent/file-render.component";
import {FileEditorComponent} from "../newAlbum/fileComponent/file-editor.component";
import {ConfirmDialogComponent} from "../../components/confirm-dialog/confirm-dialog.component";
import {NbDialogService} from "@nebular/theme";
import {Album} from "../../util/DTO/Album";
import {BranoCreateDTO} from "../../util/DTO/BranoCreateDTO";
import {ArtistAlbumDialogComponent} from "../../components/artist-album-dialog/artist-album-dialog.component";
import {FeaturedDialogType} from "../../util/FeaturedDialogType";
import {AlbumService} from '../../services/album.service';
import {ArtistaService} from '../../services/artista.service';
import {BraniService} from '../../services/brani.service';
import {ToastService} from '../../services/toast.service';
import {LocalDataSource} from "ng2-smart-table";
import {GeneriUtil} from "../../util/GeneriUtil";
import {FileService} from "../../services/file.service";
import {error} from "protractor";

@Component({
  selector: 'ngx-my-discography',
  styleUrls: ['./my-discography.component.scss'],
  templateUrl: './my-discography.component.html',
})
export class MyDiscographyComponent {


  albums: Set<Album>;

  loading: boolean = true;

  imageChanged: boolean = false;

  albumInApprovazione: Set<AlbumEditDTO> = new Set<AlbumEditDTO>();

  settings = {
    noDataMessage: 'Nessuna canzone aggiunta alla tabella',
    actions: {
      edit: false,
      add: true,
      delete: true,
      columnTitle: 'Azioni',
    },
    add: {
      addButtonContent: '<i class="nb-plus"></i>',
      createButtonContent: '<i class="nb-checkmark"></i>',
      cancelButtonContent: '<i class="nb-close"></i>',
      confirmCreate: true,
    },
    delete: {
      deleteButtonContent: '<i class="nb-trash"></i>',
      confirmDelete: true,
    },
    columns: {
      titolo: {
        title: 'Titolo Canzone',
        type: 'string',
        filter: false
      },
      generi: {
        title: 'Generi',
        filter: false,
        type: 'custom',
        renderComponent: GenreRenderComponent,
        editor: {
          type: 'custom',
          component: GenreEditorComponent,
        },
      },
      file: {
        title: 'File Audio',
        filter: false,
        type: 'custom',
        editable: false,
        renderComponent: FileRenderComponent,
        editor: {
          type: 'custom',
          component: FileEditorComponent,
        },
      },
    },
  };

  createList: BranoCreateDTO[] = [];
  deleteList: BranoCreateDTO[] = [];

  constructor(private dialogService: NbDialogService,
              private albumService: AlbumService,
              private artistService: ArtistaService,
              private braniService: BraniService,
              private toastService: ToastService,
              private  fileService: FileService) {
    //ottengo i miei migliori album
    this.albumService.getMyTopAlbums().subscribe(
      albums => this.albums = albums,
      error => toastService.showGenericConnectionErrorToast()
    );

    this.reloadAlbumInApprovazione();
  }

  onDeleteConfirm(event, album: AlbumEditDTO): void {

    this.dialogService.open(ConfirmDialogComponent, {
      context: {
        title: 'Conferma eliminazione',
        body: 'Sei sicuro di voler eliminare?'
      },
      hasScroll: true
    }).onClose.subscribe(result => {
      if (!result) {
        event.confirm.reject();
        return;
      }

      //rimuovo dalla lista di creazioni
      let createLength = this.createList.length;
      this.createList = this.createList.filter(brano => {
        return brano.titolo !== event.data.titolo
      });

      //se non ho rimosso nulla dalla lista creazioni, era già su db, quindi aggiungo alla lista delete
      if (createLength === this.createList.length) {
        this.deleteList.push({
          titolo: event.data.titolo,
        });
      }

      event.confirm.resolve();

    });
  }

  onCreateConfirm(event, album: AlbumEditDTO): void {

    //validazione campi vuoti
    if (!(event.newData.titolo.length >= 3 && event.newData.generi.length > 0 && event.newData.file)) {
      event.confirm.reject();
      return;
    }

    event.newData["idAlbum"] = album.idAlbum;

    this.createList.push(event.newData);

    event.confirm.resolve();
  }

  onAlbumClick(album: Album) {

    console.log(this.albumInApprovazione)

    this.dialogService.open(ArtistAlbumDialogComponent, {
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
  }

  reloadAlbumInApprovazione(){

    this.albumInApprovazione = new Set<AlbumEditDTO>();
    this.loading = true;

    //recupero i miei album non ancora approvati
    this.albumService.getMyAlbumsNotApproved().subscribe(
      albums => {

        this.albumInApprovazione = new Set<AlbumEditDTO>(albums);
        //carico le canzoni già presenti nel datasource della tabella
        this.albumInApprovazione.forEach( album =>{

          let datasource = [...album.braniList];
          datasource.forEach(object =>{
            object["file"] = object.titolo.replace(" ", "-") + ".mp3";
            object["generi"] = GeneriUtil.generiListToString(object.listGeneri);
          });

          album.canzoni = new LocalDataSource(datasource);
        });
        this.loading = false;
        },
      error => this.toastService.showGenericConnectionErrorToast()
    );

  }

  onSeeAllClick() {

    //recupero il mio profilo
    this.artistService.getProfilo().subscribe(artista =>

      //apro il popup con i miei dettagli
      this.dialogService.open(ArtistAlbumDialogComponent, {
        context: {
          firstTextLine: artista.nomeArte,
          secondTextLine: artista.bio,
          imageUrl: artista.urlAvatar,
          dialogType: FeaturedDialogType.Artist,
          id: artista.idArtista,
          admin: false
        },
        hasScroll: true
      })

    );

  }

  onSaveClick() {

    this.loading = true;

    //aggiorno l'immagine dell'album se cambiata
    this.albumInApprovazione.forEach(album => {
      if (album.image){

        this.fileService.uploadImage(album.image).subscribe(response =>{

          this.albumService.updateAlbum({idAlbum: album.idAlbum, urlCopertina:response.path}).subscribe(
            res => {
              this.reloadAlbumInApprovazione();
              this.toastService.showGenericSuccessToast();
            },
            error => this.toastService.showGenericFailToast()
          );

        });

      }
    });

    for (const brano of this.createList) {
      this.fileService.uploadAudio(brano.file[0]).subscribe(response =>{
        this.braniService.createBrano({
          durata: response.length,
          listGeneri: brano.generi,
          titolo: brano.titolo,
          url: response.path,
          albumID: brano.idAlbum,
        }).subscribe(
          res => {
            this.reloadAlbumInApprovazione();
            this.toastService.showGenericSuccessToast();
          },
          error => this.toastService.showGenericFailToast()
        );
      });
    }

    this.deleteList.forEach( brano => {
        this.braniService.deleteBranoByNome(brano.titolo).subscribe(
          res => {
            this.reloadAlbumInApprovazione();
            this.toastService.showGenericSuccessToast();
          },
          error =>
            this.toastService.showGenericFailToast()
        );
    });

    this.createList = [];
    this.deleteList = [];
    this.imageChanged = false;

  }

  onAnnullaClick() {
    this.createList = [];
    this.deleteList = [];

    this.reloadAlbumInApprovazione();
  }

  handleImageInput(files: FileList, album: AlbumEditDTO) {
    album.image = files.item(0);

    this.imageChanged = true;

    const reader = new FileReader();
    reader.onload = e => album.urlCopertina = reader.result.toString();

    reader.readAsDataURL(album.image);
  }

}
