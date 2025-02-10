import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { env } from '../../env';
import { map } from 'rxjs';

export namespace Loja{

  export interface LojaChangebleFieldsPayload{
    systemId:string,
    endereco:string,
    cep:string,
    telefone:string
  }
  export interface Loja{
    loja:string,
    systemId:string,
    slug:string,
    franquia:Franquia,
    endereco?:string,
    telefone?:string,
    cep?:string
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

  constructor(private http:HttpClient) {}
  
  mudarLoja(payload:Loja.LojaChangebleFieldsPayload){
    return this.http.put<void>(env.BACKEND_URL+"/lojas",payload);
  }//sera se serei livre um dia??

  getLojas(){
    return this.http.get<Loja.Loja[]>(env.BACKEND_URL+"/lojas").pipe(
      map((data)=>{
        return data.map((e)=>{
          return{
            ...e,
            endereco:e.endereco?.replace(/[\n\r]/g, ' ')
          }
        })
      }));
  }

  getLojasMatriz(){
    return this.http.get<Loja.Loja[]>(env.BACKEND_URL+"/lojas/matriz").pipe(
      map((data)=>{
        return data.map((e)=>{
          return{
            ...e,
            endereco:e.endereco?.replace(/[\n\r]/g, ' ')
          }
        })
      }));
  }

  getLojasFranquia(){
    return this.http.get<Loja.Loja[]>(env.BACKEND_URL+"/lojas/franquia").pipe(
      map((data)=>{
        return data.map((e)=>{
          return{
            ...e,
            endereco:e.endereco?.replace(/[\n\r]/g, ' ')
          }
        })
      }));
  }

}
