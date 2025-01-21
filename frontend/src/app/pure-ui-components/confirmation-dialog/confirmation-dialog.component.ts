import { Component, EventEmitter, Input } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';

@Component({
  selector: 'app-confirmation-dialog',
  imports: [ButtonModule,DialogModule],
  templateUrl: './confirmation-dialog.component.html'
})
export class ConfirmationDialogComponent {
  @Input()
  confirmationTitle = "Tem certeza?";

  @Input({required:true})
  loading = false;

  @Input({required:true})
  open = false;

  onConfirmation = new EventEmitter<void>();
}
