import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from 'primeng/dialog';
import { TableModule } from 'primeng/table';
import { AvatarModule } from 'primeng/avatar';
import { MenubarModule } from 'primeng/menubar';
import { ToastModule } from 'primeng/toast';
import { ButtonModule } from 'primeng/button';
import { DatePickerModule } from 'primeng/datepicker';
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { MultiSelectModule } from 'primeng/multiselect';
import { SkeletonModule } from 'primeng/skeleton';
import { InputGroupModule } from 'primeng/inputgroup';
import { InputGroupAddonModule } from 'primeng/inputgroupaddon';
import { SelectModule } from 'primeng/select';
import { DrawerModule } from 'primeng/drawer';
import { TooltipModule } from 'primeng/tooltip';
import { PaginatorModule } from 'primeng/paginator';
import { ProgressBarModule } from 'primeng/progressbar';
import { Carousel } from 'primeng/carousel';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    Carousel,
    FormsModule,
    DrawerModule,
    TooltipModule,
    InputTextModule,
    InputGroupModule, 
    InputGroupAddonModule,
    SelectModule,
    InputNumberModule,
    SkeletonModule,
    ProgressBarModule,
    DialogModule,
    TableModule,
    AvatarModule,
    MenubarModule,
    ToastModule,
    ButtonModule,
    DatePickerModule,
    ToggleSwitchModule,
    MultiSelectModule,
    PaginatorModule
  ],
  exports:[
    CommonModule,
    Carousel,
    ProgressBarModule,
    FormsModule,
    DrawerModule,
    TooltipModule,
    InputTextModule,
    SkeletonModule,
    InputGroupModule, 
    SelectModule,
    InputGroupAddonModule,
    InputNumberModule,
    DialogModule,
    TableModule,
    AvatarModule,
    MenubarModule,
    ToastModule,
    ButtonModule,
    DatePickerModule,
    ToggleSwitchModule,
    MultiSelectModule,
    PaginatorModule
  ]
})
export class SharedModule { }
