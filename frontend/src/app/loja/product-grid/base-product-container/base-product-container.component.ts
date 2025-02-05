import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Produto } from '../../../services/produtos.service';
import { Catalogo } from '../../../services/catalogo.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-base-product-container',
  imports: [SharedModule],
  templateUrl: './base-product-container.component.html'
})
export class BaseProductContainerComponent {
  @Input({required:true})
  produto!:Catalogo.Produto | undefined;


  constructor(private router:Router,private route: ActivatedRoute) {}
  

  goToProductPage(){
    if(this.produto){
      this.router.navigate(["produto-catalogo/" + this.produto.systemId],{relativeTo:this.route})
    }
  }
}
