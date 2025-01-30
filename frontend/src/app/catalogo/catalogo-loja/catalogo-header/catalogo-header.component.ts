import { Component, Input, OnInit } from '@angular/core';
import { CatalogoContextService } from '../catalogo-context.service';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-catalogo-header',
  imports: [SharedModule],
  templateUrl: './catalogo-header.component.html'
})
export class CatalogoHeaderComponent implements OnInit {
  loja = "";
  constructor(private catalogContext:CatalogoContextService) {
    
  }
  ngOnInit(): void {
    this.catalogContext.loja$.subscribe((d)=>{
        this.loja = d?.loja || "";
    });
  }


  
}
