import {Genere} from './Genere';

export interface Brano {
  idBrano?: number;
  titolo: string;
  durata: number;
  url: string;
  approvato?: boolean;
  urlCopertina?: string;
  urlImmagineArtista?: string;
  listGeneri?: Genere[];
  albumID?: number;
}

export interface BranoApproveDTO {
  idBrano: number;
  approvato: boolean;
}
