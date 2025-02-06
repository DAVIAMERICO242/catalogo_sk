import { Component, OnInit } from '@angular/core';
import { Catalogo, CatalogoService } from '../../services/catalogo.service';
import { SharedModule } from '../../shared/shared.module';
import { ActivatedRoute, Router } from '@angular/router';
import { Produto, ProdutosService } from '../../services/produtos.service';
import { HttpErrorResponse } from '@angular/common/http';
import { LojaContextService } from '../loja-context.service';
import { filter, first, take } from 'rxjs';
import { fadeIn } from '../../animations/fadeIn';
import { Sacola, SacolaService } from '../../services/sacola.service';
import { Loja } from '../../services/loja.service';
import { Pedidos } from '../../services/pedidos.service';

@Component({
  selector: 'app-product-page',
  imports: [SharedModule],
  templateUrl: './product-page.component.html',
  styles:[
    `
    .tiny-scroll::-webkit-scrollbar {
      width: 5px; /* Largura do scrollbar vertical */
      height: 5px;
    }
    .tiny-scroll::-webkit-scrollbar-thumb{
      background: #dfdada; /* Cor do trilho */
    }
    `
  ],
  animations:[fadeIn]
})
export class ProductPageComponent implements OnInit{
  loja!:Loja.Loja;
  productId="";
  produto!:Catalogo.Produto;
  focusedSku!:string;
  stock!:Produto.ProdutoEstoque;
  photos:string[] = [];
  cores:string[] = [];
  coresOption:{label:string,value:string,disabled:boolean}[] = [];
  coresSemEstoque:string[] = [];
  selectedCor:string|undefined;
  selectedTamanho:string|undefined;
  selectedPhoto!:string;
  tamanhos:string[] = [];
  overAddedTheSameSku = false;
  

  constructor(
    private catalogoService:CatalogoService,
    private lojaContext:LojaContextService,
    private route:ActivatedRoute,
    private produtoService:ProdutosService,
    private sacolaService:SacolaService
  ){}


  ngOnInit(): void {
    console.log(this.productId);
    this.productId = this.route.snapshot.paramMap.get("id") || "";
    this.loadProduct();
  }

  loadProduct(){
    this.catalogoService.getByProdutoCatalogoId(this.productId).subscribe(
      {
        next:(data)=>{
          this.produto = data;
          this.photos = [...new Set(this.produto.produtoBase.variacoes.map((e)=>e.foto))];
          this.cores = [...new Set(this.produto.produtoBase.variacoes.map((e)=>e.cor).sort((a,b)=>a.localeCompare(b)))];
          this.tamanhos = [...new Set(this.produto.produtoBase.variacoes.map((e)=>e.tamanho))];
          this.focusedSku = this.produto.produtoBase.sku;
          this.loadStock();
        },
        error:(err:HttpErrorResponse)=>{
          alert(err.error);
        }
      }
    );
  }


  loadStock(){
    this.lojaContext.loja$
    .pipe(filter(loja=>loja!=undefined),take(1))  // Desinscreve automaticamente após encontrar o valor não nulo
    .subscribe((loja) => {
      // Seu código aqui para tratar o valor não nulo
      if(loja){
        this.loja = loja;
        this.produtoService.getStock([this.produto.produtoBase.sku],loja.slug).subscribe({
          next:(s)=>{
            this.stock = s[0];
            this.coresETamanhosSemEstoqueUI();
          },
          error:(e:HttpErrorResponse)=>{
            alert("Erro ao carregar estoque");
          }
        });
      }
    });
  }

  coresETamanhosSemEstoqueUI(){
    // this.selectedTamanho = this.tamanhos[0];
    for(let cor of this.cores){
      let hasStockForCor = false;
      for(let tamanho of this.tamanhos){
        const sku = this.getSkuByCorETamanho(cor,tamanho);
        if(sku){
          hasStockForCor = (this.stock.estoque.find((e)=>e.sku===sku)?.estoque || 0)>0;
          if(hasStockForCor){
            break;
          }
        }
      }
      if(!hasStockForCor){
        this.coresSemEstoque.push(cor);
        this.coresOption.push({
          label:cor,
          value:cor,
          disabled:true
        })
      }else{
        this.coresOption.push({
          label:cor,
          value:cor,
          disabled:false
        })
      }
    }
    this.selectedCor = this.cores.find(e=>!this.coresSemEstoque.includes(e));
    this.selectedPhoto = this.produto.produtoBase.variacoes.find((e)=>e.cor===this.selectedCor)?.foto || this.produto.produtoBase.photoUrl;
  }

  getStockForTamanhoAndContextualCor(tamanho:string){
    const sku = this.getSkuByCorETamanho(this.selectedCor,tamanho);
    if(!sku){
      return 0;
    }
    return this.stock.estoque.find((e)=>e.sku===sku)?.estoque || 0;
  }

  getSkuByCorETamanho(cor:string|undefined,tamanho:string|undefined):string|undefined{
    if(!cor || !tamanho){
      return undefined;
    }
    return this.produto.produtoBase.variacoes.find((e)=>e.cor===cor && e.tamanho===tamanho)?.sku;
  }

  manageCorChange(){
    this.changeFocusedSku();
    this.overAddedTheSameSku = false;
    this.changeFotoAfterCorChange();
  }

  manageTamanhoChange(){
    this.changeFocusedSku();
    this.overAddedTheSameSku = false;
  }

  changeFocusedSku(){
    const sku = this.getSkuByCorETamanho(this.selectedCor,this.selectedTamanho);
    if(sku){
      this.focusedSku = sku;
    }
  }

  changeFotoAfterCorChange(){
    this.selectedPhoto = this.produto.produtoBase.variacoes.find((e)=>e.cor===this.selectedCor)?.foto || this.produto.produtoBase.photoUrl;
  }

  addToSacola(){
    const variacaoSelecionadaTransformed:Produto.ProdutoVariacao|undefined = this.produto.produtoBase.variacoes.find(e=>e.cor===this.selectedCor && e.tamanho===this.selectedTamanho);
    if(variacaoSelecionadaTransformed && this.selectedTamanho){
      const loja:Pedidos.LojaPedido = {
        nome:this.loja.loja,
        slug:this.loja.slug,
        systemId:this.loja.systemId
      };
      const quantidadeAtualDoItemSolicitadoNaSacola = this.sacolaService.getBeautySacolaForLoja(loja)?.itens.find(e=>e.systemId===variacaoSelecionadaTransformed.systemId)?.quantidade;
      if(quantidadeAtualDoItemSolicitadoNaSacola){
        if(this.getStockForTamanhoAndContextualCor(this.selectedTamanho)===quantidadeAtualDoItemSolicitadoNaSacola){
          this.overAddedTheSameSku = true;
          return;
        }
      }
      const produto:Pedidos.ProdutoPedido = {
        systemId:this.produto.produtoBase.systemId,
        nome:this.produto.produtoBase.descricao,
        sku:this.produto.produtoBase.sku,
        valorBase:this.produto.produtoBase.preco,
        variacoesCompradas:[{
          cor:variacaoSelecionadaTransformed.cor,
          fotoUrl:variacaoSelecionadaTransformed.foto,
          sku:variacaoSelecionadaTransformed.sku,
          systemId:variacaoSelecionadaTransformed.systemId,
          tamanho:variacaoSelecionadaTransformed.tamanho,
          valorBase:this.produto.produtoBase.preco
        }]
      }
      this.sacolaService.addToSacolaForLoja(loja,produto);
    }else{
      alert("Selecione uma opção")
    }
    this.sacolaService.notifySacolaChange();
  }


}
