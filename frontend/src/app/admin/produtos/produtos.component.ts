import { Component, OnDestroy, OnInit } from '@angular/core';
import { ProdutosService } from '../../services/produtos.service';
import { Subscription } from 'rxjs';
import { User, UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { ProductVariationViewComponent } from "./product-variation-view/product-variation-view.component";
import { PaginatorComponent } from "../../pure-ui-components/paginator/paginator.component";
import { AdicionarLoadedProdutoAoCatalogoComponent } from "./adicionar-loaded-produto-ao-catalogo/adicionar-loaded-produto-ao-catalogo.component";
import { Loja } from '../../services/loja.service';

@Component({
  selector: 'app-produtos',
  imports: [SharedModule, AdminPageTitleComponent, ProductVariationViewComponent, PaginatorComponent, AdicionarLoadedProdutoAoCatalogoComponent],
  templateUrl: './produtos.component.html'
})
export class ProdutosComponent implements OnInit,OnDestroy{

  subscriptions = new Subscription();
  nomeFilter = "";
  skuFilter = "";
  selectedLoja!:User.Loja;

  constructor(protected produtoService:ProdutosService,protected userService:UserService){}
  
  ngOnInit(): void {
    this.nomeFilter = this.produtoService.filterSub.value.nome || "";
    this.skuFilter = this.produtoService.filterSub.value.sku || "";
    if(this.userService.getContext()?.role===User.Role.ADMIN){
      const lojasFranquia = this.userService.getContext()?.lojasFranquia
      if(lojasFranquia){
        this.selectedLoja = lojasFranquia[0];
      }
    }else{
      this.selectedLoja = this.userService.getContext()?.loja as User.Loja;
    }
    this.loadProdutos();
    
  }


  loadProdutos(){
    if(this.selectedLoja){
      this.produtoService.setProdutosPaged(this.selectedLoja.slug,this.nomeFilter,this.skuFilter);
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
