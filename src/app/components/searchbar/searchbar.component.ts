import {Component, EventEmitter, Input, Output, ViewEncapsulation} from '@angular/core';

@Component({
  selector: 'ngx-searchbar',
  templateUrl: './searchbar.component.html',
  styleUrls: ['./searchbar.component.scss'],
})
export class SearchbarComponent  {

  constructor() { }

  @Input() searchContent: string = '';
  @Output() searchContentChange = new EventEmitter<string>();

  @Output() searchEvent = new EventEmitter<string>();

  Search(){
    //emetto l'evento che informa i componenti parent che è stata inviata una ricerca
    this.searchEvent.next(this.searchContent);
    this.searchContentChange.next(this.searchContent);
  }

}
