import { Injectable } from '@angular/core';

export namespace BannerModel{
  export interface Banner{
    systemId:string;
    lojaInfo:LojaIndexInfo[],
    media:Media[]
  }

  export interface LojaIndexInfo{
    systemId:string,
    index:number
  }

  export interface Media{
    bannerExtension:string,
    window:WindowContext,
    base64:string,
    bannerUrl:string
  }


  export enum WindowContext{
    DESKTOP="DESKTOP",
    MOBILE="MOBILE"
  }


}

@Injectable({
  providedIn: 'root'
})
export class BannerService {

  constructor() { }
}
