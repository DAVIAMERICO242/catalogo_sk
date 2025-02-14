import { Component, Inject, Input, OnInit, PLATFORM_ID } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { Router } from '@angular/router';
import { LojaContextService } from '../loja-context.service';
import { SacolaComponent } from "../sacola/sacola.component";
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [SharedModule, SacolaComponent],
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {
  loja = "";
  slug = ""
  constructor(private lojaContext:LojaContextService,private router:Router,@Inject(PLATFORM_ID) private platformId: Object) {
    
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
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }
    return window.location.href.endsWith("/checkout");
  }


  
}
