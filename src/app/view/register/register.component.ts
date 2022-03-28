import {ChangeDetectorRef, Component, Inject, OnInit} from '@angular/core';
import {NB_AUTH_OPTIONS, NbAuthService, NbRegisterComponent} from '@nebular/auth';
import {AuthService} from "../../services/auth/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'ngx-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent extends NbRegisterComponent{

  constructor(protected service: AuthService,
              @Inject(NB_AUTH_OPTIONS) protected options = {},
              protected cd: ChangeDetectorRef,
              protected router: Router) {
    super(service,options,cd,router);
  }

}
