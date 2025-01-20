import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from 'primeng/dialog';
import { TableModule } from 'primeng/table';
import { AvatarModule } from 'primeng/avatar';
import { Menubar } from 'primeng/menubar';
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


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    FormsModule,
    DrawerModule,
    InputTextModule,
    InputGroupModule, 
    InputGroupAddonModule,
    SelectModule,
    InputNumberModule,
    SkeletonModule,
    DialogModule,
    TableModule,
    AvatarModule,
    Menubar,
    ToastModule,
    ButtonModule,
    DatePickerModule,
    ToggleSwitchModule,
    MultiSelectModule
  ],
  exports:[
    CommonModule,
    FormsModule,
    DrawerModule,
    InputTextModule,
    SkeletonModule,
    InputGroupModule, 
    SelectModule,
    InputGroupAddonModule,
    InputNumberModule,
    DialogModule,
    TableModule,
    AvatarModule,
    Menubar,
    ToastModule,
    ButtonModule,
    DatePickerModule,
    ToggleSwitchModule,
    MultiSelectModule
  ]
})
export class SharedModule { }
