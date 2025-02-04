import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Catalogo, CatalogoService } from '../services/catalogo.service';
import { HttpClient } from '@angular/common/http';
import { env } from '../../env';
import { Loja } from '../services/loja.service';

@Injectable()
export class LojaContextService {

  lojaSub = new BehaviorSubject<Loja.Loja | undefined>(undefined);
  loja$ = this.lojaSub.asObservable();
  produtosSub = new BehaviorSubject<Catalogo.Produto[]|undefined>(undefined);
  produtos$ = this.produtosSub.asObservable();
  constructor(private http:HttpClient,private catalogoService:CatalogoService){}

  setCatalogo(slug:string){
    this.catalogoService.getCatalogo(slug).subscribe({
      next:(data)=>{
        this.produtosSub.next(data);
      }
    })
  }

  setLoja(slug:string){
    this.http.get<Loja.Loja>(env.BACKEND_URL + "/lojas/by-slug?slug="+slug).subscribe((loja)=>{
      this.lojaSub.next(loja);
    })
  }


}
