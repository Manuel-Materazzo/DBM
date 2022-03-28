import {Observable} from 'rxjs';
import {ApiService} from './api.service';
import {Injectable} from '@angular/core';
import { map } from 'rxjs/operators';
import {Stats} from "../util/DTO/Stats";

@Injectable({providedIn: 'root'})
export class StatsService {

  constructor(private api: ApiService) {
  }

  getStats(): Observable<Stats> {
    return this.api.unauthorizedGet("statistiche/getStatistiche", null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

}
