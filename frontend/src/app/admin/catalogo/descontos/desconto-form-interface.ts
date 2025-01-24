import { EventEmitter } from "@angular/core";
import { Desconto } from "../../../services/descontos.service";

export interface DescontoForm{
    payload:Desconto.DescontoModel;
    loading:boolean;
    onSave:EventEmitter<Desconto.DescontoModel>
    validate():boolean;
    save():void;
}