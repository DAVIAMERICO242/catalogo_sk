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
}
