import { Component, OnInit } from '@angular/core';
import { ProdutosService } from '../../services/produtos.service';
import { Subscription } from 'rxjs';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-produtos',
  imports: [],
  templateUrl: './produtos.component.html'
})
export class ProdutosComponent implements OnInit{

  subscriptions = new Subscription();
  page = 0;

  constructor(private produtoService:ProdutosService,private userService:UserService){}

  ngOnInit(): void {
    const franquiaId = this.userService.getContext()?.franquia.systemId;
    if(franquiaId){
      this.produtoService.setProdutosPaged(this.page,franquiaId);
    }
  }


}
