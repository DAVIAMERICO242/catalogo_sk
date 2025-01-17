import { HttpHeaders, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { UserService } from './services/user.service';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(UserService);

  // Verifica se o cabeçalho "token" já existe
  if (!req.headers.has('token')) {
    // Cria um novo cabeçalho com o token do contexto, se disponível
    const headers = req.headers.set('token', auth.getContext()?.token || '');

    // Clona a requisição e adiciona os novos cabeçalhos
    const clonedRequest = req.clone({ headers });
    return next(clonedRequest);
  }

  // Caso já exista o token, segue com a requisição original
  return next(req);
};
