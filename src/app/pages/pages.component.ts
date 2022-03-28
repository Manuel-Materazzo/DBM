import {Component} from '@angular/core';
import {NbMenuItem} from "@nebular/theme";
import {MenuService} from "../services/menu.service";
import {AuthService} from "../services/auth/auth.service";
import {NbAuthJWTToken} from "@nebular/auth";

@Component({
  selector: 'ngx-pages',
  styleUrls: ['pages.component.scss'],
  template: `
    <ngx-one-column-layout>
      <nb-menu [items]="menu"></nb-menu>
      <router-outlet></router-outlet>
    </ngx-one-column-layout>
  `,
})
export class PagesComponent {

  menu: NbMenuItem[] = [];
  loading: boolean = true;

  constructor(private menuService: MenuService, private authService: AuthService) {
    authService.onTokenChange().subscribe( (token: NbAuthJWTToken) => {
      sessionStorage.removeItem('token');
      sessionStorage.setItem('token', token.getValue())
      menuService.getMenu().subscribe(res => {
      this.menu = res;
      this.loading = false;
    });
    });
  }
}
