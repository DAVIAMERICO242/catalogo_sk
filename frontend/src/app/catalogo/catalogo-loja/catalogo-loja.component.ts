import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Catalogo, CatalogoService } from '../../services/catalogo.service';
import { CatalogoHeaderComponent } from "./catalogo-header/catalogo-header.component";
import { CatalogoContextService } from './catalogo-context.service';
import { Observable } from 'rxjs';
import { SharedModule } from '../../shared/shared.module';
import { CatalogoLojaWrapperComponent } from "./catalogo-loja-wrapper/catalogo-loja-wrapper.component";
import { fadeIn } from '../../animations/fadeIn';
import { CatalogoBannerComponent } from "./catalogo-banner/catalogo-banner.component";
import { BaseProductContainerComponent } from "./product-grid/base-product-container/base-product-container.component";

@Component({
  selector: 'app-catalogo-loja',
  imports: [CatalogoHeaderComponent, SharedModule, CatalogoLojaWrapperComponent, CatalogoBannerComponent, BaseProductContainerComponent,RouterModule],
  templateUrl: './catalogo-loja.component.html',
  providers:[CatalogoContextService],
  animations:[fadeIn]
})
export class CatalogoLojaComponent {
  slug = "";

  constructor(private route:ActivatedRoute,private catalogContext:CatalogoContextService){
  }
  ngOnInit(): void {
    this.slug = this.route.snapshot.paramMap.get("slug") || ""
    this.loadContext();
  }

  loadContext(){
    this.catalogContext.setLoja(this.slug)
    this.catalogContext.setCatalogo(this.slug)
  }

}
