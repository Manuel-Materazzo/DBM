import { Component, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import {Cell, DefaultEditor} from 'ng2-smart-table';
import {GenreService} from '../../../services/genre.service';
import {Genere} from "../../../util/DTO/Genere";

@Component({
  template: `

    <span *ngFor="let genere of generiAggiunti">
      {{genere.tipologia}}<nb-icon icon="minus-circle" pack="eva" (click)="RemoveGenre(genere)"></nb-icon>
    </span>

    <button nbButton status="info" nbPopoverPlacement="bottom" [disabled]="!cell.isEditable() || maxGeneriRaggiunto"
            [nbPopover]="form">+</button>

    <ng-template #form >
      <div class="p-4" *ngIf="!maxGeneriRaggiunto">
        <p>Aggiungi nuovo genere (max 5)</p>
          <div class="form-group" *ngFor="let genre of genres">
            <button class="btn btn-primary" (click)="AddGenre(genre)" style="display: inline-block;">+</button>
            {{genre.tipologia}}
          </div>
      </div>
    </ng-template>
  `,
})
export class GenreEditorComponent extends DefaultEditor implements AfterViewInit {

  generiAggiunti: Genere[] = [];
  maxGeneriRaggiunto: boolean = false;
  genres: Genere[];

  constructor(private genreService: GenreService) {
    super();
    genreService.getAllGenres().subscribe( res=> this.genres = res );
  }

  ngAfterViewInit() {
    if (this.cell.newValue !== "") {
      this.generiAggiunti = this.cell.newValue;
    }
  }

  AddGenre(genreToAdd: Genere){

    //rimuovo dalla lista generi e aggiungo alla lista generi aggiunti
    this.genres = this.genres.filter(genre => {
      if(genre.idGenere !== genreToAdd.idGenere)
        return true;

      this.generiAggiunti.push(genre);
      return false;
    });

    this.updateValue()

    //limite 5 generi
    if(this.generiAggiunti.length >= 5){
      this.maxGeneriRaggiunto = true;
      return;
    }
  }

  updateValue() {
    this.cell.newValue = this.generiAggiunti;
  }

  RemoveGenre(genreToRemove: Genere){

    //rimuovo dai generi aggiunti e aggiungo alla lista generi
    this.generiAggiunti = this.generiAggiunti.filter(genre => {
      if(genre.idGenere !== genreToRemove.idGenere)
        return true;

      this.genres.push(genre)
      return false;
    });

    this.updateValue()

    //limite 5 generi
    if(this.generiAggiunti.length < 5){
      this.maxGeneriRaggiunto = false;
      return;
    }
  }

}
