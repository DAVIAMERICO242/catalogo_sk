import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { Loja, LojaService } from '../services/loja.service';
import { Router } from '@angular/router';
import { GoogleMapsComponent } from "./google-maps/google-maps.component";

@Component({
  selector: 'app-catalogo',
  imports: [SharedModule, GoogleMapsComponent],
  templateUrl: './catalogo.component.html'
})
export class CatalogoComponent implements OnInit {

  isFranquia=false;
  lojas:Loja.Loja[] = []
  loadingLojas = false;
  constructor(private lojaService:LojaService,private router:Router){}
  ngOnInit(): void {
    if(window.location.href.endsWith("franquia")){
      this.isFranquia = true
    }
    this.loadLojas();
  }

  loadLojas(){
    this.loadingLojas = true
    if(!this.isFranquia){
      this.lojaService.getLojasMatriz().subscribe({
        next:(data)=>{
          this.lojas = data;
          this.loadingLojas = false
        }
      })
    }else{
      this.lojaService.getLojasFranquia().subscribe({
        next:(data)=>{
          this.lojas = data;
          this.loadingLojas = false
        }
      })
    }
  }

  goToCatalogo(slug: string): void {
    const url = '/' + slug;  // Construct the URL from the slug
    window.open(url, '_blank');  // Open the URL in a new tab
  }
}
