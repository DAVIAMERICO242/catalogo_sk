import { Component, Input, OnInit } from '@angular/core';
import { Catalogo, CatalogoService } from '../../../services/catalogo.service';
import { UserService } from '../../../services/user.service';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-adicionar-loaded-produto-ao-catalogo',
  imports: [SharedModule],
  templateUrl: './adicionar-loaded-produto-ao-catalogo.component.html'
})
export class AdicionarLoadedProdutoAoCatalogoComponent implements OnInit {

  @Input({required:true})
  productId!:string;

  payload!:Catalogo.CadastroModel;

  constructor(private catalogService:CatalogoService,private auth:UserService){}

  ngOnInit(): void {
    this.payload = {
      systemId:this.productId,
      lojaSlug:this.auth.getContext()?.loja.slug || ""
    }
  }


}
