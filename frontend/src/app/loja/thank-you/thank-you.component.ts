import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Pedidos } from '../../services/pedidos.service';
import { fadeIn } from '../../animations/fadeIn';
import { LojaContextService } from '../loja-context.service';
import { Loja } from '../../services/loja.service';
import { CepPipePipe } from '../../pipes/cep-pipe.pipe';
import { PhonePipePipe } from '../../pipes/phone-pipe.pipe';
import { DocumentoPipe } from '../../pipes/documento.pipe';

@Component({
  selector: 'app-thank-you',
  imports: [],
  templateUrl: './thank-you.component.html',
  host: { 'style': 'display: block;' },
  animations:[fadeIn]
})
export class ThankYouComponent implements OnInit {
  pedidoReduced!:Pedidos.PedidoReducedTypes.PedidoReduced;
  loja!:Loja.Loja;

  constructor(
    private router: Router,
    private lojaContext:LojaContextService,
    private cepPipe:CepPipePipe,
    private phonePipe:PhonePipePipe,
    private documentoPipe:DocumentoPipe
  ) {
    const navigation = this.router.getCurrentNavigation();
    if (navigation?.extras.state) {
      this.pedidoReduced =  navigation?.extras.state as Pedidos.PedidoReducedTypes.PedidoReduced;
    }
  }
  ngOnInit(): void {
    this.lojaContext.loja$.subscribe((loja)=>{
      this.loja = loja as Loja.Loja;
    })
  }

  generateWhatsAppUrl(){

  
    // Cria a mensagem do WhatsApp
    let message = `*Novo pedido - Catálogo ${this.loja.loja}*\n\n`;

    message += "*CONFERIR AS INFORMAÇÕES DESSA MENSAGEM NO SISTEMA*\n\n"
  
    message += `🛍️ *Pedido*\n\n`;
    message += `*Total (com descontos e sem frete)*: R$ ${this.pedidoReduced.valor}\n`;
    message += `*Descontos*: R$ ${this.pedidoReduced.descontosAplicados.reduce((a,b)=>a+b.valorAplicado,0)}\n\n`;

    if(this.pedidoReduced.tipoFrete){
      message += `*Frete*: R$ ${this.pedidoReduced.valorFrete}\n`;
      message += `*Tipo frete*: ${this.pedidoReduced.tipoFrete}\n\n`;
    }
  
    // Adiciona produtos
    message += `<---------------\n\n`;
    this.getProdutosPedidosStringifados().forEach(produto => {
      message += `*${produto}* \n\n`;
    });
    message += `---------------->\n\n`;
  
    // Adiciona informações do cliente
    message += `👤 *Cliente*\n\n`;
    message += `*Nome*: ${this.pedidoReduced.nome}\n`;
    message += `*CPF/CNPJ*: ${this.documentoPipe.transform(this.pedidoReduced.documento)}\n`;
    message += `*Telefone*: ${this.phonePipe.transform(this.pedidoReduced.telefone)}\n\n`;
  
    // Adiciona endereço
    message += `🚚 *Endereço*\n\n`;
    if(!this.pedidoReduced.entregaLoja){
      message += `*CEP*: ${this.cepPipe.transform(this.pedidoReduced.cep)}\n`;
      message += `*ESTADO*: ${this.pedidoReduced.estado}\n`;
      message += `*CIDADE*: ${this.pedidoReduced.cidade}\n`;
      message += `*BAIRRO*: ${this.pedidoReduced.bairro}\n`;
      message += `*RUA*: ${this.pedidoReduced.rua}\n`;
      message += `*Número*: ${this.pedidoReduced.numero}\n\n`;
    }else{
      message += `*COLETA EM LOJA*\n\n`;
    }
  
    // Codifica a mensagem para a URL
    return `https://api.whatsapp.com/send?phone=${"55" + this.loja.telefone?.replaceAll("(","").replaceAll(")","").replaceAll(" ","").replaceAll("-","")}&text=${encodeURIComponent(message)}`;
  }

  getProdutosPedidosStringifados():string[]{
    const output = [];
    for(let produto of this.pedidoReduced.produtos){
      for(let variacao of produto.variacoesCompradas){
        output.push(produto.nome + " " + "Cor: " + variacao.cor + " Tamanho: " + variacao.tamanho + ` (QDTE: ${variacao.quantidade})`)
      }
    }
    return output;
  }

  goToWhatsapp(){
    const waUrl = this.generateWhatsAppUrl();
    // window.location.href = waUrl;
    window.open(waUrl, '_blank');

  }

}
