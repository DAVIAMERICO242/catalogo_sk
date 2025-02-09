import { Component, Input, OnInit } from '@angular/core';
import { Sacola, SacolaService } from '../../../services/sacola.service';
import { Loja } from '../../../services/loja.service';
import { Produto, ProdutosService } from '../../../services/produtos.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { Pedidos } from '../../../services/pedidos.service';
import { SharedModule } from '../../../shared/shared.module';
import { ProdutoPrecificavel, ProdutoPrecoComponent } from '../../produto-preco/produto-preco.component';
import { SacolaUiContextService } from '../sacola-ui-context.service';

@Component({
  selector: 'app-produto-sacola',
  imports: [SharedModule, ProdutoPrecoComponent],
  templateUrl: './produto-sacola.component.html'
})
export class ProdutoSacolaComponent implements OnInit {
  @Input({required:true})
  loja!:Loja.Loja;
  @Input({required:true})
  produtoSacola!:Sacola.BeautySacolaItem;//VARIACAO
  produtoPrecificavel!:ProdutoPrecificavel;//pra usar o componente que abstrai descontos
  loadingUnitUpdate = false;

  constructor(private sacolaContext:SacolaUiContextService,private produtoService:ProdutosService,private message:MessageService){}
  ngOnInit(): void {
    const loja:Pedidos.LojaPedido={
      nome:this.loja.loja,
      slug:this.loja.slug,
      systemId:this.loja.systemId
    }
    const produto:Sacola.ProdutoRawSacola | undefined = this.sacolaContext.getRawSacolaForLoja(loja)?.produtos.find(e=>e.variacoesCompradas.map(e=>e.systemId).includes(this.produtoSacola.systemId));
    if(produto){
      this.produtoPrecificavel = produto;
    }
  }



  increaseOneUnit(){
    this.loadingUnitUpdate = true;
    this.produtoService.getVariationStock(this.produtoSacola.sku,this.loja.slug).subscribe({
      next:(data)=>{
        if(data.estoque>=(this.produtoSacola.quantidade + 1)){
          const loja:Pedidos.LojaPedido={
            nome:this.loja.loja,
            slug:this.loja.slug,
            systemId:this.loja.systemId
          }
          const sacolaRaw = this.sacolaContext.getRawSacolaForLoja(loja);
          const produtoExistente = sacolaRaw?.produtos.find((e)=>e.variacoesCompradas.map((e1)=>e1.systemId).includes(this.produtoSacola.systemId));
          if(produtoExistente){
            const requestProdutoSacola:Sacola.ProdutoSacolaRequest = {
              ...produtoExistente,
              variacaoAlvo:this.produtoSacola
            }
            this.sacolaContext.addToSacolaForLoja(loja,requestProdutoSacola);
          }
          this.loadingUnitUpdate = false;
        }else{
          this.message.add({
            severity:"error",
            summary:"Não há mais estoque desse produto"
          });
          this.loadingUnitUpdate = false;
        }
      },
      error:(err:HttpErrorResponse)=>{
        this.message.add({
          severity:"error",
          summary:"Erro ao obter estoque"
        })
      }
    });
  }

  decreaseOneUnit(){
    const loja:Pedidos.LojaPedido={
      nome:this.loja.loja,
      slug:this.loja.slug,
      systemId:this.loja.systemId
    }
    this.sacolaContext.removeExactlyOneQuantityOfItemFromSacolaLoja(loja,this.produtoSacola.systemId);
    
  }




}
