import { Component, OnInit } from '@angular/core';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { User, UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';
import { MessageService } from 'primeng/api';
import { BannerModel } from '../../services/banner.service';

@Component({
  selector: 'app-banners',
  imports: [AdminPageTitleComponent,SharedModule],
  templateUrl: './banners.component.html'
})
export class BannersComponent implements OnInit {
  lojas!:User.Loja[];
  banners:BannerModel.Banner[] = [];
  constructor(private auth:UserService,private message:MessageService){}
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
    this.banners.push({
      lojaInfo:[],
      media:[],
      systemId:""
    })
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
    const regarding = this.banners.find((e)=>{
       if(e.lojaInfo.map((e)=>e.systemId).includes(lojaId)){
          const r0 =  e.lojaInfo.find((e)=>e.systemId===lojaId && e.index===index);
          if(r0){
            return true;
          }else{
            return false;
          }
       }else{
        return false;
       }
    });
    if(regarding){
      this.banners = this.banners.map((e)=>{
         if(e.lojaInfo.map((e)=>e.systemId).includes(lojaId)){
          const r0 =  e.lojaInfo.find((e)=>e.systemId===lojaId && e.index===index);
          if(r0){
            const mediaClone = [...e.media];
            if(!e.media.map((e)=>e.window).includes(BannerModel.WindowContext.MOBILE) && isMobile){
              mediaClone.push({
                bannerExtension:extension,
                bannerUrl:"",
                window:BannerModel.WindowContext.MOBILE,
                base64:base64
              })
            }
            if(!e.media.map((e)=>e.window).includes(BannerModel.WindowContext.DESKTOP) && !isMobile){
              mediaClone.push({
                bannerExtension:extension,
                bannerUrl:"",
                window:BannerModel.WindowContext.DESKTOP,
                base64:base64
              })
            }
            return{
              ...e,
              media:mediaClone.map((e1)=>{

                if(isMobile){
                  if(e1.bannerExtension===BannerModel.WindowContext.MOBILE){//mobile que ja existe
                    return{
                      ...e1,
                      base64:base64
                    }
                  }else{
                    return e1;
                  }
                }else{
                  if(e1.bannerExtension===BannerModel.WindowContext.DESKTOP){//desktop que ja existe
                    return{
                      ...e1,
                      base64:base64
                    }
                  }else{
                    return e1;
                  }
                }
              })
            }
          }else{
            return e;
          }
         }else{
          return e;
         }
      })
    }else{
      this.banners.push({
        systemId:"",
        media:[{base64:base64,window:isMobile?BannerModel.WindowContext.MOBILE:BannerModel.WindowContext.DESKTOP,bannerUrl:"",bannerExtension:extension}],
        lojaInfo:[{index:index,systemId:lojaId}]
      })
    }
  }

  getBannerByLojaIdAndIndex(lojaId:string,index:number,isMobile:boolean):string|undefined{
    const regarding = this.banners.find((e)=>{
      if(e.lojaInfo.map((e)=>e.systemId).includes(lojaId)){
         const r0 =  e.lojaInfo.find((e)=>e.systemId===lojaId && e.index===index);
         if(r0){
           return true;
         }else{
           return false;
         }
      }else{
       return false;
      }
   });
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
