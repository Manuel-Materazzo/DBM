import {Genere} from "./Genere";

export interface BranoCreateDTO {
  idBrano?: number;
  idAlbum?: number;
  titolo?: string;
  generi?: Genere[];
  file?: File;
}
