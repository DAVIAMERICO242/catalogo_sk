import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Catalogo, CatalogoService } from '../../services/catalogo.service';
import { HttpClient } from '@angular/common/http';
import { env } from '../../../env';
import { Loja } from '../../services/loja.service';

@Injectable()
export class CatalogoContextService {

  lojaSub = new BehaviorSubject<Loja.Loja | undefined>(undefined);
  produtosSub = new BehaviorSubject<Catalogo.Produto[]|undefined>(undefined);
  constructor(private http:HttpClient,private catalogoService:CatalogoService){}

  setCatalogo(slug:string){
    this.catalogoService.getCatalogo(slug).subscribe({
      next:(data)=>{
        this.produtosSub.next(data);
      }
    })
  }

  setLoja(slug:string){
    this.http.get<Loja.Loja>(env.BACKEND_URL + "/loja/by-slug").subscribe((loja)=>{
      this.lojaSub.next(loja);
    })
  }


}
