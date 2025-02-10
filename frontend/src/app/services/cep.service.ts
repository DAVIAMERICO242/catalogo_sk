import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { env } from '../../env';

export namespace CEP{
  export interface CEPResponse{
    rua:string,
    bairro:string,
    cidade:string,
    estado:string
  }
}
@Injectable({
  providedIn: 'root'
})
export class CepService {

  constructor(private http: HttpClient) {}

  buscarCep(cep: string) {
    return this.http.get<CEP.CEPResponse>(`${env.BACKEND_URL+"/cep"}?cep=${cep}`);
  }
}