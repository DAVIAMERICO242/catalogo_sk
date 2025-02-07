import { Component, OnDestroy, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { Sacola, SacolaService } from '../../services/sacola.service';
import { LojaContextService } from '../loja-context.service';
import { Loja } from '../../services/loja.service';
import { filter, Subscriber, Subscription, take } from 'rxjs';
import { ProdutoSacolaComponent } from "./produto-sacola/produto-sacola.component";

@Component({
  selector: 'app-sacola',
  imports: [SharedModule, ProdutoSacolaComponent],
  templateUrl: './sacola.component.html',
  styles:[
    `
    .descricao{
      display: -webkit-box;
      -webkit-line-clamp: 3;
      -webkit-box-orient: vertical;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    .tiny-scroll::-webkit-scrollbar {
      width: 5px; /* Largura do scrollbar vertical */
      height: 5px;
    }
    .tiny-scroll::-webkit-scrollbar-thumb{
      background: #dfdada; /* Cor do trilho */
    }
    `
  ]
})
export class SacolaComponent implements OnInit,OnDestroy {
  loja!:Loja.Loja;
  beautySacola!:Sacola.BeautySacola|undefined;
  totaisItensSacola!:number;
  subscriptions = new Subscription();
  open = false;
  constructor(protected sacolaService:SacolaService,private lojaContext:LojaContextService){}
  ngOnInit(): void {
    this.lojaContext.loja$.pipe(filter(loja => !!loja), take(1)).subscribe((loja)=>
      {
          if(loja){
            this.loja = loja;
            this.setSacola();
          }
      });
    this.subscriptions.add(this.sacolaService.onSacolaChange$.subscribe(()=>{
      this.setSacola();
    }));
    this.subscriptions.add(this.sacolaService.open$.subscribe((val)=>{
      this.open = val;
    }));
     
  }
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
      
  setSacola(){
    this.beautySacola = this.sacolaService.getBeautySacolaForLoja({
      nome:this.loja.loja,
      slug:this.loja.slug,
      systemId:this.loja.systemId
    });
    if(this.beautySacola?.itens.length || this.beautySacola?.itens.length===0){
      this.totaisItensSacola = this.beautySacola?.itens.reduce((a,b)=>a+b.quantidade,0);
    }
    console.log(this.beautySacola)
  }

}
