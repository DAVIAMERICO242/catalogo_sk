import { Component, OnInit } from '@angular/core';
import { ProdutosService } from '../../services/produtos.service';
import { Subscription } from 'rxjs';
import { UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { ProductVariationViewComponent } from "./product-variation-view/product-variation-view.component";

@Component({
  selector: 'app-produtos',
  imports: [SharedModule, AdminPageTitleComponent, ProductVariationViewComponent],
  templateUrl: './produtos.component.html'
})
export class ProdutosComponent implements OnInit{

  subscriptions = new Subscription();
  page = 0;

  constructor(protected produtoService:ProdutosService,private userService:UserService){}

  ngOnInit(): void {
    const franquiaId = this.userService.getContext()?.franquia.systemId;
    if(franquiaId){
      this.produtoService.setProdutosPaged(this.page,franquiaId);
    }
  }


}
