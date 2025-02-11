import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { env } from '../../env';


export namespace CorreiosFranquiasContext{
  export interface CorreiosFranquias{
    systemId:string,
    franquiaId:string,
    usuario:string,
    senha:string,
    codigoPac:string,
    codigoSedex:string,
    cepOrigem:string,
    numeroDiretoriaRegional:string,
    numeroContrato:string,
    numeroCartaoPostal:string
  }
  export interface PesoCategoria{
    systemId:string,
    franquiaId:string,
    categoria:string,
    pesoGramas:number
  }
}
@Injectable({
  providedIn: 'root'
})
export class CorreiosFranquiasContextService {

  constructor(private http:HttpClient) { }

  criarAtualizar(payload:CorreiosFranquiasContext.CorreiosFranquias){
    return this.http.post<CorreiosFranquiasContext.CorreiosFranquias>(env.BACKEND_URL+"/correio-franquia-context",payload);
  }

  getByFranquiaId(franquiaId:string){
    return this.http.get<CorreiosFranquiasContext.CorreiosFranquias | undefined | null>(env.BACKEND_URL+"/correio-franquia-context?franquiaId="+franquiaId);
  }

  getPesoCategorias(franquiaId:string){
    return this.http.get<CorreiosFranquiasContext.PesoCategoria[]>(env.BACKEND_URL+"/peso-categoria?franquiaId="+franquiaId);
  }

  cadastrarAtualizarPeso(payload:CorreiosFranquiasContext.PesoCategoria){
    return this.http.post<CorreiosFranquiasContext.PesoCategoria>(env.BACKEND_URL+"/peso-categoria",payload);
  }
}
