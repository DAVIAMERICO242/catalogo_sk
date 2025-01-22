import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { AdminPageTitleComponent } from '../admin-page-title/admin-page-title.component';
import { Catalogo, CatalogoService } from '../../services/catalogo.service';
import { HttpErrorResponse } from '@angular/common/http';
import { UserService } from '../../services/user.service';
import { ConfirmationDialogComponent } from "../../pure-ui-components/confirmation-dialog/confirmation-dialog.component";
import { ExcluirProdutoComponent } from "./excluir-produto/excluir-produto.component";
import { MessageService } from 'primeng/api';
import { DescontosComponent } from "./descontos/descontos.component";

@Component({
  selector: 'app-catalogo',
  imports: [SharedModule, AdminPageTitleComponent, ExcluirProdutoComponent, DescontosComponent],
  templateUrl: './catalogo.component.html',
  providers:[CatalogoService]
})
export class CatalogoComponent implements OnInit{

  catalogo!:Catalogo.Produto[];
  loadingCatalogo = false;

  constructor(private catalogoService:CatalogoService,private auth:UserService,private message:MessageService){

  }


  ngOnInit(): void {
    this.loadCatalogo();
  }

  loadCatalogo(){
    const slug = this.auth.getContext()?.loja.slug || "";
    this.loadingCatalogo = true;
    this.catalogoService.getCatalogo(slug).subscribe({
      next:(data)=>{
        this.catalogo = data;
        this.loadingCatalogo = false;
      },
      error:(error:HttpErrorResponse)=>{
        this.loadingCatalogo = false;
        alert(error.error);
      }
    })
  }

  handleExclude(productId:string){
    this.catalogo = this.catalogo.filter((e)=>e.produtoBase.systemId!==productId);
    this.message.add({
      severity:"success",
      summary:"Sucesso"
    })
  }

}
