import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Catalogo, CatalogoService } from '../../services/catalogo.service';
import { CatalogoHeaderComponent } from "./catalogo-header/catalogo-header.component";
import { CatalogoContextService } from './catalogo-context.service';
import { Observable } from 'rxjs';
import { SharedModule } from '../../shared/shared.module';
import { CatalogoLojaWrapperComponent } from "./catalogo-loja-wrapper/catalogo-loja-wrapper.component";

@Component({
  selector: 'app-catalogo-loja',
  imports: [CatalogoHeaderComponent, SharedModule, CatalogoLojaWrapperComponent],
  templateUrl: './catalogo-loja.component.html',
  providers:[CatalogoContextService]
})
export class CatalogoLojaComponent implements OnInit {
  slug = "";
  produtos$!:Observable<Catalogo.Produto[] | undefined>;


  constructor(private route:ActivatedRoute,private catalogContext:CatalogoContextService){
  }
  ngOnInit(): void {
    this.slug = this.route.snapshot.paramMap.get("slug") || ""
    console.log(this.slug)
    this.loadContext();
    this.produtos$ = this.catalogContext.produtos$;
  }

  loadContext(){
    this.catalogContext.setLoja(this.slug)
    this.catalogContext.setCatalogo(this.slug)
  }

}
