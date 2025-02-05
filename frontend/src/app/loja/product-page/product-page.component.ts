import { Component } from '@angular/core';
import { CatalogoService } from '../../services/catalogo.service';
import { SharedModule } from '../../shared/shared.module';

@Component({
  selector: 'app-product-page',
  imports: [SharedModule],
  templateUrl: './product-page.component.html'
})
export class ProductPageComponent {

  constructor(private catalogoService:CatalogoService){}
}
