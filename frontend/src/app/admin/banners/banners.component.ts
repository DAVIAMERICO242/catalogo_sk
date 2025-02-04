import { Component, OnInit } from '@angular/core';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { User, UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';
import { MessageService } from 'primeng/api';
import { BannerModel, BannerService } from '../../services/banner.service';
import { DeletarBannerComponent, DeleteNotification } from "./deletar-banner/deletar-banner.component";
import { BulkBannerReplicationComponent } from "./bulk-banner-replication/bulk-banner-replication.component";


@Component({
  selector: 'app-banners',
  imports: [AdminPageTitleComponent, SharedModule, DeletarBannerComponent, BulkBannerReplicationComponent],
  templateUrl: './banners.component.html'
})
export class BannersComponent implements OnInit {
  lojas!:User.Loja[];
  banners:BannerModel.Banner[] = [];
  loadingBanners = false;
  loadingBannerForIndex:boolean[] = [];
  constructor(private bannerService:BannerService,protected auth:UserService,private message:MessageService){}
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
        });
      }
    }else{
      const lojaId = this.auth.getContext()?.loja.systemId;
      if(lojaId && franquiaId){
        this.bannerService.getBanners(franquiaId,lojaId).subscribe((data)=>{
          this.loadingBanners = false;
          this.banners = data;
        });
      }
    }
  }

  onBannerChange(event:Event,lojaId:string,index:number,isMobile:boolean){
    const input = event.target as HTMLInputElement;
    if (input.files) {
      const file = input.files[0];
      
      if(!this.checkIfIsImage(file)){
        return;
      }
      if(!this.checkIfIsLessOrEqual660kb(file)){
        return;
      }
      const targetRatio = isMobile?1.2:3.84;
      this.checkImageRatio(file,targetRatio).then((bool)=>{
        if(!bool){
          this.message.add({
            severity:"error",
            summary:"Dimensões inválidas para " + (isMobile?"mobile":"desktop")
          })
          return;
        }
        this.convertFileToBase64(file).then((base64String) => {
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

  checkImageRatio(file: File, expectedRatio: number): Promise<boolean> {
    return new Promise((resolve) => {
        const img = new Image();
        const objectURL = URL.createObjectURL(file);
        
        img.onload = () => {
            const ratio = img.width / img.height;
            console.log(`Proporção da imagem: ${ratio}`);
            URL.revokeObjectURL(objectURL); // Libera a memória
            resolve(Math.abs(ratio - expectedRatio) < 0.05); // Aceita pequenas variações
        };

        img.onerror = () => {
            console.error("Erro ao carregar a imagem.");
            URL.revokeObjectURL(objectURL);
            resolve(false);
        };

        img.src = objectURL;
    });
}

  checkIfIsImage(file:File){
    if (!file.type.startsWith("image/")) {
      console.error("O arquivo selecionado não é uma imagem.");
      this.message.add({
        severity:"error",
        summary:"O arquivo não é uma imagem"
      })
      return false;
    }
    return true;
  }

  checkIfIsLessOrEqual660kb(file:File){
    if (file.size > 614400) { // 600 KB em bytes
      this.message.add({
        severity:"error",
        summary:"Máximo 600kb"
      })
      return false;
    }
    return true;
  }

  convertFileToBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        
        reader.onload = () => {
            resolve(reader.result as string);
        };
        
        reader.onerror = (error) => {
            reject(error);
        };
    });
  }


}
