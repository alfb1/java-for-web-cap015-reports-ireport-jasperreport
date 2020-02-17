package br.com.javaparaweb.financeiro.bolsa.acao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.javaparaweb.financeiro.usuario.Usuario;
import br.com.javaparaweb.financeiro.util.DAOFactory;
import br.com.javaparaweb.financeiro.util.RNException;
import br.com.javaparaweb.financeiro.web.util.YahooFinanceiroUtil;


public class AcaoRN 
{
   private AcaoDAO acaoDAO;
   
   public AcaoRN() {
	   this.acaoDAO = DAOFactory.criarAcaoDAO();
   }
   
   public void salvar(Acao acao) {
	   this.acaoDAO.salvar(acao);
   }
   
   public void excluir(Acao acao) {
	   this.acaoDAO.excluir(acao);
   }
   
   public List<Acao> listar(Usuario usuario){
	   return this.acaoDAO.listar(usuario);
   }
   
   public List<AcaoVirtual> listarAcaoVirtual(Usuario usuario) throws RNException {
		List<Acao> listaAcao = null;
		List<AcaoVirtual> listaAcaoVirtual = new ArrayList<AcaoVirtual>();
		AcaoVirtual acaoVirtual = null;
		String cotacao = null;
		float ultimoPreco = 0.0f;
		float total = 0.0f;
		int quantidade = 0;

		try {
			listaAcao = this.listar(usuario);
			for (Acao acao : listaAcao) {
				acaoVirtual = new AcaoVirtual();
				acaoVirtual.setAcao(acao);
				cotacao = this.retornaCotacao(YahooFinanceiroUtil.ULTIMO_PRECO_DIA_ACAO_INDICE, acao);
				if (cotacao != null) {
					ultimoPreco = new Float(cotacao).floatValue();
					quantidade = acao.getQuantidade();
					total = ultimoPreco * quantidade;
					acaoVirtual.setUltimoPreco(ultimoPreco);
					acaoVirtual.setTotal(total);
					listaAcaoVirtual.add(acaoVirtual);
				}
			}
		} catch (RNException e) {
			throw new RNException("Não foi possível listar ações. Erro: " + e.getMessage());
		}
		return listaAcaoVirtual;
	}

	public String retornaCotacao(int indiceInformacao, Acao acao) throws RNException {
		YahooFinanceiroUtil yahooFinanceUtil = null;
		String informacao = null;
		try {
			yahooFinanceUtil = new YahooFinanceiroUtil(acao);
			informacao = yahooFinanceUtil.retornaCotacao(indiceInformacao, acao.getSigla());
		} catch (IOException e) {
			throw new RNException("Não foi possível recuperar cotação. Erro: " + e.getMessage());
		}
		return informacao;
	}

	public String montaLinkAcao(Acao acao) {
		String link = null;
		if (acao != null) {
			if (acao.getOrigem() != null) {
				if (acao.getOrigem() == YahooFinanceiroUtil.ORIGEM_BOVESPA) {
					link = acao.getSigla() + YahooFinanceiroUtil.POSFIXO_ACAO_BOVESPA;
				} else {
					link = acao.getSigla();
				}
			} else {
				link = YahooFinanceiroUtil.INDICE_BOVESPA;
			}
		} else {
			link = YahooFinanceiroUtil.INDICE_BOVESPA;
		}
		return link;
	}
}
