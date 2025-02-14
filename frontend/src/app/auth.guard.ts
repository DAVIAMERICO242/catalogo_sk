import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateFn, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router';
import { UserService } from './services/user.service';
import { catchError, map } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn:'root'
})
export class AuthGuard implements CanActivate{

  constructor(private auth:UserService,private router:Router,@Inject(PLATFORM_ID) private platformId: Object){

  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): MaybeAsync<GuardResult> {
    console.log("aaa");
    console.log("CAN ACTIVE")
    
    return this.auth.isAuthRoute().pipe(
      map(()=>{
        if(isPlatformBrowser(this.platformId)){
          console.log("CLIENTE");
        }else{
          console.log("SERVIDOR");
        }
        return true;
      }),
      catchError((e)=>{
        if(isPlatformBrowser(this.platformId)){
          this.router.navigate(["/login"]);
          console.log("CLIENTE");
        }else{
          console.log("SERVIDOR");
        }
        console.log(e)
        return [false];
      })
    )
  }

  
}

