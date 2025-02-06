import { Component, OnInit } from '@angular/core';
import { Catalogo, CatalogoService } from '../../services/catalogo.service';
import { SharedModule } from '../../shared/shared.module';
import { ActivatedRoute, Router } from '@angular/router';
import { Produto, ProdutosService } from '../../services/produtos.service';
import { HttpErrorResponse } from '@angular/common/http';
import { LojaContextService } from '../loja-context.service';
import { first, take } from 'rxjs';

@Component({
  selector: 'app-product-page',
  imports: [SharedModule],
  templateUrl: './product-page.component.html',
  styles:[
    `
    .photos::-webkit-scrollbar {
      width: 5px; /* Largura do scrollbar vertical */
      height: 5px;
    }
    .photos::-webkit-scrollbar-thumb{
      background: #dfdada; /* Cor do trilho */
    }
    `
  ]
})
export class ProductPageComponent implements OnInit{

  productId="";
  produto!:Catalogo.Produto;
  photos:string[] = [];
  

  constructor(
    private catalogoService:CatalogoService,
    private lojaContext:LojaContextService,
    private route:ActivatedRoute,
    private produtoService:ProdutosService
  ){}


  ngOnInit(): void {
    console.log(this.productId);
    this.productId = this.route.snapshot.paramMap.get("id") || "";
    this.loadProduct();
  }

  loadProduct(){
    this.catalogoService.getByProdutoCatalogoId(this.productId).subscribe(
      {
        next:(data)=>{
          this.produto = data;
          this.photos = this.produto.produtoBase.variacoes.map((e)=>e.foto);
          this.loadStock();
        },
        error:(err:HttpErrorResponse)=>{
          alert(err.error);
        }
      }
    );
  }


  loadStock(){
    this.lojaContext.lojaSub
    .pipe(take(1))  // Desinscreve automaticamente após encontrar o valor não nulo
    .subscribe((loja) => {
      // Seu código aqui para tratar o valor não nulo
      if(loja){
        this.produtoService.getStock([this.produto.produtoBase.sku],loja.slug).subscribe();
      }
    });
  }

  


}
