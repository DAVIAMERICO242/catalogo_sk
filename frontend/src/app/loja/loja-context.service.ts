import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Catalogo, CatalogoService } from '../services/catalogo.service';
import { HttpClient } from '@angular/common/http';
import { env } from '../../env';
import { Loja } from '../services/loja.service';
import { Desconto, DescontosService } from '../services/descontos.service';

@Injectable()
export class LojaContextService {

  private lojaSub = new BehaviorSubject<Loja.Loja | undefined>(undefined);
  loja$ = this.lojaSub.asObservable();
  produtosSub = new BehaviorSubject<Catalogo.Produto[]|undefined>(undefined);
  produtos$ = this.produtosSub.asObservable();
  descontosSub = new BehaviorSubject<Desconto.DescontoModel[]|undefined>(undefined);
  descontos$ = this.descontosSub.asObservable();
  constructor(private http:HttpClient,private catalogoService:CatalogoService,private descontoService:DescontosService){}

  setCatalogo(slug:string){
    this.catalogoService.getCatalogo(slug).subscribe({
      next:(data)=>{
        this.produtosSub.next(data);
      }
    })
  }

  setDescontos(slug:string){
    this.descontoService.getDescontosAtivosBySlug(slug).subscribe((data)=>{
      this.descontosSub.next(data);
    })
  }

  setLoja(slug:string){
    this.http.get<Loja.Loja>(env.BACKEND_URL + "/lojas/by-slug?slug="+slug).subscribe((loja)=>{
      this.lojaSub.next(loja);
    })
  }


}
