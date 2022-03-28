import {Injectable} from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AuthService} from "./auth.service";
import {Router} from "@angular/router";

//è aggiunto come provider nell'app module
@Injectable()
export class AuthErrorInterceptorService implements HttpInterceptor {
  constructor(private authService: AuthService,
              private router: Router) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => {
      if (err.status === 401) {
        this.authService.isAuthenticatedOrRefresh().subscribe(authenticated => {

          if (authenticated)
            next.handle(request).subscribe();
          else
            this.router.navigate(["auth/login"]);

        });
      }

      const error = err.error.message || err.statusText;
      return throwError(error);
    }))
  }
}
