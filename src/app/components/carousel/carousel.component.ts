import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Album} from "../../util/DTO/Album";

@Component({
  selector: 'ngx-carousel',
  templateUrl: './carousel.component.html',
  styleUrls: ['./carousel.component.scss']
})
export class CarouselComponent {

  @Input() albums: Set<Album>;

  @Output() cardClickEvent = new EventEmitter<Album>();
  @Output() seeAllEvent = new EventEmitter();

  constructor() { }

  CardClick(album:Album){
    //emetto l'evento che avvisa il parent del clic sull'album
    this.cardClickEvent.next(album);
  }

  VediClick(){
    //emetto l'evento che avvisa il parent del clic sul "vedi tutti"
    this.seeAllEvent.next();
  }

}
