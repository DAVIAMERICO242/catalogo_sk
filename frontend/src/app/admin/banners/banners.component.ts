import { Component, OnInit } from '@angular/core';
import { AdminPageTitleComponent } from "../admin-page-title/admin-page-title.component";
import { User, UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-banners',
  imports: [AdminPageTitleComponent,SharedModule],
  templateUrl: './banners.component.html'
})
export class BannersComponent implements OnInit {
  lojas!:User.Loja[]
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
            
        }).catch((error) => {
            console.error("Erro ao converter o arquivo para Base64", error);
        });
      })
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
            resolve(Math.abs(ratio - expectedRatio) < 0.01); // Aceita pequenas variações
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
