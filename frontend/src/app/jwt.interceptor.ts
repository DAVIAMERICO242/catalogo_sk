import { HttpHeaders, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { UserService } from './services/user.service';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {

  const auth = inject(UserService);

  const headers = new HttpHeaders({
    "token":auth.getContext().token
  })

  const clonedRequest = req.clone({
    headers: headers
  });
  return next(clonedRequest);
};
