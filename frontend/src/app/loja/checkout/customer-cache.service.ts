import { Injectable } from "@angular/core";

export interface CheckoutCache{
  nome:string,
  documento:string,
  telefone:string,
  cep:string,
  estado:string,
  cidade:string,
  rua:string,
  bairro:string,
  numero:number
}
@Injectable({providedIn:"root"})
export class CustomerCache {

  cacheCheckoutFields(payload:CheckoutCache){//nao cachea metodo de entrega
    localStorage.setItem("checkout-cache",JSON.stringify(payload));
  }

  getCachedCheckout():CheckoutCache|undefined{
    const jsonString = localStorage.getItem("checkout-cache");
    if(jsonString){
      return {...JSON.parse(jsonString)} as CheckoutCache;
    }
    return undefined
  }



}
