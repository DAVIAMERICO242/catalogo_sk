import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { User } from '../../../services/user.service';
import { BannerModel, BannerService } from '../../../services/banner.service';
import { HttpClient } from '@angular/common/http';
import { ConfirmationDialogComponent } from "../../../pure-ui-components/confirmation-dialog/confirmation-dialog.component";

@Component({
  selector: 'app-bulk-banner-replication',
  imports: [SharedModule, ConfirmationDialogComponent],
  templateUrl: './bulk-banner-replication.component.html'
})
export class BulkBannerReplicationComponent implements OnInit {
  @Input({required:true})
  desiredIndex = 0;
  @Input({required:true})
  lojas!:User.Loja[];
  selectedLojas:User.Loja[] = [];
  @Input({required:true})
  stringOrBase64Desktop:string|undefined = "";
  @Input({required:true})
  stringOrBase64Mobile:string|undefined  = "";
  sureMobile64 = "";
  sureDesktop64 = "";
  payload:BannerModel.Banner[] = [];
  @Output()
  onBulk = new EventEmitter<void>();
  loadingBulk = false;
  openLojaSelector = false;

  openConfirm = false;

  constructor(private http:HttpClient,private bannerService:BannerService){}
  
  ngOnInit(): void {
  }

  definePayload(){
    const promises: Promise<void>[] = [];
    if(this.stringOrBase64Desktop){
      const dp = this.convertImageToBase64(this.stringOrBase64Desktop || "").then((d)=>{
        this.sureDesktop64 = d;
      })
      promises.push(dp);
    }
    if(this.stringOrBase64Mobile){
      const mp = this.convertImageToBase64(this.stringOrBase64Mobile || "").then((d)=>{
        this.sureMobile64 = d;
      })
      promises.push(mp);
    }
    Promise.all(promises).then(()=>{
      this.selectedLojas.forEach((e)=>{
        let bannerMobileExtension = this.stringOrBase64Mobile?.split('.').pop();
        let bannerDesktopExtension = this.stringOrBase64Desktop?.split('.').pop();
        if(!bannerDesktopExtension && !bannerMobileExtension){
          return;
        }
        const media = [];
        if(bannerMobileExtension){
          media.push({
            bannerExtension:bannerMobileExtension,
            bannerUrl:"",
            window:BannerModel.WindowContext.MOBILE,
            base64:this.sureMobile64
          })
        }
        if(bannerDesktopExtension){
          media.push({
            bannerExtension:bannerDesktopExtension,
            bannerUrl:"",
            window:BannerModel.WindowContext.DESKTOP,
            base64:this.sureDesktop64
          })
        }
        this.payload.push({
          systemId:"",
          lojaInfo:{
            index:this.desiredIndex,
            systemId:e.systemId
          },
          media
        })
      });
    this.loadingBulk = true
    this.bannerService.bulkBanner(this.payload).subscribe({
        next:()=>{
          this.loadingBulk = false;
          this.openLojaSelector = false;
          this.openConfirm = false;
          this.onBulk.emit();
        }
      })
    })
  }


  postBanners(){
    this.definePayload();
  }

  convertImageToBase64(imageUrl: string): Promise<string> {
    return new Promise((resolve, reject) => {
      this.http.get(imageUrl, { responseType: 'blob' }).subscribe({
        next: (blob) => {
          const reader = new FileReader();
          reader.readAsDataURL(blob);
          reader.onloadend = () => {
            resolve(reader.result as string);
          };
          reader.onerror = reject;
        },
        error: reject,
      });
    });
  }

}
