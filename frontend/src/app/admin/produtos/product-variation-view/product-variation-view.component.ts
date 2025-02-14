import { Component, Input } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { Produto, ProdutosService } from '../../../services/produtos.service';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../../services/user.service';


@Component({
  selector: 'app-product-variation-view',
  imports: [SharedModule],
  templateUrl: './product-variation-view.component.html'
})
export class ProductVariationViewComponent {
  @Input({required:true})
  lojaSlug = "";
  @Input()
  buttonText = "";
  variacoes!:Produto.ProdutoVariacao[];
  @Input({required:true})
  skuBase!:string;
  estoque!:Produto.ProdutoEstoque;
  loading = false;
  openVariacao = false;
  @Input({required:true})
  productId!:string;
  estoquesUI:{[key:string]:number}={};
  
  constructor(private produtoService:ProdutosService,private auth:UserService){}



  getVariacao(){
    if(this.variacoes){
      this.openVariacao = true;
    }else{
      this.loading = true;
      this.produtoService.getProductVariation(this.productId).subscribe({
         next:(data)=>{
          this.variacoes = data;
          this.getStock();
         },
         error:(erro:HttpErrorResponse)=>{
          alert(erro.error)
         }
      })
    }
  }

  getStock(){
    if(this.estoque){
      return;
    }
    this.loading = true;
    const sku = [this.skuBase];
    this.produtoService.getStock(sku,this.lojaSlug).subscribe({
       next:(data)=>{
        this.estoque = data[0];
        this.loading = false;
        this.estoque.estoque.forEach((e)=>{
              const keys = Object.keys(this.estoquesUI || {});
                if(!keys.includes(e.sku) && this.variacoes.map(e=>e.sku).includes(e.sku)){
                  this.estoquesUI[e.sku] = e.estoque;
                }
        })
        const skusEstoque = this.variacoes.map(e=>e.sku);
        skusEstoque.map((e)=>{
          const keys = Object.keys(this.estoquesUI || {});
          if(!keys.includes(e)){
            this.estoquesUI[e] = 0;
          }
        })
        this.openVariacao = true;
       },
       error:(erro:HttpErrorResponse)=>{
        this.loading = false;
        this.openVariacao = true;
        alert("Erro ao carregar estoque")
       }
    })
  }



}
