import { Component } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-catalogo-banner',
  imports: [SharedModule],
  templateUrl: './catalogo-banner.component.html',
  styleUrls:['./catalogo-banner.component.css']
})
export class CatalogoBannerComponent {
  banners = [
    "https://skyler.com.br/wp-content/uploads/2023/09/banner_1920-tyt-desconto-jpg.webp",
    "https://skyler.com.br/wp-content/uploads/2023/09/banner_1920-tyt-desconto-jpg.webp",
    "https://skyler.com.br/wp-content/uploads/2023/09/banner_1920-tyt-desconto-jpg.webp"
  ]
}
