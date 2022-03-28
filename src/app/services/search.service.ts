import {Observable} from 'rxjs';
import {ApiService} from './api.service';
import {Injectable} from '@angular/core';
import { map } from 'rxjs/operators';
import {Search} from '../util/DTO/Search';

@Injectable({providedIn: 'root'})
export class SearchService {

  constructor(private api: ApiService) {
  }

  getSearchResults(searchText:string, page: number): Observable<Search> {

    searchText == null ? page : searchText + "/" + page

    return this.api.unauthorizedGet("search/featuredSearch/" +
            (searchText == "" ? page : searchText + "/" + page), null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  getArtistSearchResults(searchText:string, page: number): Observable<Search> {
    return this.api.unauthorizedGet("search/artistSearch/" +
            (searchText == "" ? page : searchText + "/" + page), null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  getAlbumSearchResults(searchText:string, page: number): Observable<Search> {
    return this.api.unauthorizedGet("search/albumSearch/" +
            (searchText == "" ? page : searchText + "/" + page), null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  getAlbumNotApprovedSearchResults(searchText:string, page: number): Observable<Search> {
    return this.api.get("search/albumNotApprovedSearch/" +
            (searchText == "" ? page : searchText + "/" + page), null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }


}
