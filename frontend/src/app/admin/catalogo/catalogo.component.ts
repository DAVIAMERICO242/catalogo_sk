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

@Component({
  selector: 'app-catalogo',
  imports: [SharedModule, AdminPageTitleComponent, ExcluirProdutoComponent, DescontosComponent,DragDropModule],
  templateUrl: './catalogo.component.html',
  providers:[CatalogoService]
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

  handleExclude(productId:string){
    this.catalogo = this.catalogo.filter((e)=>e.produtoBase.systemId!==productId);
    this.message.add({
      severity:"success",
      summary:"Sucesso"
    })
  }

  drop(event: CdkDragDrop<number[]>) {
      moveItemInArray(this.catalogo, event.previousIndex, event.currentIndex);
      this.catalogoService.reindex(this.selectedLoja.systemId,event.previousIndex,event.currentIndex).subscribe();
  }

}
