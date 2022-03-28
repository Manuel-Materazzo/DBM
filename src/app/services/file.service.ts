import {Observable} from 'rxjs';
import {ApiService} from './api.service';
import {Injectable} from '@angular/core';
import { map } from 'rxjs/operators';
import {UploadResponseDTO} from "../util/DTO/UploadResponseDTO";

@Injectable({providedIn: 'root'})
export class FileService {

  constructor(private api: ApiService) {
  }

  uploadImage(file: File): Observable<UploadResponseDTO> {
    return this.api.postFile("upload/image", file).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

  uploadAudio(file: File): Observable<UploadResponseDTO> {
    return this.api.postFile("upload/song", file).pipe(
      map((res: any) => {
        if (res) {
          return res;
        }
        return null;
      }));

  }

}
