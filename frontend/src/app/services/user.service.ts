import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { env } from '../../env';


export namespace User{
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
    token:string,
    shouldChangeFirstPass:boolean,
    loja:Loja,
    franquia:Franquia
  }
  export interface Loja{
    systemId:string
    nome:string,
    slug:string
  }
  export interface Franquia{
    systemId:string,
    nome:string
  }
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient){}

  login(payload:User.LoginRequest){
     return this.http.post<User.LoginResponse>(env.BACKEND_URL+"/login",payload);
  }

  loginByLoja(payload:User.LoginRequestByLoja){
    return this.http.post<User.LoginResponse>(env.BACKEND_URL+"/login-by-loja",payload);
  }

  setContext(payload:User.LoginRequest){
    localStorage.setItem("context",JSON.stringify(payload));
  }

  getContext():User.LoginResponse|undefined{
    const storage = localStorage.getItem("context");
    if(!storage){
      return undefined;
    }
    return JSON.parse(storage) as User.LoginResponse;
  }

  isAuthRoute(){
    return this.http.get(env.BACKEND_URL+"/private-route")
  }

}
