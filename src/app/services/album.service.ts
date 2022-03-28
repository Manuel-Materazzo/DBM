import {Observable} from 'rxjs';
import {ApiService} from './api.service';
import {Injectable} from '@angular/core';
import { map } from 'rxjs/operators';
import {Album} from "../util/DTO/Album";
import {Artista} from '../util/DTO/Artista';
import {Brano} from "../util/DTO/Brano";

@Injectable({providedIn: 'root'})
export class AlbumService {

  constructor(private api: ApiService) {
  }

  getAlbumsByIdArtist(id:number): Observable<Set<Album>> {
    return this.api.unauthorizedGet("album/getAlbumByIdArtista/" + id, null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  getAllAlbums(): Observable<Set<Album>> {
    return this.api.get("album/getAllAlbum", null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  getMyTopAlbums(): Observable<Set<Album>> {
    return this.api.get("album/getTopAlbumByIdArtista", null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  getMyAlbumsNotApproved(): Observable<Set<Album>> {
    return this.api.get("album/getAlbumNotApprovedWithBrani/false", null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  updateAlbum(album: Album): Observable<Artista> {
    return this.api.patch("album/updateAlbumById", album).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  createAlbum(album: Album): Observable<Album>{
    return this.api.post("album/addAlbumWithSongs", album).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));
  }

  deleteAlbumById(id: number): Observable<Set<Brano>> {
    return this.api.delete("album/deleteAlbumById/" + id).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

}
