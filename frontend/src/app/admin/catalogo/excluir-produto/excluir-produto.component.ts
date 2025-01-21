import { Component } from '@angular/core';
import { ConfirmationDialogComponent } from "../../../pure-ui-components/confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-excluir-produto',
  imports: [ConfirmationDialogComponent],
  templateUrl: './excluir-produto.component.html'
})
export class ExcluirProdutoComponent {
  open = false;
}
