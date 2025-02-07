import { Component, Input, OnInit } from '@angular/core';
import { Catalogo } from '../../services/catalogo.service';
import { SharedModule } from '../../shared/shared.module';
import { LojaContextService } from '../loja-context.service';
import { filter, take } from 'rxjs';
import { Desconto } from '../../services/descontos.service';

@Component({
  selector: 'app-produto-preco',
  imports: [SharedModule],
  templateUrl: './produto-preco.component.html'
})
export class ProdutoPrecoComponent implements OnInit {
  @Input()
  styleClass = "";
  @Input()
  produto!:Catalogo.Produto;
  descontos!:Desconto.DescontoModel[];
  precoComDesconto!:number;

  constructor(private lojaContext:LojaContextService){}
  ngOnInit(): void {
    this.precoComDesconto = this.produto.produtoBase.preco;
    this.lojaContext.descontosSub.pipe(
      filter(d=>d!==undefined),
      take(1)
    ).subscribe((data)=>{
      this.descontos = data;
      this.calcularDesconto();
    });
  }

  calcularDesconto(){
    const termos = [this.produto.produtoBase.categoria,this.produto.produtoBase.grupo,this.produto.produtoBase.linha];
    const produtosEmDesconto = this.descontos.map((e)=>e.descontoSimples?.produto);
    if(produtosEmDesconto?.map(e=>e?.systemId).includes(this.produto.systemId)){
      const valorPercentDesconto = this.descontos.filter((e)=>e.descontoSimples?.produto.systemId===this.produto.systemId).reduce((a,b)=>a+(b?.descontoSimples?.percentDecimalDiscount||0),0);
      if(valorPercentDesconto){
        this.precoComDesconto = this.produto.produtoBase.preco*(1-valorPercentDesconto);
      }
    }
    const decontoTermos = this.descontos.map((e)=>e.descontoSimplesTermo);
    decontoTermos.forEach((e)=>{
      const delimitedTermos = [e?.delimitedCategorias,e?.delimitedGrupos,e?.delimitedLinhas].flat().filter(Boolean);
      const excludedTermos = [e?.excludedCategorias,e?.excludedGrupos,e?.excludedLinhas].flat().filter(Boolean);
      termos.forEach((e1)=>{
        if(delimitedTermos.includes(e1) && !excludedTermos.includes(e1)){
          this.precoComDesconto = this.precoComDesconto - this.precoComDesconto*(e?.percentDecimalDiscount||0);
        }
      })
    });
    if(this.produto.produtoBase.descricao.includes("SUNGA")){
      console.log("a")
    }
  }



}
