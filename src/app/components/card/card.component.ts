import {Component, Input, OnInit, ViewEncapsulation} from '@angular/core';

@Component({
  selector: 'ngx-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CardComponent {

  constructor() { }
  @Input() imageSmall: string;
  @Input() imageBig: string = 'https://i.imgur.com/oYiTqum.jpg';
  @Input() title: string = 'Title';
  @Input() status: string;
  @Input() description: string = 'description';
  @Input() grayed: boolean = false;

}
