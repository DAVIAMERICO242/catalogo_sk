import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ConfirmationDialogComponent } from "../../../pure-ui-components/confirmation-dialog/confirmation-dialog.component";
import { Catalogo, CatalogoService } from '../../../services/catalogo.service';

@Component({
  selector: 'app-excluir-produto',
  imports: [ConfirmationDialogComponent],
  templateUrl: './excluir-produto.component.html'
})
export class ExcluirProdutoComponent {
  open = false;
  loading = false;
  @Input({required:true})
  payload!:Catalogo.DeletarModel;
  @Output()
  onExclude = new EventEmitter<string>();

  constructor(private catalogoService:CatalogoService){

  }

  excluir(){
    this.loading = true;
    this.catalogoService.removerProduto(this.payload).subscribe({
      next:()=>{
        this.loading = false;
        this.open = false;
        this.onExclude.emit(this.payload.systemId);
      },
      error:()=>{
        alert("Erro")
      }
    })
  }
}
