import { Component } from '@angular/core';
import { Catalogo } from '../../../services/catalogo.service';
import { ActivatedRoute } from '@angular/router';
import { CatalogoContextService } from '../catalogo-context.service';
import { fadeIn } from '../../../animations/fadeIn';
import { CatalogoHeaderComponent } from '../catalogo-header/catalogo-header.component';
import { CatalogoLojaWrapperComponent } from '../catalogo-loja-wrapper/catalogo-loja-wrapper.component';
import { CatalogoBannerComponent } from '../catalogo-banner/catalogo-banner.component';
import { BaseProductContainerComponent } from './base-product-container/base-product-container.component';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-product-grid',
  imports: [CatalogoHeaderComponent, SharedModule, CatalogoLojaWrapperComponent, CatalogoBannerComponent, BaseProductContainerComponent],
  templateUrl: './product-grid.component.html',
  providers:[CatalogoContextService],
  animations:[fadeIn]
})
export class ProductGridComponent {
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
