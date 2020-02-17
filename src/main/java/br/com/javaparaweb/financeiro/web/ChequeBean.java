package br.com.javaparaweb.financeiro.web;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.javaparaweb.financeiro.cheque.Cheque;
import br.com.javaparaweb.financeiro.cheque.ChequeRN;
import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.util.RNException;
import br.com.javaparaweb.financeiro.web.util.MensagemUtil;

@ManagedBean(name = "chequeBean")
@RequestScoped
public class ChequeBean {
	private Cheque chequeSelecionado = new Cheque();
	private List<Cheque> lista = null;
	private Integer chequeInicial;
	private Integer chequeFinal;

	@ManagedProperty(value = "#{contextoBean}")
	private ContextoBean contextoBean;

	public void salvar() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Conta conta = contextoBean.getContaAtiva();

		if (this.chequeInicial == null || this.chequeFinal == null) {
			String texto = MensagemUtil.getMensagem("cheque_erro_sequencia");
			facesContext.addMessage(null, new FacesMessage(texto));
		} else if (this.chequeFinal < this.chequeInicial) {
			String texto = MensagemUtil.getMensagem("cheque_erro_inicial_final");
			facesContext.addMessage(null, new FacesMessage(texto));
		} else {
			ChequeRN chequeRN = new ChequeRN();

			int totalCheques = chequeRN.salvarSequencia(conta, this.chequeInicial, this.chequeFinal);

			String texto = MensagemUtil.getMensagem("cheque_info_cadastrado", this.chequeFinal, totalCheques);
			facesContext.addMessage(null, new FacesMessage(texto));
			this.lista = null;
		}
	}

	public void excluir() {
		ChequeRN chequeRN = new ChequeRN();

		try {
			chequeRN.excluir(this.chequeSelecionado);

		} catch (RNException e) {
			String texto = MensagemUtil.getMensagem("cheque_erro_excluir");
			this.setMensagemUtil(texto);
		}

		this.lista = null;
	}

	public void cancelar() {
		ChequeRN chequeRN = new ChequeRN();

		try {
			chequeRN.cancelarCheque(this.chequeSelecionado);

		} catch (RNException e) {
			String texto = MensagemUtil.getMensagem("cheque_erro_cancelar");
			this.setMensagemUtil(texto);
		}

		this.lista = null;
	}

	public List<Cheque> getLista() {
		if (this.lista == null) {
			Conta conta = contextoBean.getContaAtiva();
			ChequeRN chequeRN = new ChequeRN();
			this.lista = chequeRN.listar(conta);
		}

		return this.lista;
	}

	public void setMensagemUtil(String texto) {
		FacesContext facesContext = FacesContext.getCurrentInstance();

		FacesMessage facesMessage = new FacesMessage(texto);
		facesMessage.setSeverity(FacesMessage.SEVERITY_WARN);

		facesContext.addMessage(null, facesMessage);
	}

	public Cheque getChequeSelecionado() {
		return chequeSelecionado;
	}

	public void setChequeSelecionado(Cheque chequeSelecionado) {
		this.chequeSelecionado = chequeSelecionado;
	}

	public Integer getChequeInicial() {
		return chequeInicial;
	}

	public void setChequeInicial(Integer chequeInicial) {
		this.chequeInicial = chequeInicial;
	}

	public Integer getChequeFinal() {
		return chequeFinal;
	}

	public void setChequeFinal(Integer chequeFinal) {
		this.chequeFinal = chequeFinal;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public void setLista(List<Cheque> lista) {
		this.lista = lista;
	}

}
