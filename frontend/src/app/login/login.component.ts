import { Component, OnInit } from '@angular/core';
import { User, UserService } from '../services/user.service';
import { LojaService } from '../services/loja.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Loja } from '../services/loja.service';
import { SharedModule } from '../shared/shared.module';
import { Router } from '@angular/router';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-login',
  imports: [SharedModule],
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

  transientContext!:User.LoginResponse;
  loadingLogin = false;
  loadingLojas = false;
  loadingChangePass = false;
  openChangePass = false;
  lojas:Loja.Loja[] = [];
  selectedLoja!:Loja.Loja;
  payloadByUsername!:User.LoginRequest;
  payloadByLoja:User.LoginRequestByLoja={
     lojaSystemId:"",
     password:""
  };
  newPass =""
  loginLoja = true;
  unauthorizedAttemp = false;

  constructor(private auth:UserService,private lojaService:LojaService,private router:Router,private message:MessageService){

  }


  ngOnInit(): void {
    this.loadLojas();
  }

  loadLojas(){
    this.loadingLojas = true;
    this.lojaService.getLojas().subscribe({
       next:(data)=>{
        this.loadingLojas = false;
        this.lojas = data;
       },
       error:(e:HttpErrorResponse)=>{
        this.loadingLojas = false;

       }
    })
  }

  changeLojaPayload(){
       this.payloadByLoja = {
          ...this.payloadByLoja,
          lojaSystemId:this.selectedLoja.systemId,
       }
  }


  manageFirstPass(data:User.LoginResponse){
    this.transientContext = data;
    this.openChangePass = true;
  }

  submitFirstPass(){
     if(this.newPass.length<6){
      alert("Minimo 6 digitos")
      return;
     }
     this.loadingChangePass = true;
     this.auth.changeFirstPassword(this.transientContext.token,this.transientContext.username,this.newPass)
     .subscribe({
        next:()=>{
          this.loadingChangePass = false;
          this.auth.setContext(this.transientContext)
          this.router.navigate(["/admin"])
        },error:(e:HttpErrorResponse)=>{
           this.loadingChangePass = false;
           this.message.add({
            severity:"error",
            summary:e.error
          })
        }
     })
  }


  submit(){
     this.unauthorizedAttemp = false;
     this.loadingLogin = true;
     if(this.loginLoja){
      this.auth.loginByLoja(this.payloadByLoja).subscribe({
        next:(data)=>{
          this.loadingLogin = false;
          if(data.shouldChangeFirstPass){
            this.manageFirstPass(data)
          }else{
            this.auth.setContext(data);
            this.router.navigate(["/admin"])
          }
        
        },
        error:(e:HttpErrorResponse)=>{
          this.loadingLogin = false;
          this.unauthorizedAttemp = true;
        }
    })
     }
    else{
        this.auth.login(this.payloadByUsername).subscribe({
            next:(data)=>{
              this.loadingLogin = false;
              if(data.shouldChangeFirstPass){
                this.manageFirstPass(data)
              }else{
                this.auth.setContext(data);
                this.router.navigate(["/admin"])
              }
            },
            error:(e:HttpErrorResponse)=>{
              this.loadingLogin = false;
              this.unauthorizedAttemp = true;
            }
        })
     }



  }


}
