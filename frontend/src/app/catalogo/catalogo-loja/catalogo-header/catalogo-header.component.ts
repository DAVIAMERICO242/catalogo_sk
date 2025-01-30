import { Component, Input } from '@angular/core';
import { CatalogoContextService } from '../catalogo-context.service';

@Component({
  selector: 'app-catalogo-header',
  imports: [],
  templateUrl: './catalogo-header.component.html'
})
export class CatalogoHeaderComponent {
  loja = "";
  constructor(private catalogContext:CatalogoContextService) {
    
  }
  
}
