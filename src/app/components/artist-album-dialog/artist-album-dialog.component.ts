import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NbDialogRef} from '@nebular/theme';
import {GridSettings} from './album-grid/album-grid.component';
import {FeaturedDialogType} from '../../util/FeaturedDialogType';
import {AlbumData, AlbumDataObject} from '../../util/DTO/SongData';
import {AlbumService} from '../../services/album.service';
import {BraniService} from '../../services/brani.service';
import {ToastService} from '../../services/toast.service';
import {Brano, BranoApproveDTO} from '../../util/DTO/Brano';
import {GeneriUtil} from "../../util/GeneriUtil";

@Component({
  selector: 'ngx-featured-dialog',
  templateUrl: 'artist-album-dialog.component.html',
  styleUrls: ['artist-album-dialog.component.scss'],
})
export class ArtistAlbumDialogComponent implements OnInit {

  @Input() firstTextLine: string;
  @Input() secondTextLine: string;
  @Input() imageUrl: string;
  @Input() id: number;
  @Input() dialogType: FeaturedDialogType;
  @Input() admin: boolean = false;

  @Output() playSongEvent = new EventEmitter<number>();

  loading: boolean = true;
  playing: boolean = false;

  audioList = [];

  audioPlaceholder = [
    {
      url: "https://ia800206.us.archive.org/16/items/SilentRingtone/silence_64kb.mp3",
      title: "\t",
      cover: ""
    }
  ];

  gridSettings: GridSettings;
  dataSet: AlbumData[] = [];

  approvationList: BranoApproveDTO[] = [];

  constructor(protected ref: NbDialogRef<ArtistAlbumDialogComponent>,
              private albumService: AlbumService,
              private braniService: BraniService,
              private toastService: ToastService) {
  }

  ngOnInit() {
    switch (this.dialogType) {

      case FeaturedDialogType.Artist:
        this.artistInit();
        break;

      case FeaturedDialogType.Album:
        this.albumInit();
        break;
    }
  }

  artistInit() {
    this.gridSettings = {
      primaColonna: 'Nome',
      nomeDatiPrimaColonna: 'nome',
      altreColonne: ['Durata', 'Genere', 'Numero Canzoni'],
      nomeDatiAltreColonne: ['durata', 'genere', 'numeroCanzoni'],
    };

    this.albumService.getAlbumsByIdArtist(this.id).subscribe(res => {

      res.forEach(album => {

        let durataAlbum: number = 0;

        let albumData: AlbumData = {
          data: {
            durata: "",
            id: album.idAlbum,
            nome: album.nome,
            numeroCanzoni: 0
          },
          children: []
        };

        album.braniList?.forEach(brano => {
          durataAlbum += brano.durata;

            albumData.children.push({
              data: {
                durata: new Date(brano.durata * 1000).toISOString().substr(11, 8),
                genere: GeneriUtil.generiListToString(brano.listGeneri),
                id: brano.idBrano,
                titolo: brano.titolo
              }
            });

            albumData.data.numeroCanzoni++;
        });

        albumData.data.durata = new Date(durataAlbum * 1000).toISOString().substr(11, 8);

        this.dataSet.push(albumData);

      });
      this.loading = false;
    }, (error) => {
      this.toastService.showGenericFailToast();
    });
  }

  albumInit() {
    this.gridSettings = {
      primaColonna: 'Nome',
      nomeDatiPrimaColonna: 'nome',
      altreColonne: ['Durata', 'Genere'],
      nomeDatiAltreColonne: ['durata', 'genere'],
    };


    this.braniService.getBraniByIdAlbum(this.id).subscribe(res => {

      res.forEach(brano => {
          let data: AlbumData = {
            data: {
              durata: new Date(brano.durata * 1000).toISOString().substr(11, 8),
              genere: GeneriUtil.generiListToString(brano.listGeneri),
              id: brano.idBrano,
              nome: brano.titolo,
              approvato: brano.approvato
            }
          };

          this.dataSet.push(data);
        }
      );
      this.loading = false;
    }, (error) => {
      this.toastService.showGenericFailToast();
    });
  }

  dismiss() {
    this.ref.close();
  }

  onSaveClick() {
    this.approvationList.forEach(brano => {
      this.braniService.approvaBrano({
        idBrano: brano.idBrano,
        durata: 0,
        approvato: true,
        titolo: '',
        url: ''
      }).subscribe(
        res => {},
        error => this.toastService.showGenericFailToast()
      );
    });
    this.toastService.showGenericSuccessToast();
  }

  onApproveChange(event: BranoApproveDTO) {
    //elimino le modifiche fatte alla stessa canzone prima di aggiungerne di nuove alla lista
    this.approvationList = this.approvationList.filter(x => x.idBrano != event.idBrano);
    this.approvationList.push(event);
  }

  playSong(songID) {
    this.playing = false;
    this.braniService.getBranoById(songID).subscribe(brano => {
      this.audioList = [
        {
          url: brano.url,
          title: brano.titolo,
          cover: brano.urlCopertina
        }
      ];
      this.playing = true;
      //emetto l'evento che informa i componenti parent che sta partendo una canzone
      this.playSongEvent.next(songID);
    });
  }

}
