package br.com.javaparaweb.financeiro.web;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;

import br.com.javaparaweb.financeiro.usuario.Usuario;
import br.com.javaparaweb.financeiro.usuario.UsuarioRN;

import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.conta.ContaRN;

import br.com.javaparaweb.financeiro.util.RNException;

@ManagedBean(name = "usuarioBean")
@RequestScoped
public class UsuarioBean {
	private Usuario usuario = new Usuario();
	private String confirmarSenha;
	//--- a partir do cap 5
	private List<Usuario> lista;
	private String destinoSalvar;
	
	private Conta conta = new Conta();

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}

	
	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public String salvar() {
		FacesContext context = FacesContext.getCurrentInstance();

		String senha = this.usuario.getSenha();

		if (!senha.equals(this.confirmarSenha)) {
			FacesMessage facesMessage = new FacesMessage("A senha não foi confirmada corretamente");
			context.addMessage(null, facesMessage);
			return null;
		}

		UsuarioRN usuarioRN = new UsuarioRN();
		usuarioRN.salvar(usuario);
		
		//cap7 add a default account :
		
		if ( this.conta.getDescricao() != null ) {
			this.conta.setUsuario(this.usuario);
			this.conta.setFavorita(true);
			
			ContaRN contaRN = new ContaRN();
			contaRN.salvar(this.conta);
		}
		
		//chatp 14 emails :
		if ( this.destinoSalvar.equals("usuario_sucesso") ) 
		{
		    try {
		    	usuarioRN.enviarEmailPosCadastramento(this.usuario);
		    }catch(RNException e) {
		    	context.addMessage(null, new FacesMessage( e.getMessage() ));
		    	return null;
		    }
		}
		//befor in chap 4
		//return "usuario_sucesso";
		//now :
		return this.destinoSalvar;
	}

	public String novo() {
		this.destinoSalvar = "usuario_sucesso";
		this.usuario = new Usuario();
		this.usuario.setAtivo(true);
		return "/publico/usuario";
	}
	
	public String editar() {
		 this.confirmarSenha = this.usuario.getSenha();
		 return "/public/usuario";
	}
	
	public String excluir() {
		UsuarioRN usuarioRN = new UsuarioRN();
		usuarioRN.excluir(this.usuario);
		
		this.lista = null;
		return null;
	}
	
	public String ativar() {
		
		if (this.usuario.isAtivo())
			this.usuario.setAtivo(false);
		else
			this.usuario.setAtivo(true);
		
		UsuarioRN usuarioRN = new UsuarioRN();
		usuarioRN.salvar(this.usuario);
		
		return null;

	}
	
	public List<Usuario> getLista(){
		
		if (this.lista == null) {
			UsuarioRN usuarioRN = new UsuarioRN();
			this.lista = usuarioRN.listar();
		}
		
		return this.lista;
	}

	public String getDestinoSalvar() {
		return destinoSalvar;
	}

	public void setDestinoSalvar(String destinoSalvar) {
		this.destinoSalvar = destinoSalvar;
	}
	
	//cap. 6
	public String atribuiPermissao(Usuario usuario, String permissao) {
		this.usuario = usuario;
		java.util.Set<String> permissoes = this.usuario.getPermissao();
		
		if (permissoes.contains(permissao)) {
			permissoes.remove(permissao);
		}else {
			permissoes.add(permissao);
		}
		
		return null;
	}
	

}
