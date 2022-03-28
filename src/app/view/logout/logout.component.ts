import {Component} from '@angular/core';
import {NbAuthService, NbLogoutComponent, NbTokenService} from '@nebular/auth';
import {AuthService} from '../../services/auth/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'ngx-logout',
  template: `
    <div style="text-align: center">
      Logout eseguito con sucesso, se non sei stato reindirizzato clicca <a href="/">qui</a>
    </div>
  `
})
export class LogoutComponent extends NbLogoutComponent {

  constructor(private authService: AuthService, router: Router, private nbTokenService:NbTokenService) {
    super(authService, {}, router);
    this.logout();
  }

  logout() {
    this.nbTokenService.clear()
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('role');
    this.router.navigate(['']);
  }

}
