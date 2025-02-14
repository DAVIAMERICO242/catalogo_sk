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
import { FreteComponent} from "./frete/frete.component";
import { FreteContextService, FreteEmissionSignature } from './frete/frete-context.service';
import { VerLocalizacaoLojaComponent } from "../ver-localizacao-loja/ver-localizacao-loja.component";
import { Router } from '@angular/router';
import { CustomerCache } from './customer-cache.service';

@Component({
  selector: 'app-checkout',
  imports: [SharedModule, ProdutoSacolaComponent, FreteComponent, VerLocalizacaoLojaComponent],
  templateUrl: './checkout.component.html'
})
export class CheckoutComponent implements OnInit, OnDestroy {

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
  loadingFrete = false;


  constructor(
    private sacolaContext:SacolaUiContextService,
    private sacolaService:SacolaService,
    private lojaContext:LojaContextService,
    private pedidoService:PedidosService,
    private cepService:CepService,
    private message:MessageService,
    private freteContext:FreteContextService,
    private customerCache:CustomerCache,
    private router:Router
  ){

  }
  ngOnInit(): void {
    this.subscriptions.add(
      this.lojaContext.loja$.subscribe((loja)=>{
        if(loja){
          this.loja = loja;
          this.loadSacola();
          this.configurePayload();
        }
      }));

    this.subscriptions.add(this.sacolaContext.onSacolaChange$.subscribe(()=>{
      if(this.loja){
        this.loadSacola();
        if(this.sacola.produtos.length && this.customerDetails.cep){
          this.freteContext.setValorFrete(this.sacola,true);
        }
      }
    }));
    this.subscriptions.add(
      this.freteContext.freteEmissor$.subscribe((val)=>{
        this.manageFreteChange(val);
      })
    );
    this.subscriptions.add(
      this.freteContext.loadingValorFrete$.subscribe((loadingFrete)=>{
        this.loadingFrete = loadingFrete;
      })
    );
  }
  
  ngOnDestroy(): void {
    console.log("checkout destruido")
    this.subscriptions.unsubscribe();
  }


  configurePayload(){
    const cache = this.customerCache.getCachedCheckout();
    this.customerDetails = {
      bairro:cache?.bairro || "",
      cep:cache?.cep || "",
      cidade:cache?.cidade || "",
      documento:cache?.documento || "",
      estado:cache?.estado || "",
      nome:cache?.nome || "",
      numero:cache?.numero || 0,
      rua:cache?.rua || "",
      telefone:cache?.telefone || "",
      entregaLoja:false
    }
    if(cache){
      this.manageCepChange();
      this.loadTipoFrete();
    }
  }

  manageCepChange(){
    this.freteContext.setCep(this.customerDetails.cep);
    this.preencherCamposPeloCep();
    this.loadTipoFrete();
  }

  manageColetaLojaChange(){
    this.freteContext.setColetaLoja(this.customerDetails.entregaLoja);
    if(this.customerDetails.entregaLoja){
      this.total = this.total - this.frete;
      this.frete = 0;
      this.tipoFrete = undefined;
    }
  }

  manageFreteChange(frete?:FreteEmissionSignature){
    if(!frete){
      this.total = this.total - this.frete;
      this.frete = 0;
      this.tipoFrete = undefined;
      return;
    }
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
      this.freteContext.setValorFrete(this.sacola,false);
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
    }else{
      this.sacola = {
        loja:this.loja,
        produtos:[] 
      };
    }
    this.setValores();
  }

  setValores(){
    this.subscriptions.add(this.sacolaContext.descontosAplicados$.subscribe((data)=>{
      let bruto = 0;
      for(let produto of this.sacola.produtos){
        for(let variacao of produto.produtoBase.variacoes){
          bruto = bruto + produto.produtoBase.preco*variacao.quantidade;
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
      if(!this.customerDetails.telefone){
        this.message.add({
          severity:"error",
          summary:"Telefone inválido"
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
      next:(pedido)=>{
        this.loadingNovoPedido = false;
        this.sacolaContext.limparSacola(this.loja);
        this.customerCache.cacheCheckoutFields({
          bairro:pedido.bairro,
          cep:pedido.cep,
          cidade:pedido.cidade,
          documento:pedido.documento,
          estado:pedido.estado,
          nome:pedido.nome,
          numero:pedido.numero,
          rua:pedido.rua,
          telefone:pedido.telefone
        })
        this.gotToThankYouPage(this.pedidoService.reducePedido(pedido));
      },
      error:(err:HttpErrorResponse)=>{
        this.loadingNovoPedido = false;
        alert("Erro crítico: " + err.error);
      }
    });
  }

  gotToThankYouPage(pedido:Pedidos.PedidoReducedTypes.PedidoReduced){
    this.router.navigate([this.loja.slug + "/thank-you"],{state:pedido})
  }

}
