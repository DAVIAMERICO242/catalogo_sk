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
  produtoCatalogoId!:string | undefined;
  payloadCadastro!:Catalogo.CadastroModel;
  loadingAdd = false;
  loadingRemove = false;

  constructor(private catalogService:CatalogoService,private auth:UserService,private message:MessageService){}

  ngOnInit(): void {
    this.payloadCadastro = {
      systemId:this.productId,
      lojaSlug:this.lojaSlug || ""
    }
  }

  adicionarAoCatalogo(){
    this.loadingAdd = true;
    this.catalogService.adicionarProduto(this.payloadCadastro).subscribe({
      next:(data)=>{
        this.loadingAdd = false;
        this.produtoCatalogoId = data.systemId;
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
    if(this.produtoCatalogoId){
      this.catalogService.removerProduto(this.produtoCatalogoId).subscribe({
        next:()=>{
          this.loadingRemove = false;
          this.produtoCatalogoId = undefined;
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


}
