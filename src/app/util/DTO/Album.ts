import {Artista} from './Artista';
import {Brano} from './Brano';

export class Album{
  idAlbum?: number;
  nome?: string;
  dataPubblicazione?: Date;
  descrizione?: string;
  casaDiscografica?: string;
  urlCopertina?: string;
  approvato?: boolean;
  discoOro?: number;
  discoPlatino?: number;
  urlImmagineArtista?: string;
  listArtisti?: Artista[];
  braniList?: Brano[];
}
