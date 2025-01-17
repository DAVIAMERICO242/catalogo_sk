import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateFn, GuardResult, MaybeAsync, Router, RouterStateSnapshot } from '@angular/router';
import { UserService } from './services/user.service';
import { catchError, map } from 'rxjs';

@Injectable({
  providedIn:'root'
})
export class AuthGuard implements CanActivate{

  constructor(private auth:UserService,private router:Router){

  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): MaybeAsync<GuardResult> {
    console.log("CAN ACTIVE")
    
    return this.auth.isAuthRoute().pipe(
      map(()=>{
        return true;
      }),
      catchError((e)=>{
        this.router.navigate(["/login"]);
        console.log(e)
        return [false];
      })
    )
  }

  
}

