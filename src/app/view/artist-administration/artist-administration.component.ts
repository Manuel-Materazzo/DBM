import {Component, OnInit} from '@angular/core';
import {Artista} from '../../util/DTO/Artista';
import {NbDialogService} from '@nebular/theme';
import {ArtistEditDialogComponent} from '../../components/artist-edit-dialog/artist-edit-dialog.component';
import {ArtistaService} from '../../services/artista.service';
import {ToastService} from '../../services/toast.service';
import {SearchService} from '../../services/search.service';

@Component({
  selector: 'ngx-artist-administration',
  templateUrl: './artist-administration.component.html',
  styleUrls: ['./artist-administration.component.scss']
})
export class ArtistAdministrationComponent {

  constructor(private dialogService: NbDialogService,
              private artistaService: ArtistaService,
              private toastService: ToastService,
              private searchService: SearchService) {
    this.loadNext();
  }

  artistiList: Artista[] = [];
  loading: boolean = false;
  stopLoading: boolean = false;
  pageToLoadNext: number = 0;
  pageSize = 12; // MCM di 2, 3 e 4, per avere pagine sempre piene fino alla fine

  previousSearch: string = '';
  currentSearch: string = '';

  loadNext() {
    if (this.loading || this.stopLoading) {
      return;
    }
    this.loading = true;


    this.searchService.getArtistSearchResults(this.currentSearch, this.pageToLoadNext).subscribe(searchResult => {

      if(this.pageToLoadNext > 0)
        this.artistiList.push(...searchResult.artistiList);
      else
        this.artistiList = searchResult.artistiList;

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
    this.dialogService.open(ArtistEditDialogComponent, {
      context: {
        id: artista.idArtista
      },
      hasScroll: true
    }).onClose.subscribe( () => {
      this.stopLoading = false;
      this.pageToLoadNext = 0;
      this.artistiList = [];
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
    this.artistiList = [];
  }

  resetSearch() {
    if (this.currentSearch.length == 0)
      return;

    this.currentSearch = "";
    this.previousSearch = "";
    this.stopLoading = false;
    this.pageToLoadNext = 0;
    this.stopLoading = false;
    this.loadNext();
  }

}
