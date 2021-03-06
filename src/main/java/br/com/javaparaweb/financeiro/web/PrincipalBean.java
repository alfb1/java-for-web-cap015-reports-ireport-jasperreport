package br.com.javaparaweb.financeiro.web;

import java.util.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.lancamento.Lancamento;
import br.com.javaparaweb.financeiro.lancamento.LancamentoRN;

@ManagedBean
public class PrincipalBean {
	private List<Lancamento> listaAteHoje;
	private List<Lancamento> listaFuturos;

	//@ManagedProperty(value = "#{ContextoBean}")
	@ManagedProperty( value = "#{contextoBean}" )
	private ContextoBean contextoBean;

	
	public List<Lancamento> getListaAteHoje() {
		if (this.listaAteHoje == null) {
			Conta conta = this.contextoBean.getContaAtiva();
			Calendar today = new GregorianCalendar();

			LancamentoRN lancamentoRN = new LancamentoRN();
			this.listaAteHoje = lancamentoRN.listar(conta, null, today.getTime());
		}

		return this.listaAteHoje;
	}

	public List<Lancamento> getListaFuturos() {
		if (this.listaFuturos == null) {
			Conta conta = this.contextoBean.getContaAtiva();
			Calendar tomorrow = new GregorianCalendar();
			tomorrow.add(Calendar.DAY_OF_MONTH, 1);

			LancamentoRN lancamentoRN = new LancamentoRN();
			this.listaFuturos = lancamentoRN.listar(conta, tomorrow.getTime(), null);
		}

		return this.listaFuturos;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public void setListaAteHoje(List<Lancamento> listaAteHoje) {
		this.listaAteHoje = listaAteHoje;
	}

	public void setListaFuturos(List<Lancamento> listaFuturos) {
		this.listaFuturos = listaFuturos;
	}

}
