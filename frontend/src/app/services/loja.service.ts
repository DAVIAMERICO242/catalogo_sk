import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { env } from '../../env';

export namespace Loja{
  export interface Loja{
    loja:string,
    systemId:string,
    franquia:Franquia
  }
  export interface Franquia{
    franquia:string,
    systemId:string
  }
}
@Injectable({
  providedIn: 'root'
})
export class LojaService {

  constructor(private http:HttpClient) { }

  getLojas(){
    return this.http.get<Loja.Loja[]>(env.BACKEND_URL+"/lojas");
  }

}
