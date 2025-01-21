import { Component, OnDestroy, OnInit } from '@angular/core';
import { ProdutosService } from '../../services/produtos.service';
import { Subscription } from 'rxjs';
import { UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { ProductVariationViewComponent } from "./product-variation-view/product-variation-view.component";
import { PaginatorComponent } from "../../pure-ui-components/paginator/paginator.component";
import { AdicionarLoadedProdutoAoCatalogoComponent } from "./adicionar-loaded-produto-ao-catalogo/adicionar-loaded-produto-ao-catalogo.component";

@Component({
  selector: 'app-produtos',
  imports: [SharedModule, AdminPageTitleComponent, ProductVariationViewComponent, PaginatorComponent, AdicionarLoadedProdutoAoCatalogoComponent],
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
    this.loadProdutos();
    
  }


  loadProdutos(){
    const slug = this.userService.getContext()?.loja.slug;
    if(slug){
      this.produtoService.setProdutosPaged(slug,this.nomeFilter,this.skuFilter);
    }
  }
  
  ngOnDestroy(): void {
    
  }

  onPageChange(page:number){
    this.produtoService.changePageContext(page);
    this.loadProdutos();
  }

  clearFilter(){
    this.nomeFilter = "";
    this.skuFilter = "";
    this.produtoService.changePageContext(0);
    this.loadProdutos();
  }

  applyFilters(){
    this.produtoService.changePageContext(0);
    this.loadProdutos();
  }


}
