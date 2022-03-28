import {ChangeDetectorRef, Component, Inject, OnInit} from '@angular/core';
import {
  getDeepFromObject,
  NB_AUTH_OPTIONS,
  NbAuthResult,
  NbAuthSocialLink, NbLoginComponent
} from '@nebular/auth';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth/auth.service";

@Component({
  selector: 'ngx-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent extends NbLoginComponent{

  constructor(protected service: AuthService,
              @Inject(NB_AUTH_OPTIONS) protected options = {},
              protected cd: ChangeDetectorRef,
              protected router: Router) {
    super(service, options, cd, router);
  }

}
