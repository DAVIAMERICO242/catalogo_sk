import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ConfirmationDialogComponent } from "../../../pure-ui-components/confirmation-dialog/confirmation-dialog.component";
import { BannerService } from '../../../services/banner.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService } from 'primeng/api';

export interface DeleteNotification{
  bannerId:string,
  lojaId:string,
  isMobile:boolean
}
@Component({
  selector: 'app-deletar-banner',
  imports: [ConfirmationDialogComponent],
  templateUrl: './deletar-banner.component.html'
})
export class DeletarBannerComponent {
  @Input({required:true})
  bannerId = "";
  @Input({required:true})
  lojaId = "";
  @Input({required:true})
  isMobile = false;
  open = false;
  loading = false;
  @Output()
  onDisassociation = new EventEmitter<void>();

  constructor(private bannerService:BannerService,private message:MessageService){}

  deletarBanner(){
    this.loading = true;
    this.bannerService.desassociarBannerLoja(this.bannerId,this.isMobile).subscribe({
      next:()=>{
        this.loading = false;
        this.open = false;
        this.onDisassociation.emit();
      },
      error:(err:HttpErrorResponse)=>{
        this.loading = false;
        this.message.add({
          severity:"error",
          summary:"Erro ao excluir banner",
          detail:err.error
        })
      }
    })
  }
}
