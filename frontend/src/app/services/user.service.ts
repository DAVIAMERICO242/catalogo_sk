import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { env } from '../../env';
import { Router } from '@angular/router';
import { isPlatformBrowser } from '@angular/common';


export namespace User{
  export enum Role{
    ADMIN="ADMIN",
    OPERACIONAL="OPERACIONAL"
  }
  export interface LoginRequest{
    username:string,
    password:string
  }
  export interface LoginRequestByLoja{
    lojaSystemId:string,
    password:string
  }
  export interface LoginResponse{
    beautyName:string,
    username:string,
    password:string,
    role:Role
    token:string,
    shouldChangeFirstPass:boolean,
    loja:Loja,
    franquia:Franquia,
    lojasFranquia?:Loja[]//nao carregado para o não admin
  }
  export interface Loja{
    systemId:string
    nome:string,
    slug:string,
  }
  export interface Franquia{
    systemId:string,
    nome:string,
    isMatriz:boolean
  }
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient, private route:Router,@Inject(PLATFORM_ID) private platformId: Object){}

  login(payload:User.LoginRequest){
     return this.http.post<User.LoginResponse>(env.BACKEND_URL+"/login",payload);
  }

  loginByLoja(payload:User.LoginRequestByLoja){
    return this.http.post<User.LoginResponse>(env.BACKEND_URL+"/login-by-loja",payload);
  }

  setContext(payload:User.LoginRequest){
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem("context",JSON.stringify(payload));
    }
  }


  getContext():User.LoginResponse|undefined{
    if (isPlatformBrowser(this.platformId)) {
      const storage = localStorage.getItem("context");
      if(!storage){
        return undefined;
      }
      return JSON.parse(storage) as User.LoginResponse;
    }
    return undefined
  }

  isAuthRoute(){
    return this.http.get(env.BACKEND_URL+"/private-route")
  }

  changeFirstPassword(jwt:string,username:string,password:string){
    const headers = new HttpHeaders({
      token:jwt
    })
    return this.http.put<void>(env.BACKEND_URL + `/change-password?username=${username}&newPass=${password}`,{},{headers})
  }

  logout(){
    this.clearContext();
    this.route.navigate(["/login"])
  }

  private clearContext(){
    if (isPlatformBrowser(this.platformId)) {
      localStorage.clear();
    }
  }

}
