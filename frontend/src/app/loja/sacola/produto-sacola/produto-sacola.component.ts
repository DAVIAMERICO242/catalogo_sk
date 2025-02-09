import { Component, Input, OnInit } from '@angular/core';
import { Sacola, SacolaService } from '../../../services/sacola.service';
import { Loja } from '../../../services/loja.service';
import { Produto, ProdutosService } from '../../../services/produtos.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { Pedidos } from '../../../services/pedidos.service';
import { SharedModule } from '../../../shared/shared.module';
import { ProdutoPrecoComponent } from '../../produto-preco/produto-preco.component';
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
  produtoSacola!:Sacola.ProdutoCatalogoModel;//VARIACAO/pra usar o componente que abstrai descontos
  @Input({required:true})
  variacaoSacola!:Sacola.ProdutoVariacaoModel;
  loadingUnitUpdate = false;

  constructor(private sacolaContext:SacolaUiContextService,private produtoService:ProdutosService,private message:MessageService){}
  ngOnInit(): void {
  }

  increaseOneUnit(){
    this.loadingUnitUpdate = true;
    this.produtoService.getVariationStock(this.variacaoSacola.sku,this.loja.slug).subscribe({
      next:(data)=>{
        if(data.estoque>=(this.variacaoSacola.quantidade + 1)){
          this.sacolaContext.addToSacolaForLoja(this.loja,this.produtoSacola,this.variacaoSacola);
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
    this.sacolaContext.removeExactlyOneQuantityOfItemFromSacolaLoja(this.loja,this.variacaoSacola.systemId);
    
  }




}
