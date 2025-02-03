import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { env } from '../../env';

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
    base64?:string,//só é nao nulo no post
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

  constructor(private http:HttpClient){}

  getBanners(franquiaId:string,lojaId?:string){
    let url = env.BACKEND_URL+"/media/banner?franquiaId="+franquiaId;
    if(lojaId){
      url = url + "&lojaId="+lojaId;
    }
    return this.http.get<BannerModel.Banner[]>(url)
  }

  postBanner(banners:BannerModel.Banner){
    return this.http.post<{id:string}>(env.BACKEND_URL+"/media/banner",banners);
  }
}
