import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { Pedidos, PedidosService } from '../../services/pedidos.service';
import { Sacola, SacolaService } from '../../services/sacola.service';
import { Loja } from '../../services/loja.service';
import { LojaContextService } from '../loja-context.service';
import { Subscription } from 'rxjs';
import { SacolaUiContextService } from '../sacola/sacola-ui-context.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ProdutoSacolaComponent } from "../sacola/produto-sacola/produto-sacola.component";
import { Desconto } from '../../services/descontos.service';
import { CepService } from '../../services/cep.service';
import { MessageService } from 'primeng/api';
import { FreteComponent, FreteEmissionSignature } from "./frete/frete.component";

@Component({
  selector: 'app-checkout',
  imports: [SharedModule, ProdutoSacolaComponent, FreteComponent],
  templateUrl: './checkout.component.html'
})
export class CheckoutComponent implements OnInit, OnDestroy {
  @ViewChild(FreteComponent) freteComponent!:FreteComponent;
  customerDetails!:Pedidos.PedidoCustomerDetails;
  tipoFrete!:Pedidos.TipoFrete | undefined;
  frete = 0;
  sacola!:Sacola.SacolaModel;
  loja!:Loja.Loja;
  subscriptions = new Subscription();
  loadingNovoPedido = false;
  totalBruto!:number;
  total!:number;//liquido
  descontosAplicados!:Desconto.DescontoAplicado[];
  totalDescontos!:number;
  loadingCepAutoComplete = false;

  constructor(
    private sacolaContext:SacolaUiContextService,
    private sacolaService:SacolaService,
    private lojaContext:LojaContextService,
    private pedidoService:PedidosService,
    private cepService:CepService,
    private message:MessageService
  ){

  }
  ngOnInit(): void {
    this.subscriptions.add(
      this.lojaContext.loja$.subscribe((loja)=>{
        if(loja){
          this.loja = loja;
          this.loadSacola();
        }
      })
    )

    this.subscriptions.add(this.sacolaContext.onSacolaChange$.subscribe(()=>{
      if(this.loja){
        this.loadSacola();
      }
    }));
    this.configurePayload();
  }
  
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  configurePayload(){
    this.customerDetails = {
      bairro:"",
      cep:"",
      cidade:"",
      documento:"",
      estado:"",
      nome:"",
      numero:0,
      rua:"",
      telefone:"",
      entregaLoja:false
    }
  }

  showEntregaLoja(){
    console.log(this.customerDetails.entregaLoja)
  }

  manageCepChange(){
    this.preencherCamposPeloCep();
    this.loadTipoFrete();
  }

  manageColetaLojaChange(){
    if(this.customerDetails.entregaLoja){
      this.total = this.total - this.frete;
      this.frete = 0;
      this.tipoFrete = undefined;
    }
  }

  manageFreteChange(frete:FreteEmissionSignature){
    if(!this.customerDetails.entregaLoja){//esse if e importante caso o usuario veja que o frete demora calcular e clique em coletar loja
      this.total = this.total - this.frete;
      this.tipoFrete = frete.tipo;
      this.frete = frete.valorFrete;
      this.total = this.total + frete.valorFrete;
    }
  }

  loadTipoFrete(){
    const formated = this.customerDetails.cep.trim().replace("-","");
    if(formated.length===8){
      this.freteComponent.getValorFrete();
    }
  }

  preencherCamposPeloCep(){
    const formated = this.customerDetails.cep.trim().replace("-","");
    if(formated.length===8){
      this.loadingCepAutoComplete = true;
      this.cepService.buscarCep(formated).subscribe({
        next:(data)=>{
          this.customerDetails.estado = data.estado;
          this.customerDetails.cidade = data.cidade;
          this.customerDetails.rua = data.rua;
          this.customerDetails.bairro = data.bairro;
          this.loadingCepAutoComplete = false;
        },
        error:()=>{
          this.loadingCepAutoComplete = false;
        }
      });
    }
  }

  loadSacola(){
    const sacola = this.sacolaContext.getSacolaForLoja(this.loja);
    if(sacola){
      this.sacola = sacola;
      this.sacolaContext.setDescontos(this.sacola);
    }
    this.setValores();
  }

  setValores(){
    this.subscriptions.add(this.sacolaContext.descontosAplicados$.subscribe((data)=>{
      let bruto = 0;
      for(let produto of this.sacola.produtos){
        for(let variacao of produto.produtoBase.variacoes){
          bruto = bruto + produto.produtoBase.preco;
        }
      }
      this.totalBruto = bruto;
      this.descontosAplicados = data;
      this.totalDescontos = this.descontosAplicados.reduce((a,b)=>a+b.valorAplicado,0);
      this.total = bruto - this.totalDescontos;
      console.log(this.total)
    }));
  }

  isValid(){
    if(!this.customerDetails.nome){
      this.message.add({
        severity:"error",
        summary:"Nome inválido"
      });
      return false;
    }
    const formatedDocumento = this.customerDetails.documento.trim().replaceAll("/","").replaceAll(".","").replaceAll(" ","").replaceAll("-","");
    if(formatedDocumento.length!==11 && formatedDocumento.length!==14){
      this.message.add({
        severity:"error",
        summary:"CPF/CNPJ inválido"
      });
      return false;
    }
    if(!this.customerDetails.entregaLoja){
      const formattedCep = this.customerDetails.cep.trim().replaceAll(" ","").replaceAll("-","");
      if(formattedCep.length!==8){
        this.message.add({
          severity:"error",
          summary:"CEP inválido"
        });
        return false;
      }
      if(!this.customerDetails.estado){
        this.message.add({
          severity:"error",
          summary:"Estado inválido"
        });
        return false;
      }
      if(!this.customerDetails.cidade){
        this.message.add({
          severity:"error",
          summary:"Cidade inválida"
        });
        return false;
      }
      if(!this.customerDetails.rua){
        this.message.add({
          severity:"error",
          summary:"Rua inválida"
        });
        return false;
      }
      if(!this.customerDetails.bairro){
        this.message.add({
          severity:"error",
          summary:"Bairro inválido"
        });
        return false;
      }
      if(!this.customerDetails.numero){
        this.message.add({
          severity:"error",
          summary:"Número inválido"
        });
        return false;
      }
    }
    return true;
  }

  postPedido(){
    if(!this.isValid()){
      return;
    }
    const rawSacola = this.sacolaService.mapModelToRawSacola(this.sacola);
    const preparedPayload = this.pedidoService.mapRawSacolaAndCustomerAndValorFreteToPedidoRequest(rawSacola,this.customerDetails,this.frete);
    preparedPayload.tipoFrete = this.tipoFrete;
    this.loadingNovoPedido = true;
    this.pedidoService.novoPedido(preparedPayload).subscribe({
      next:()=>{
        this.loadingNovoPedido = false;
      },
      error:(err:HttpErrorResponse)=>{
        this.loadingNovoPedido = false;
        alert("Erro crítico: " + err.error);
      }
    });
  }

}
