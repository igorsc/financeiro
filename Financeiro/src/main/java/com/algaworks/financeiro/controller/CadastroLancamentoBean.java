package com.algaworks.financeiro.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.financeiro.model.Lancamento;
import com.algaworks.financeiro.model.Pessoa;
import com.algaworks.financeiro.model.TipoLancamento;
import com.algaworks.financeiro.repository.Lancamentos;
import com.algaworks.financeiro.repository.Pessoas;
import com.algaworks.financeiro.service.CadastroLancamentos;
import com.algaworks.financeiro.service.NegocioException;


@Named
@javax.faces.view.ViewScoped
public class CadastroLancamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CadastroLancamentos cadastro;
	
	@Inject
	private Pessoas pessoas;
	
	@Inject
	private Lancamentos lancamentos;
	
	private Lancamento lancamento = new Lancamento();
	private List<Pessoa> todasPessoas;
	
	public void prepararCadastro() {
		
		this.todasPessoas=pessoas.todas();
		if (this.lancamento==null) {
			this.lancamento = new Lancamento();
			
		}
    }
	
	public void salvar(){

		FacesContext context = FacesContext.getCurrentInstance();
		
		try {
			
			cadastro.salvar(this.lancamento);
			
			this.lancamento=new Lancamento();
			context.addMessage(null, new FacesMessage("Lançamento salvo com sucesso!"));
			
			
		} catch (NegocioException e) {
			// TODO: handle exception
			
			FacesMessage message = new FacesMessage(e.getMessage());
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
		} 
	
	}
	
	public void descricaoModificada(ValueChangeEvent event) {
		System.out.println("Valor antigo: " + event.getOldValue());
		System.out.println("Novo valor: " + event.getNewValue());
		FacesContext.getCurrentInstance().renderResponse();
	}
	
	public void dataVencimentoAlterada(AjaxBehaviorEvent event){
		if (this.lancamento.getDataPagamento() == null) {
			this.lancamento.setDataPagamento(this.lancamento.getDataVencimento());
		}
	}
	
	public List<String> pesquisarDescricoes(String descricao){
		return this.lancamentos.descricoesQueContem(descricao);
	}
	

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public List<Pessoa> getTodasPessoas() {
		return todasPessoas;
	}

	public TipoLancamento[] getTiposLancamentos() {
		return TipoLancamento.values();
	}

}
