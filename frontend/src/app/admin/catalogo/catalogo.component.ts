import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { AdminPageTitleComponent } from '../admin-page-title/admin-page-title.component';
import { Catalogo, CatalogoService } from '../../services/catalogo.service';
import { HttpErrorResponse } from '@angular/common/http';
import { User, UserService } from '../../services/user.service';
import { ConfirmationDialogComponent } from "../../pure-ui-components/confirmation-dialog/confirmation-dialog.component";
import { ExcluirProdutoComponent } from "./excluir-produto/excluir-produto.component";
import { MessageService } from 'primeng/api';
import { DescontosComponent } from "./descontos/descontos.component";
import { CdkDragDrop, DragDropModule, moveItemInArray } from '@angular/cdk/drag-drop';
import { ProductVariationViewComponent } from "../produtos/product-variation-view/product-variation-view.component";

@Component({
  selector: 'app-catalogo',
  imports: [SharedModule, AdminPageTitleComponent, ExcluirProdutoComponent, DescontosComponent, DragDropModule, ProductVariationViewComponent],
  templateUrl: './catalogo.component.html',
  providers:[CatalogoService],
  styles:[
    `
    .text-vertical-overflow {
      display: -webkit-box;
      -webkit-line-clamp: 3; /* Define o número máximo de linhas */
      -webkit-box-orient: vertical;
      overflow: hidden;
      text-overflow: ellipsis; /* Adiciona os "..." no final */
      height: 30px; /* Ajuste conforme necessário */
    }
    `
  ]
})
export class CatalogoComponent implements OnInit{

  catalogo!:Catalogo.Produto[];
  loadingCatalogo = false;
  selectedLoja!:User.Loja;

  constructor(private catalogoService:CatalogoService,protected auth:UserService,private message:MessageService){

  }


  ngOnInit(): void {
    if(this.auth.getContext()?.role===User.Role.ADMIN){
      const lojasFranquia = this.auth.getContext()?.lojasFranquia
      if(lojasFranquia){
        this.selectedLoja = lojasFranquia[0];
      }
    }else{
      this.selectedLoja = this.auth.getContext()?.loja as User.Loja;
    }
    this.loadCatalogo();
  }

  loadCatalogo(){
    this.loadingCatalogo = true;
    this.catalogoService.getCatalogo(this.selectedLoja.slug).subscribe({
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

  handleExclude(productCatalogoId:string){
    this.catalogo = this.catalogo.filter((e)=>e.systemId!==productCatalogoId);
    this.message.add({
      severity:"success",
      summary:"Sucesso"
    })
  }

  drop(event: CdkDragDrop<number[]>) {// o index vai ta desatualizado no frontend, mas talvez n seja um problema
      moveItemInArray(this.catalogo, event.previousIndex, event.currentIndex);
      this.catalogoService.reindex(this.selectedLoja.systemId,event.previousIndex,event.currentIndex).subscribe();
  }

}
