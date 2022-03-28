import {Observable} from 'rxjs';
import {ApiService} from './api.service';
import {Injectable} from '@angular/core';
import {map} from 'rxjs/operators';
import {NbMenuItem} from "@nebular/theme";

@Injectable({providedIn: 'root'})
export class MenuService {

  constructor(private api: ApiService) {
  }

  getMenu(): Observable<NbMenuItem[]> {
    return this.api.get("menu/getmenu", null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

}
