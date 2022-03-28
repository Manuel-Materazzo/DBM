import {Observable} from 'rxjs';
import {ApiService} from './api.service';
import {Injectable} from '@angular/core';
import {Genere} from '../util/DTO/Genere';
import {map} from 'rxjs/operators';

@Injectable({providedIn: 'root'})
export class GenreService {

  constructor(private api: ApiService) {
  }

  getAllGenres(): Observable<Genere[]> {
    return this.api.unauthorizedGet("generi/getAllGeneri", null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

}
