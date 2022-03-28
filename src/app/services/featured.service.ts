import {Observable} from 'rxjs';
import {ApiService} from './api.service';
import {Injectable} from '@angular/core';
import { map } from 'rxjs/operators';
import {Featuring} from '../util/DTO/Featuring';

@Injectable({providedIn: 'root'})
export class FeaturedService {

  constructor(private api: ApiService) {
  }

  getActiveFeaturings(): Observable<Set<Featuring>> {
    return this.api.unauthorizedGet("featuring/getActiveFeaturing", null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  createFeaturing(featuring: Featuring): Observable<Featuring>{
    return this.api.post("featuring/addFeaturing", featuring).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));
  }

}
