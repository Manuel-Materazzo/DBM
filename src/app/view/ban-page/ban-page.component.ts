import { Component, OnInit } from '@angular/core';
import {NbTokenService} from '@nebular/auth';

@Component({
  selector: 'ngx-ban-page',
  templateUrl: './ban-page.component.html',
  styleUrls: ['./ban-page.component.scss']
})
export class BanPageComponent implements OnInit {

  constructor(private nbTokenService:NbTokenService) { }

  ngOnInit(): void {
    this.nbTokenService.clear()
    sessionStorage.removeItem('token');
  }

}
