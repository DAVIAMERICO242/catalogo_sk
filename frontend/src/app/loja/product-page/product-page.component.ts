import { Component, OnInit } from '@angular/core';
import { CatalogoService } from '../../services/catalogo.service';
import { SharedModule } from '../../shared/shared.module';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-product-page',
  imports: [SharedModule],
  templateUrl: './product-page.component.html'
})
export class ProductPageComponent implements OnInit{

  productId="";

  constructor(private catalogoService:CatalogoService,private route:ActivatedRoute){}


  ngOnInit(): void {
    this.productId = this.route.snapshot.paramMap.get("id") || "";
    console.log(this.productId);
    this.catalogoService.getByProdutoCatalogoId(this.productId).subscribe();
  }


}
