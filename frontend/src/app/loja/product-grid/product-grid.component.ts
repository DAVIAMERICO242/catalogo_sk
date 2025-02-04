import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import {MainWrapperComponent } from '../main-wrapper/main-wrapper.component';

import { BaseProductContainerComponent } from './base-product-container/base-product-container.component';
import { HeaderComponent } from '../header/header.component';
import { SharedModule } from '../../shared/shared.module';
import { BannerComponent } from '../banner/banner.component';
import { LojaContextService } from '../loja-context.service';
import { fadeIn } from '../../animations/fadeIn';
import { Catalogo } from '../../services/catalogo.service';


@Component({
  selector: 'app-product-grid',
  imports: [ SharedModule, BaseProductContainerComponent],
  templateUrl: './product-grid.component.html',
  animations:[fadeIn]
})
export class ProductGridComponent {
  slug = "";
  produtos!:Catalogo.Produto[] | undefined;
  produtosFiltrados!:Catalogo.Produto[] | undefined; 
  filter = "";


  constructor(private route:ActivatedRoute,private lojaContext:LojaContextService){
  }
  ngOnInit(): void {
    this.slug = this.route.snapshot.paramMap.get("slug") || ""
    console.log(this.slug)
    this.loadContext();
    this.lojaContext.produtos$.subscribe((data)=>{
      if(data){
        this.produtos = data;
        this.produtosFiltrados = [...this.produtos as Catalogo.Produto[]]
      }
    })
  }

  loadContext(){
    this.lojaContext.setLoja(this.slug)
    this.lojaContext.setCatalogo(this.slug)
  }

  filtrar(){
    this.produtosFiltrados = this.produtos?.filter((e)=>e.produtoBase.descricao.toLowerCase().includes(this.filter.toLowerCase()))
  }
}
