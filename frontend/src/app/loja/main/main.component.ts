import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Catalogo, CatalogoService } from '../../services/catalogo.service';
import { Observable } from 'rxjs';
import { SharedModule } from '../../shared/shared.module';
import { fadeIn } from '../../animations/fadeIn';
import { BaseProductContainerComponent } from "../product-grid/base-product-container/base-product-container.component";
import { HeaderComponent } from '../header/header.component';
import { MainWrapperComponent } from '../main-wrapper/main-wrapper.component';
import { BannerComponent } from '../banner/banner.component';
import { LojaContextService } from '../loja-context.service';


@Component({
  selector: 'app-main',
  imports: [HeaderComponent, SharedModule, MainWrapperComponent, BannerComponent, BaseProductContainerComponent,RouterModule],
  templateUrl: './main.component.html',
  providers:[LojaContextService],
  animations:[fadeIn]
})
export class LojaComponent {
  slug = "";

  constructor(private route:ActivatedRoute,private lojaContext:LojaContextService){
  }
  ngOnInit(): void {
    this.slug = this.route.snapshot.paramMap.get("slug") || ""
    this.loadContext();
  }

  loadContext(){
    this.lojaContext.setLoja(this.slug)
    this.lojaContext.setCatalogo(this.slug)
  }

}
