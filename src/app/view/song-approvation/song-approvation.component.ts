import {Component} from '@angular/core';
import {ToastService} from '../../services/toast.service';
import {NbDialogService} from '@nebular/theme';
import {Album} from '../../util/DTO/Album';
import {ArtistAlbumDialogComponent} from '../../components/artist-album-dialog/artist-album-dialog.component';
import {FeaturedDialogType} from '../../util/FeaturedDialogType';
import {SearchService} from '../../services/search.service';

@Component({
  selector: 'ngx-song-approvation',
  templateUrl: './song-approvation.component.html',
  styleUrls: ['./song-approvation.component.scss']
})
export class SongApprovationComponent {

  albumList: Album[] = [];
  loading: boolean = false;
  stopLoading: boolean = false;
  pageToLoadNext: number = 0;
  pageSize = 12; // MCM di 2, 3 e 4, per avere pagine sempre piene fino alla fine

  previousSearch: string = '';
  currentSearch: string = '';

  constructor(private toastService: ToastService,
              private dialogService: NbDialogService,
              private searchService: SearchService) {
    this.loadNext();
  }

  loadNext() {
    if (this.loading || this.stopLoading) {
      return;
    }
    this.loading = true;

    this.searchService.getAlbumNotApprovedSearchResults(this.currentSearch, this.pageToLoadNext).subscribe(searchResult => {

      if(this.pageToLoadNext > 0)
        this.albumList.push(...searchResult.albumList);
      else
        this.albumList = searchResult.albumList;

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

  openOverlay(album: Album) {
    this.dialogService.open(ArtistAlbumDialogComponent, {
      context: {
        id: album.idAlbum,
        dialogType: FeaturedDialogType.Album,
        imageUrl: album.urlCopertina,
        firstTextLine: album.nome,
        secondTextLine: album.descrizione,
        admin: true
      },
      hasScroll: true
    }).onClose.subscribe( () => {
      this.stopLoading = false;
      this.pageToLoadNext = 0;
      this.albumList = [];
      this.loadNext();
    });
  }

  search(text: string) {

    if (text == this.previousSearch || text.length < 3)
      return;

    this.previousSearch = text;

    //resetto il contatore della pagina della tabella ogni volta che la ricerca cambia
    this.pageToLoadNext = 0;

    this.stopLoading = false;

    //resetto la lista per svuotare la tabella e triggerare il loadnext
    this.albumList = [];
  }

  resetSearch() {
    if (this.currentSearch.length == 0)
      return;

    this.currentSearch = '';
    this.previousSearch = "";
    this.pageToLoadNext = 0;
    this.stopLoading = false;
    this.loadNext();
  }

}
