import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CorreiosFranquiasContext, CorreiosFranquiasContextService } from '../../../../services/correios-franquias-context.service';
import { SharedModule } from '../../../../shared/shared.module';
import { HttpErrorResponse } from '@angular/common/http';
import { ConfirmationDialogComponent } from "../../../../pure-ui-components/confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-deletar-faixa',
  imports: [SharedModule, ConfirmationDialogComponent],
  templateUrl: './deletar-faixa.component.html'
})
export class DeletarFaixaComponent {
  loading = false;
  open = false;
  @Input()
  faixa!:CorreiosFranquiasContext.FaixaCep;
  @Output()
  onDelete = new EventEmitter<string>();

  constructor(private franquiaCorreiosContext:CorreiosFranquiasContextService) {
  }
  


  deletar(){
    this.loading = true;
    this.franquiaCorreiosContext.deletarFaixa(this.faixa.systemId).subscribe({
      next:()=>{
        this.onDelete.emit(this.faixa.systemId);
      },
      error:(e:HttpErrorResponse)=>{
        alert(e.error);
      }
    })
  }

}
