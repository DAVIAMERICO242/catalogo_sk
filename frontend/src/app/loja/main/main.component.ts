import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router, RouterModule } from '@angular/router';
import { Catalogo, CatalogoService } from '../../services/catalogo.service';
import { filter, Observable } from 'rxjs';
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
  isHome = false;

  constructor(private route:ActivatedRoute,private lojaContext:LojaContextService,private router:Router){//so mostra o banner na home
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      const segments = event.urlAfterRedirects.split('/').filter(Boolean);
      this.isHome = segments.length === 1; // Apenas se houver um único segmento na URL
    });
  }

  ngOnInit(): void {
    this.slug = this.route.snapshot.paramMap.get("slug") || ""
    this.loadContext();
  }

  loadContext(){
    this.lojaContext.setLoja(this.slug);
  }

}
