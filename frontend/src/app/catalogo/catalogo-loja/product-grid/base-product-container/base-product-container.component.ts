import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { Produto } from '../../../../services/produtos.service';
import { Catalogo } from '../../../../services/catalogo.service';

@Component({
  selector: 'app-base-product-container',
  imports: [CommonModule],
  templateUrl: './base-product-container.component.html'
})
export class BaseProductContainerComponent {
  @Input({required:true})
  produto!:Catalogo.Produto;
}
