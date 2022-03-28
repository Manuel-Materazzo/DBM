import {Observable} from 'rxjs';
import {ApiService} from './api.service';
import {Injectable} from '@angular/core';
import { map } from 'rxjs/operators';
import {Artista} from "../util/DTO/Artista";

@Injectable({providedIn: 'root'})
export class ArtistaService {

  constructor(private api: ApiService) {
  }

  getProfilo(): Observable<Artista>{
    return this.api.get("artisti/getProfilo", null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));
  }

  getArtistaById(id:number): Observable<Artista> {
    return this.api.get("artisti/getArtistaById/" + id, null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  updateArtista(artista: Artista): Observable<Artista> {
    return this.api.patch("artisti/updateArtista", artista).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  deleteArtistaById(id:number): Observable<Artista> {
    return this.api.delete("artisti/deleteArtistaById/" + id).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

}
