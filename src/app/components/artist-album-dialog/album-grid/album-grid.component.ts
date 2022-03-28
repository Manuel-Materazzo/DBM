import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {BranoApproveDTO} from "../../../util/DTO/Brano";

export interface GridSettings {
  primaColonna: string;
  nomeDatiPrimaColonna: string;
  altreColonne: string[];
  nomeDatiAltreColonne: string[];
}

@Component({
  selector: 'ngx-album-grid[dataSet][gridSettings]',
  templateUrl: './album-grid.component.html',
  styleUrls: ['./album-grid.component.scss'],
})
export class AlbumGridComponent implements OnInit{

  colonne: string[];

  @Input() dataSet;
  @Input() admin:boolean = false;
  @Input() gridSettings: GridSettings;

  @Output() playClickEvent = new EventEmitter<number>();
  @Output() ApproveChangeEvent = new EventEmitter<BranoApproveDTO>();

  bools = {};

  constructor() {
  }

  ngOnInit() {
    //non posso farlo nel costruttore, altrimenti le variabili non verrebbero injectate in tempo
    this.colonne = [ this.gridSettings.primaColonna , ...this.gridSettings.altreColonne ];
    if(this.admin) this.colonne.push('approva')
    this.dataSet.forEach( data =>{
      this.bools[data.data.id] = data.data.approvato;
    });
  }

  getShowOn(index: number) {
    const minWithForMultipleColumns = 400;
    const nextColumnStep = 100;
    return minWithForMultipleColumns + (nextColumnStep * index);
  }

  //uso l'event emitter per notificare ai componenti "parent" quando viene cliccato il tasto play
  playclick(id: number){
    this.playClickEvent.next(id);
  }

  //uso l'event emitter per notificare ai componenti "parent" quando viene cliccato il tasto approva su una canzone
  approveclick(data, event){
    let brano: BranoApproveDTO = {
      approvato: event,
      idBrano: data.id
    };
    this.ApproveChangeEvent.next(brano);
  }

}
