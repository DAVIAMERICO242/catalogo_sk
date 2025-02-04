import { Component, OnInit } from '@angular/core';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { User, UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';
import { MessageService } from 'primeng/api';
import { BannerModel, BannerService } from '../../services/banner.service';
import { DeletarBannerComponent, DeleteNotification } from "./deletar-banner/deletar-banner.component";
import { BulkBannerReplicationComponent } from "./bulk-banner-replication/bulk-banner-replication.component";
import { MediaService } from '../../services/media-service';
import {CdkDragDrop, CdkDrag, CdkDropList, moveItemInArray, DragDropModule} from '@angular/cdk/drag-drop';


@Component({
  selector: 'app-banners',
  imports: [AdminPageTitleComponent, SharedModule, DeletarBannerComponent, BulkBannerReplicationComponent,DragDropModule],
  templateUrl: './banners.component.html'
})
export class BannersComponent implements OnInit {
  lojas!:User.Loja[];
  banners:BannerModel.Banner[] = [];
  loadingBanners = false;
  loadingBannerForIndex:boolean[] = [];
  allowedIndexes = [0,1,2,3,4];
  constructor(
    private mediaService:MediaService,
    private bannerService:BannerService,
    protected auth:UserService,
    private message:MessageService
  ){}
  ngOnInit(): void {
    if(this.auth.getContext()?.role===User.Role.ADMIN){
      const lojas = this.auth.getContext()?.lojasFranquia
      if(lojas){
        this.lojas = lojas;
      }
    }else{
      const loja = this.auth.getContext()?.loja;
      if(loja){
        this.lojas = [loja]
      }
    }
    this.loadBanners();
  }


  loadBanners(){
    this.loadingBanners = true;
    const franquiaId = this.auth.getContext()?.franquia.systemId;
    if(this.auth.getContext()?.role===User.Role.ADMIN){
      if(franquiaId){
        this.bannerService.getBanners(franquiaId).subscribe((data)=>{
          this.loadingBanners = false;
          this.banners = data;
          this.lojas.forEach((e)=>{
            this.allowedIndexes.forEach((i)=>{
              const hasBannerForIndex = this.banners.find((e1)=>e1.lojaInfo.systemId===e.systemId && e1.lojaInfo.index===i);
              if(!hasBannerForIndex){
                this.banners.push({
                  systemId:"",
                  media:[],
                  lojaInfo:{
                    index:i,
                    systemId:e.systemId
                  }
                })
              }
            });
          });
        });
      }
    }else{
      const lojaId = this.auth.getContext()?.loja.systemId;
      if(lojaId && franquiaId){
        this.bannerService.getBanners(franquiaId,lojaId).subscribe((data)=>{
          this.loadingBanners = false;
          this.banners = data;
          this.lojas.forEach((e)=>{
            this.allowedIndexes.forEach((i)=>{
              const hasBannerForIndex = this.banners.find((e1)=>e1.lojaInfo.systemId===e1.systemId && e1.lojaInfo.index===i);
              if(!hasBannerForIndex){
                this.banners.push({
                  systemId:"",
                  media:[],
                  lojaInfo:{
                    index:i,
                    systemId:e.systemId
                  }
                })
              }
            });
          });
        });
      }
    }
  }

  drop(event: CdkDragDrop<number[]>,lojaId:string) {
    let backup  = this.getBannersForLoja(lojaId);
    moveItemInArray(backup, event.previousIndex, event.currentIndex);
    backup = backup.map((e,i)=>{
      return {
        ...e,
        lojaInfo:{
          systemId:lojaId,
          index:i
        }
      }
    });
    this.banners = this.banners.map((e)=>{
      if(e.lojaInfo.systemId===lojaId){
        const found = backup.find((e1)=>e1.lojaInfo.systemId===lojaId && e1.lojaInfo.index===e.lojaInfo.index);
        return found as BannerModel.Banner;
      }else{
        return e;
      }
    });
    console.log(this.banners);
  }

  getBannersForLoja(lojaId:string){
    return this.banners.filter((e)=>e.lojaInfo.systemId===lojaId).sort((a,b)=>a.lojaInfo.index-b.lojaInfo.index);
  }

  onBannerChange(event:Event,lojaId:string,index:number,isMobile:boolean){
    const input = event.target as HTMLInputElement;
    if (input.files) {
      const file = input.files[0];
      
      if(!this.mediaService.checkIfIsImage(file)){
        this.message.add({
          severity:"error",
          summary:"O arquivo não é uma imagem "
        })
        return;
      }
      if(!this.mediaService.checkIfIsImage(file)){
        this.message.add({
          severity:"error",
          summary:"O arquivo supera 600kb "
        })
        return;
      }
      const targetRatio = isMobile?1.2:3.84;
      this.mediaService.checkImageRatio(file,targetRatio).then((bool)=>{
        if(!bool){
          this.message.add({
            severity:"error",
            summary:"Dimensões inválidas para " + (isMobile?"mobile":"desktop")
          })
          return;
        }
        this.mediaService.convertFileToBase64(file).then((base64String) => {
            console.log(base64String); // Aqui você pode enviar para uma API ou armazenar no estado
            const extension = file.name.split('.').pop() || ".png";
            this.changeBanner(lojaId,index,isMobile,base64String,extension);

        }).catch((error) => {
            console.error("Erro ao converter o arquivo para Base64", error);
        });
      })
    }
  }

  changeBanner(lojaId:string,index:number,isMobile:boolean,base64:string,extension:string){
    const regarding = this.banners.find((e)=>e.lojaInfo.systemId===lojaId && e.lojaInfo.index===index);
    let banner!:BannerModel.Banner;
    if(regarding){
      banner = {...regarding};
      let media:BannerModel.Media[] = banner.media.filter((e)=>{
        if(isMobile && e.window===BannerModel.WindowContext.MOBILE){
          return false;
        }else if(!isMobile && e.window===BannerModel.WindowContext.DESKTOP){
          return false;
        }else{
          return true;
        }
      })
      media.push({
        bannerExtension:extension,
        bannerUrl:"",
        window:isMobile?BannerModel.WindowContext.MOBILE:BannerModel.WindowContext.DESKTOP,
        base64:base64
      })
      banner.media = media;
    }else{
      banner = {
        systemId:"",
        media:[{base64:base64,window:isMobile?BannerModel.WindowContext.MOBILE:BannerModel.WindowContext.DESKTOP,bannerUrl:"",bannerExtension:extension}],
        lojaInfo:{index:index,systemId:lojaId}
      }
    }
    this.loadingBannerForIndex[index] = true;
    this.bannerService.postBanner(banner).subscribe((id)=>{
      this.loadingBannerForIndex[index] = false;
      this.loadBanners();
    });
  }

  getBannerSourceByLojaIdAndIndex(lojaId:string,index:number,isMobile:boolean):string|undefined{
   const regarding = this.getBannerByLojaIdAndIndex(lojaId,index);
   if(isMobile){
     const mobileInfo = regarding?.media.find(e=>e.window===BannerModel.WindowContext.MOBILE);
     if(mobileInfo){
      return mobileInfo.bannerUrl || mobileInfo.base64;
     }
     return undefined;
   }else{
    const desktopInfo = regarding?.media.find(e=>e.window===BannerModel.WindowContext.DESKTOP);
    if(desktopInfo){
      return desktopInfo.bannerUrl || desktopInfo.base64;
    }
    return undefined;
   }
  }

  getBannerByLojaIdAndIndex(lojaId:string,index:number):BannerModel.Banner|undefined{
    return this.banners.find((e)=>e.lojaInfo.systemId===lojaId && e.lojaInfo.index===index);
  }


  onDelete(){//tirar a window, a loja e o banner
    this.loadBanners();
  }

  getBannerIdByLojaIdAndBannerIndex(lojaId:string,index:number){
    return this.banners.find((e)=>e.lojaInfo.systemId===lojaId && e.lojaInfo.index===index)?.systemId as string;
  }



}
