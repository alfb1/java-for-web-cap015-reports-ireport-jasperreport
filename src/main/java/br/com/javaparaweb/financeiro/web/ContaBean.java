package br.com.javaparaweb.financeiro.web;

import java.util.List;
import javax.faces.bean.*;
import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.conta.ContaRN;

//Chapt 15
import org.primefaces.model.StreamedContent;
import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

import br.com.javaparaweb.financeiro.web.util.RelatorioUtil;
import br.com.javaparaweb.financeiro.util.UtilException;

import java.util.HashMap;

@ManagedBean
@RequestScoped
public class ContaBean {
	private Conta contaSelecionada = new Conta();
	private List<Conta> lista = null;

	@ManagedProperty(value = "#{contextoBean}")
	private ContextoBean contextoBean;
	
	//chapt 15
	private StreamedContent arquivoRetorno;
	private int tipoRelatorio;

	public String salvar() 
	{
		this.contaSelecionada.setUsuario(this.contextoBean.getUsuarioLogado());
		
		//usando a camada de regra de negocios para salvar o objeto
		ContaRN contaRN = new ContaRN();
		contaRN.salvar(this.contaSelecionada);
		
		this.contaSelecionada = new Conta();
		// força reload da lista
		this.lista = null;
		return null;
	}

	public String excluir() 
	{
		ContaRN contaRN = new ContaRN();
		contaRN.excluir(this.contaSelecionada);
		
		this.contaSelecionada = new Conta();
		this.lista = null;
		return null;
	}
	
	public List<Conta> getLista() 
	{
		if ( this.lista == null ) {
			ContaRN contaRN = new ContaRN();
			this.lista = contaRN.listar(this.contextoBean.getUsuarioLogado());
		}
		
		return this.lista;
	}
	
	public String tornarFavorita() 
	{
		ContaRN contaRN = new ContaRN();
		contaRN.tornarFavorita(this.contaSelecionada);
		
		this.contaSelecionada = new Conta();
		return null;
	}

	public Conta getContaSelecionada() {
		return contaSelecionada;
	}

	public void setContaSelecionada(Conta contaSelecionada) {
		this.contaSelecionada = contaSelecionada;
	}



	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}
	
	// Chapt 15
	
	public StreamedContent getArquivoRetorno()
	{
		FacesContext context = FacesContext.getCurrentInstance();
		
		String usuario = contextoBean.getUsuarioLogado().getLogin();
		String nomeRelatorioJasper = "contas";
		String nomeRelatorioSaida  = usuario + "_contas";
		
		RelatorioUtil relatorioUtil = new RelatorioUtil();
		
		HashMap parametrosRelatorio = new HashMap();
		parametrosRelatorio.put( "codigoUsuario", contextoBean.getUsuarioLogado().getCodigo());
		
		try {
			this.arquivoRetorno = relatorioUtil.geraRelatorio(parametrosRelatorio, nomeRelatorioJasper, nomeRelatorioSaida, this.tipoRelatorio);
		}catch( UtilException e) 
		{
			context.addMessage( null, new FacesMessage(e.getCause().getMessage() ));
		}
		
		return this.arquivoRetorno;
	}

	public int getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(int tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
	
	

}
