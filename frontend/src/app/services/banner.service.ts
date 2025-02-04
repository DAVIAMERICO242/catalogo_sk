import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { env } from '../../env';

export namespace BannerModel{
  export interface Banner{
    systemId:string;
    lojaInfo:LojaIndexInfo,
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

  getBanners(franquiaId?: string, lojaId?: string) {
    const url = new URL(env.BACKEND_URL + "/media/banner");
  
    const params = new URLSearchParams();
    if (franquiaId) params.append("franquiaId", franquiaId);
    if (lojaId) params.append("lojaId", lojaId);
  
    return this.http.get<BannerModel.Banner[]>(`${url.toString()}?${params.toString()}`);
  }

  postBanner(banner:BannerModel.Banner){
    return this.http.post<{id:string}>(env.BACKEND_URL+"/media/banner",banner);
  }

  bulkBanner(banners:BannerModel.Banner[]){
    return this.http.post<void>(env.BACKEND_URL+"/media/banner/bulk",banners);
  }

  bulkDelete(ids:string[]){
    return this.http.delete<void>(env.BACKEND_URL+"/media/banner/bulk?ids="+ids.join(","));
  }

  saveReindex(reindexedForLoja:BannerModel.Banner[]){
    return this.http.post<void>(env.BACKEND_URL+"/media/banner/save-reindex",reindexedForLoja);
  }

  desassociarBannerLoja(bannerId:string,isMobile:boolean){
    return this.http.delete<void>(env.BACKEND_URL+"/media/banner?bannerId="+bannerId+"&isMobile=" + isMobile)
  }
}
