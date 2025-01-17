import { Component, OnInit } from '@angular/core';
import { User, UserService } from '../services/user.service';
import { LojaService } from '../services/loja.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Loja } from '../services/loja.service';
import { SharedModule } from '../shared/shared.module';
enum LoginMethod{
  LOJA="LOJA",
  USERNAME="USERNAME"
}
@Component({
  selector: 'app-login',
  imports: [SharedModule],
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

  transientContext!:User.LoginResponse;
  loadingLogin = false;
  loadingLojas = false;
  lojas:Loja.Loja[] = [];
  selectedLoja!:Loja.Loja;
  payloadByUsername!:User.LoginRequest;
  payloadByLoja!:User.LoginRequestByLoja;
  loginMethod = LoginMethod.LOJA;
  unauthorizedAttemp = false;

  constructor(private auth:UserService,private lojaService:LojaService){

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

  submit(){
     this.unauthorizedAttemp = false;
     this.loadingLogin = true;
     if(this.loginMethod===LoginMethod.LOJA){
      this.auth.loginByLoja(this.payloadByLoja).subscribe({
        next:(data)=>{
          this.loadingLogin = false;
        
        },
        error:(e:HttpErrorResponse)=>{
          this.loadingLogin = false;
          this.unauthorizedAttemp = true;
        }
    })
     }
     if(this.loginMethod===LoginMethod.USERNAME){
        this.auth.login(this.payloadByUsername).subscribe({
            next:(data)=>{
              this.loadingLogin = false;
    
            },
            error:(e:HttpErrorResponse)=>{
              this.loadingLogin = false;
              this.unauthorizedAttemp = true;
            }
        })
     }



  }


}
