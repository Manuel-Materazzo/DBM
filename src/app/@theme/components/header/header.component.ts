import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  NbDialogService,
  NbMediaBreakpointsService,
  NbMenuService,
  NbSidebarService,
  NbThemeService
} from '@nebular/theme';

import { LayoutService } from '../../../@core/utils';
import {map, takeUntil} from 'rxjs/operators';
import { Subject } from 'rxjs';
import {Router} from '@angular/router';
import {NbAuthJWTToken} from '@nebular/auth';
import {ApiService} from '../../../services/api.service';
import {AuthService} from "../../../services/auth/auth.service";
import {ConfirmDialogComponent} from '../../../components/confirm-dialog/confirm-dialog.component';
import {ToastService} from '../../../services/toast.service';
import {ArtistaService} from '../../../services/artista.service';

@Component({
  selector: 'ngx-header',
  styleUrls: ['./header.component.scss'],
  templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit, OnDestroy {

  private destroy$: Subject<void> = new Subject<void>();
  user: any = {};
  themes = [
    {
      value: 'default',
      name: 'Chiaro',
    },
    {
      value: 'dark',
      name: 'Scuro',
    },
    {
      value: 'cosmic',
      name: 'Solarizzato',
    },
    {
      value: 'corporate',
      name: 'Alternativo',
    },
  ];

  currentTheme = 'default';

  userMenu = [ { title: 'Profilo' }, { title: 'Logout' } ];

  constructor(private sidebarService: NbSidebarService,
              private menuService: NbMenuService,
              private themeService: NbThemeService,
              private layoutService: LayoutService,
              private nbMenuService: NbMenuService,
              private router: Router,
              private authService: AuthService,
              private toastService: ToastService,
              private artistaService: ArtistaService) {

    this.artistaService.getProfilo().subscribe( currentUser => {
      sessionStorage.removeItem('role');
      sessionStorage.setItem('role', currentUser.admin ? "admin" : "artist");
      this.user.name = currentUser.nomeArte;
      this.user.role = currentUser.admin ? "admin" : "";
      this.user.bannato = currentUser.bannato;
      this.user.picture = currentUser.urlAvatar;
      if(this.user.picture == 'https://clinicforspecialchildren.org/wp-content/uploads/2016/08/avatar-placeholder.gif'){
        this.toastService.showToast('info', 'Completa il profilo per continuare', '')
        this.router.navigate(['/pages/profile']);
      }
    }, error => toastService.showGenericConnectionErrorToast());

  }

  ngOnInit() {
    this.currentTheme = this.themeService.currentTheme;

    if(this.user.bannato)
      this.router.navigate(['/ban']);


    this.themeService.onThemeChange()
      .pipe(
        map(({ name }) => name),
        takeUntil(this.destroy$),
      )
      .subscribe(themeName => this.currentTheme = themeName);

    this.nbMenuService.onItemClick()
      .pipe(map(({ item: { title } }) => title))
      .subscribe(title => {

        switch (title){
          case 'Profilo':
            this.router.navigate(['/pages/profile']);
            break;
          case 'Logout':
            this.router.navigate(['/auth/logout']);
            break;
          default:
            break;
        }

      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  changeTheme(themeName: string) {
    this.themeService.changeTheme(themeName);
  }

  toggleSidebar(): boolean {
    this.sidebarService.toggle(true, 'menu-sidebar');
    this.layoutService.changeLayoutSize();

    return false;
  }

  navigateHome() {
    this.menuService.navigateHome();
    return false;
  }
}
