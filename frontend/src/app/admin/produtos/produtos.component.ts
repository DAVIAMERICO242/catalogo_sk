import { Component, OnDestroy, OnInit } from '@angular/core';
import { ProdutosService } from '../../services/produtos.service';
import { Subscription } from 'rxjs';
import { UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { ProductVariationViewComponent } from "./product-variation-view/product-variation-view.component";
import { PaginatorComponent } from "../../pure-ui-components/paginator/paginator.component";

@Component({
  selector: 'app-produtos',
  imports: [SharedModule, AdminPageTitleComponent, ProductVariationViewComponent, PaginatorComponent],
  templateUrl: './produtos.component.html'
})
export class ProdutosComponent implements OnInit,OnDestroy{

  subscriptions = new Subscription();
  nomeFilter = "";
  skuFilter = "";

  constructor(protected produtoService:ProdutosService,private userService:UserService){}
  
  ngOnInit(): void {
    this.nomeFilter = this.produtoService.filterSub.value.nome || "";
    this.skuFilter = this.produtoService.filterSub.value.sku || "";
    if(!this.produtoService.produtosSub.value){
      this.loadProdutos();
    }
  }


  loadProdutos(){
    const franquiaId = this.userService.getContext()?.franquia.systemId;
    if(franquiaId){
      this.produtoService.setProdutosPaged(franquiaId,this.nomeFilter,this.skuFilter);
    }
  }
  
  ngOnDestroy(): void {
    console.log("Destroyed");
  }

  onPageChange(page:number){
    this.produtoService.changePageContext(page);
    this.loadProdutos();
  }

  clearFilter(){
    this.nomeFilter = "";
    this.skuFilter = "";
    this.loadProdutos();
  }


}
