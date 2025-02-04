import { Component, EventEmitter, Input, Output } from '@angular/core';
import { User } from '../../../services/user.service';
import { BannerModel, BannerService } from '../../../services/banner.service';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-bulk-delete',
  imports: [SharedModule],
  templateUrl: './bulk-delete.component.html'
})
export class BulkDeleteComponent {
  @Input({required:true})
  banners:BannerModel.Banner[] = [];
  @Input({required:true})
  lojas:User.Loja[] = [];
  selectedLojas:User.Loja[] = [];
  open = false;
  loading = false;
  @Output()
  onBulkDelete = new EventEmitter();


  constructor(private bannerService:BannerService){}

  bulkDelete(){
    this.loading = true;
    this.bannerService.bulkDelete(this.banners.filter(e=>this.selectedLojas.map(e=>e.systemId).includes(e.lojaInfo.systemId)).map(e=>e.systemId))
    .subscribe({
      next:()=>{
        this.loading = false;
        this.open = false;
        this.onBulkDelete.emit();
      },
      error:(e:HttpErrorResponse)=>{
        this.loading = false;
        alert(e.error);
      }
    })
  }
}
