import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Produto } from '../../../services/produtos.service';
import { Catalogo } from '../../../services/catalogo.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-base-product-container',
  imports: [CommonModule],
  templateUrl: './base-product-container.component.html'
})
export class BaseProductContainerComponent {
  @Input({required:true})
  produto!:Catalogo.Produto;

  constructor(private router:Router,private route: ActivatedRoute) {}
  

  goToProductPage(){
    this.router.navigate(["produto-catalogo/" + this.produto.systemId],{relativeTo:this.route})
  }
}
