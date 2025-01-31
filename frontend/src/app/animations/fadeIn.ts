import { animate, style, transition, trigger } from "@angular/animations";

export const fadeIn = trigger('fadeIn', [
    transition(':enter', [ // This applies when the element is added to the DOM
      style({ opacity: 0,transform:"translateY(100px)" }), // Start invisible
      animate('300ms ease-in', style({ opacity: 1,transform:"translateY(0px)" })) // Fade in over 500ms
    ])
  ])