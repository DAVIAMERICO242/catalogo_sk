import { Component, OnDestroy, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { Sacola, SacolaService } from '../../services/sacola.service';
import { LojaContextService } from '../loja-context.service';
import { Loja } from '../../services/loja.service';
import { filter, Subscriber, Subscription, take } from 'rxjs';
import { ProdutoSacolaComponent } from "./produto-sacola/produto-sacola.component";
import { Pedidos } from '../../services/pedidos.service';
import { Desconto, DescontosService } from '../../services/descontos.service';
import { DescontosAplicadosComponent } from "./descontos-aplicados/descontos-aplicados.component";
import { ValoresDetailsComponent } from "./valores-details/valores-details.component";
import { SacolaUiContextService } from './sacola-ui-context.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sacola',
  imports: [SharedModule, ProdutoSacolaComponent, DescontosAplicadosComponent, ValoresDetailsComponent],
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
  sacola!:Sacola.SacolaModel|undefined;
  totaisItensSacola!:number;
  subscriptions = new Subscription();
  variacoesSacola!:Sacola.ProdutoVariacaoModel[]
  open = false;
  constructor(
    protected sacolaContext:SacolaUiContextService,
    private lojaContext:LojaContextService,
    private router:Router

  ){}
  ngOnInit(): void {

    this.lojaContext.loja$.pipe(filter(loja => !!loja), take(1)).subscribe((loja)=>
      {
          if(loja){
            this.loja = loja;
            this.setSacola();
          }
      });
    this.subscriptions.add(this.sacolaContext.onSacolaChange$.subscribe(()=>{
      this.setSacola();
    }));
    this.subscriptions.add(this.sacolaContext.open$.subscribe((val)=>{
      this.open = val;
    }));
     
  }
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
      
  setSacola(){
    this.sacola = this.sacolaContext.getSacolaForLoja(this.loja);
    if(this.sacola?.produtos.length || this.sacola?.produtos.length===0){
      this.variacoesSacola = this.sacola.produtos.flatMap((e)=>e.produtoBase.variacoes);
      this.totaisItensSacola = this.sacola?.produtos.flatMap(e=>e.produtoBase.variacoes).reduce((a,b)=>a+b.quantidade,0);
    }else{
      this.totaisItensSacola = 0
    }
    if(this.sacola){
      this.sacolaContext.setDescontos(this.sacola);
    }
    
  }

  goToCheckout(){
    this.open = false;
    this.router.navigate([this.loja.slug + "/checkout"])
  }




  limpar(){
    this.sacolaContext.limparSacola(this.loja);
  }

}
