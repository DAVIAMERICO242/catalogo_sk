import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Catalogo, CatalogoService } from '../../services/catalogo.service';
import { CatalogoHeaderComponent } from "./catalogo-header/catalogo-header.component";
import { CatalogoContextService } from './catalogo-context.service';
import { Observable } from 'rxjs';
import { SharedModule } from '../../shared/shared.module';
import { CatalogoLojaWrapperComponent } from "./catalogo-loja-wrapper/catalogo-loja-wrapper.component";
import { fadeIn } from '../../animations/fadeIn';
import { CatalogoBannerComponent } from "./catalogo-banner/catalogo-banner.component";
import { BaseProductContainerComponent } from "./base-product-container/base-product-container.component";

@Component({
  selector: 'app-catalogo-loja',
  imports: [CatalogoHeaderComponent, SharedModule, CatalogoLojaWrapperComponent, CatalogoBannerComponent, BaseProductContainerComponent],
  templateUrl: './catalogo-loja.component.html',
  providers:[CatalogoContextService],
  animations:[fadeIn]
})
export class CatalogoLojaComponent implements OnInit {
  slug = "";
  produtos!:Catalogo.Produto[] | undefined;
  produtosFiltrados!:Catalogo.Produto[] | undefined; 
  filter = "";


  constructor(private route:ActivatedRoute,private catalogContext:CatalogoContextService){
  }
  ngOnInit(): void {
    this.slug = this.route.snapshot.paramMap.get("slug") || ""
    console.log(this.slug)
    this.loadContext();
    this.catalogContext.produtos$.subscribe((data)=>{
      this.produtos = data;
      this.produtosFiltrados = [...this.produtos as Catalogo.Produto[]]
    })
  }

  loadContext(){
    this.catalogContext.setLoja(this.slug)
    this.catalogContext.setCatalogo(this.slug)
  }

  filtrar(){
    this.produtosFiltrados = this.produtos?.filter((e)=>e.produtoBase.descricao.toLowerCase().includes(this.filter.toLowerCase()))
  }

}
