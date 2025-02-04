import { Component, Input, OnInit } from '@angular/core';
import { CatalogoContextService } from '../catalogo-context.service';
import { SharedModule } from '../../../shared/shared.module';
import { Router } from '@angular/router';

@Component({
  selector: 'app-catalogo-header',
  imports: [SharedModule],
  templateUrl: './catalogo-header.component.html'
})
export class CatalogoHeaderComponent implements OnInit {
  loja = "";
  slug = ""
  constructor(private catalogContext:CatalogoContextService,private router:Router) {
    
  }
  ngOnInit(): void {
    this.catalogContext.loja$.subscribe((d)=>{
        this.loja = d?.loja || "";
        this.slug = d?.slug || "";
    });
  }

  goToHome(){
    if(this.slug){
      this.router.navigate(["/" + this.slug])
    }
  }


  
}
