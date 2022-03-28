import {Genere} from "./DTO/Genere";

export class GeneriUtil{

  static generiListToString(list: Genere[]): string{
    let response = "";

    list?.forEach(genere => {
      response += genere.tipologia + " ";
    });

    return response;
  }

}
