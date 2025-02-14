import { isPlatformBrowser } from "@angular/common";
import { Inject, Injectable, PLATFORM_ID } from "@angular/core";

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

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  cacheCheckoutFields(payload:CheckoutCache){//nao cachea metodo de entrega
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem('checkout-cache', JSON.stringify(payload));
    }  
  }

  getCachedCheckout():CheckoutCache|undefined{
    if (isPlatformBrowser(this.platformId)) {
      const jsonString = localStorage.getItem('checkout-cache');
      if (jsonString) {
        return { ...JSON.parse(jsonString) } as CheckoutCache;
      }
    }
    return undefined;
  }



}
