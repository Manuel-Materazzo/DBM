export interface AlbumData{
  data: AlbumDataObject;
  children?: SongDataObject[];
}

export interface AlbumDataObject {
  id: number,
  nome: string,
  durata: string,
  approvato?: boolean;
  numeroCanzoni?: number,
  genere?: string,
}

export interface SongDataObject {
  data:{
    id: number,
    titolo: string,
    durata: string,
    genere: string
  }
}

