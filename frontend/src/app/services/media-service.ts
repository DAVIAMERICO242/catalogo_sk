import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MediaService {

  constructor() { }

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
      return false;
    }
    return true;
  }

  checkIfIsLessOrEqual660kb(file:File){
    if (file.size > 614400) { // 600 KB em bytes
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
