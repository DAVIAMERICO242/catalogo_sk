import { Component, Input, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { Router } from '@angular/router';
import { LojaContextService } from '../loja-context.service';
import { SacolaComponent } from "../sacola/sacola.component";

@Component({
  selector: 'app-header',
  imports: [SharedModule, SacolaComponent],
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  loja = "";
  slug = ""
  constructor(private lojaContext:LojaContextService,private router:Router) {
    
  }
  ngOnInit(): void {
    this.lojaContext.loja$.subscribe((d)=>{
        this.loja = d?.loja || "";
        this.slug = d?.slug || "";
    });
  }

  goToHome(){
    if(this.slug){
      this.router.navigate(["/" + this.slug])
    }
  }


  isCheckout(){
    return window.location.href.endsWith("/checkout");
  }


  
}
