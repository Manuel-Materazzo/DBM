import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import {tap} from 'rxjs/operators';
import {AuthService} from "./auth.service";
import {NbRoleProvider} from "@nebular/security";

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.authService.isAuthenticatedOrRefresh().pipe(
      tap(authenticated => {
        if (!authenticated) {
          this.router.navigate(['auth/login']);
        }
      }),
    );
  }
}

@Injectable({ providedIn: 'root' })
export class AuthGuardAdmin implements CanActivate {
  constructor(
    private router: Router,
    private authService: AuthService,
    private roleProvider: NbRoleProvider
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.authService.isAuthenticatedOrRefresh().pipe(
      tap(authenticated => this.roleProvider.getRole().subscribe( role => {
        if (!authenticated || role != "admin") {
          this.router.navigate(['auth/login']);
        }
        })),
    );
  }
}
