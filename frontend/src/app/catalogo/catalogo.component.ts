import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { Loja, LojaService } from '../services/loja.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-catalogo',
  imports: [SharedModule],
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
    if(!this.isFranquia){
      this.lojaService.getLojasMatriz().subscribe({
        next:(data)=>{
          this.lojas = data;
        }
      })
    }else{
      this.lojaService.getLojasFranquia().subscribe({
        next:(data)=>{
          this.lojas = data;
        }
      })
    }
  }

  goToCatalogo(slug:string){
    this.router.navigate(["/" + slug])
  }
}
