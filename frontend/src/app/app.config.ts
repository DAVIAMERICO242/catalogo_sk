import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { definePreset } from '@primeng/themes';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { jwtInterceptor } from './jwt.interceptor';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';
import Aura from '@primeng/themes/aura';

export const MyTheme = definePreset(Aura, {
  semantic: {
      primary: {
          50: '{slate.100}',
          100: '{slate.900}',
          200: '{slate.900}',
          300: '{slate.900}',
          400: '{slate.900}',
          500: '{slate.900}',
          600: '{slate.900}',
          700: '{slate.900}',
          800: '{slate.900}',
          900: '{slate.900}',
          950: '{slate.900}'
      }
  }
});

export const appConfig: ApplicationConfig = {
  providers: 
  [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([jwtInterceptor])),
    provideAnimationsAsync(),
    providePrimeNG({
      theme: {
          preset: Aura
      }
  })
  ]
};
