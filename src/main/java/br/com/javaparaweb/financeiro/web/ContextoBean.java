package br.com.javaparaweb.financeiro.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.event.ValueChangeEvent;

import br.com.javaparaweb.financeiro.conta.*;
import br.com.javaparaweb.financeiro.usuario.*;
//chapt 11
import java.util.Locale;
import java.util.ArrayList;
import java.util.Iterator;

@ManagedBean
//your life time is while the user is logged
@SessionScoped
public class ContextoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4693275947797122717L;
	private int codigoContaAtiva       = 0;
	//chapt 11
	private List<Locale> idiomas;
	private Conta	contaAtiva	       = null;
	
	public Usuario getUsuarioLogado() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		String login = externalContext.getRemoteUser();
        
		if (login != null) {
			UsuarioRN usuarioRN = new UsuarioRN();
			//chapt 11
			//return usuarioRN.buscarPorLogin(login);
			Usuario usuario = usuarioRN.buscarPorLogin(login);
			String[] info = usuario.getIdioma().split("_");
			
			Locale locale = new Locale(info[0], info[1]);
			
			facesContext.getViewRoot().setLocale(locale);
			
			return usuario;
		}

		return null;
	}

	public Conta getContaAtiva() {
		if (this.contaAtiva == null) {
			Usuario usuario = this.getUsuarioLogado();

			ContaRN contaRN = new ContaRN();
			this.contaAtiva = contaRN.buscarFavorita(usuario);
			
			
			if (this.contaAtiva == null) {
				List<Conta> contas = contaRN.listar(usuario);
				if (contas != null) {
					for (Conta conta : contas) {
						this.contaAtiva = conta;
						break;
					}
				}
			}
		}
		return this.contaAtiva;
	}


	public void setContaAtiva(ValueChangeEvent event) {

		Integer conta = (Integer) event.getNewValue();

		ContaRN contaRN = new ContaRN();
		this.contaAtiva = contaRN.carregar(conta);
	}
	
	//chapt 11
	public List<Locale> getIdiomas(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Iterator<Locale> locales = facesContext.getApplication().getSupportedLocales();
		this.idiomas = new ArrayList<Locale>();
		
		while(locales.hasNext()) {
			this.idiomas.add(locales.next());
		}
		
		return this.idiomas;
	}
	
	//chapt 11
	public void setIdiomaUsuario(String idioma)
	{
		Usuario usuario = this.getUsuarioLogado();
		usuario.setIdioma(idioma);
		
		UsuarioRN usuarioRN = new UsuarioRN();
		usuarioRN.salvar(usuario);
		
		String[]  info = idioma.split("_");
		Locale locale = new Locale(info[0], info[1]);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.getViewRoot().setLocale(locale);
		
	}

}
