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
          100: '{slate.950}',
          200: '{slate.950}',
          300: '{slate.950}',
          400: '{slate.950}',
          500: '{slate.950}',
          600: '{slate.950}',
          700: '{slate.950}',
          800: '{slate.950}',
          900: '{slate.950}',
          950: '{slate.950}'
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
