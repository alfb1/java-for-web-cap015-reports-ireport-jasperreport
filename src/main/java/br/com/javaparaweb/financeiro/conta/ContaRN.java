package br.com.javaparaweb.financeiro.conta;

import java.util.List;
import java.util.Date;

import br.com.javaparaweb.financeiro.usuario.Usuario;
import br.com.javaparaweb.financeiro.util.DAOFactory;

public class ContaRN {
	private ContaDAO contaDAO;
	
	public ContaRN() {
		this.contaDAO = DAOFactory.criarContaDAO();
	}
	
	public List<Conta> listar(Usuario usuario){
		return this.contaDAO.listar(usuario);
	}
			
	public Conta carregar(Integer codigo) {
		return this.contaDAO.carregar(codigo);
	}
	  
	public void salvar(Conta conta) {
	    conta.setDataCadastro(new Date());
	    
	    this.contaDAO.salvar(conta);
	}
	
	public void excluir(Conta conta) {
		this.contaDAO.excluir(conta);
	}
	
	public Conta buscarFavorita(Usuario usuario) {
		return this.contaDAO.buscarFavorita(usuario);
	}
	
	public void tornarFavorita(Conta favorita) {
		Conta conta = this.buscarFavorita(favorita.getUsuario());
		
		if ( conta != null) {
			conta.setFavorita(false);
			this.contaDAO.salvar(conta);
		}
		
		favorita.setFavorita(true);
		
		this.contaDAO.salvar(favorita);
	}
}
