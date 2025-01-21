import { Component, EventEmitter, Input, Output } from '@angular/core';
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

  @Output()
  openChange = new EventEmitter<boolean>();
  
  @Output()
  onConfirmation = new EventEmitter<void>();


  close(){
    this.openChange.emit(false);
    this.open = false;
  }

  emitConfirmation(){
    this.onConfirmation.emit();
  }
}
