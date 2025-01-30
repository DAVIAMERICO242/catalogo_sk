import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CatalogoService } from '../../services/catalogo.service';
import { CatalogoHeaderComponent } from "./catalogo-header/catalogo-header.component";
import { CatalogoContextService } from './catalogo-context.service';

@Component({
  selector: 'app-catalogo-loja',
  imports: [CatalogoHeaderComponent],
  templateUrl: './catalogo-loja.component.html',
  providers:[CatalogoContextService]
})
export class CatalogoLojaComponent implements OnInit {
  slug = "";

  constructor(private route:ActivatedRoute,private catalogContext:CatalogoContextService){

  }
  ngOnInit(): void {
    this.slug = this.route.snapshot.paramMap.get("slug") || ""
    console.log(this.slug)
    this.loadContext();
  }

  loadContext(){
    this.catalogContext.setLoja(this.slug)
    this.catalogContext.setCatalogo(this.slug)
  }

}
