import { Component, Input, OnInit } from '@angular/core';
import { Catalogo, CatalogoService } from '../../../services/catalogo.service';
import { UserService } from '../../../services/user.service';
import { SharedModule } from '../../../shared/shared.module';
import { MessageService } from 'primeng/api';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-adicionar-loaded-produto-ao-catalogo',
  imports: [SharedModule],
  templateUrl: './adicionar-loaded-produto-ao-catalogo.component.html'
})
export class AdicionarLoadedProdutoAoCatalogoComponent implements OnInit {
  
  @Input({required:true})
  lojaSlug = "";

  @Input({required:true})
  productId!:string;
  @Input({required:true})
  alreadyOnCatalogo!:boolean;
  payloadCadastro!:Catalogo.CadastroModel;
  payloadDelecao!:Catalogo.DeletarModel;
  loadingAdd = false;
  loadingRemove = false;

  constructor(private catalogService:CatalogoService,private auth:UserService,private message:MessageService){}

  ngOnInit(): void {
    this.payloadCadastro = {
      systemId:this.productId,
      lojaSlug:this.lojaSlug || ""
    }

    this.payloadDelecao = {
      systemId:this.productId,
      lojaSlug:this.lojaSlug || ""
    }
  }

  adicionarAoCatalogo(){
    this.loadingAdd = true;
    this.catalogService.adicionarProduto(this.payloadCadastro).subscribe({
      next:()=>{
        this.loadingAdd = false;
        this.alreadyOnCatalogo = true;
      },error:(erro:HttpErrorResponse)=>{
        this.loadingAdd = false;
        this.message.add({
          severity:"error",
          summary:erro.error
        })
      }
    })
  }

  removerDoCatalogo(){
    this.loadingRemove = true;
    this.catalogService.removerProduto(this.payloadDelecao).subscribe({
      next:()=>{
        this.loadingRemove = false;
        this.alreadyOnCatalogo = false;
      },error:(erro:HttpErrorResponse)=>{
        this.loadingRemove = false;
        this.message.add({
          severity:"error",
          summary:erro.error
        })
      }
    })
  }


}
