import {Observable} from 'rxjs';
import {ApiService} from './api.service';
import {Injectable} from '@angular/core';
import { map } from 'rxjs/operators';
import {Brano} from "../util/DTO/Brano";

@Injectable({providedIn: 'root'})
export class BraniService {

  constructor(private api: ApiService) {
  }

  getBraniByIdAlbum(id:number): Observable<Set<Brano>> {
    return this.api.unauthorizedGet("brani/getBraniByIdAlbum/" + id, null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  getBranoById(id:number): Observable<Brano> {
    return this.api.unauthorizedGet("brani/getBranoById/" + id, null).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  createBrano(brano: Brano): Observable<Brano>{
    return this.api.post("brani/addBrano", brano).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));
  }

  updateBrano(brano: Brano): Observable<Brano> {
    return this.api.patch("brani/updateBrano", brano).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  approvaBrano(brano: Brano): Observable<Brano> {
    return this.api.patch("brani/approvaBrano", brano).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  deleteBranoByNome(nome: string): Observable<Set<Brano>> {
    return this.api.delete("brani/deleteBranoByTitolo/" + nome).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  deleteBranoById(id: number): Observable<Set<Brano>> {
    return this.api.delete("brani/deleteBranoById/" + id).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

}
