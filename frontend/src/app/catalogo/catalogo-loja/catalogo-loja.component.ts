import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CatalogoService } from '../../services/catalogo.service';

@Component({
  selector: 'app-catalogo-loja',
  imports: [],
  templateUrl: './catalogo-loja.component.html'
})
export class CatalogoLojaComponent implements OnInit {

  constructor(private route:ActivatedRoute,private catalogService:CatalogoService){

  }
  ngOnInit(): void {
    const routeSlug = this.route.snapshot.paramMap.get("slug")
    console.log(routeSlug)
    this.catalogService.getCatalogo(routeSlug as string).subscribe();
  }

}
