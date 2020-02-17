package br.com.javaparaweb.financeiro.web.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

import java.util.Locale;

public class MensagemUtil {
    // chapt 14
	private static final String PACOTE_MENSAGENS_IDIOMAS = "br.com.javaparaweb.idioma.mensagens";
	
	public static String getMensagem(String propriedade) 
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResourceBundle resourceBundle = facesContext.getApplication().getResourceBundle(facesContext, "msg");
		
		return resourceBundle.getString(propriedade);
	}
	
	
	public static String getMensagem(String propriedade, Object...parametros) 
	{
		String mensagem = getMensagem(propriedade);
		MessageFormat messageFormat = new MessageFormat(mensagem);
		
		return messageFormat.format(parametros);
	}
	
	// chapt 14
	public static String getMensagem(Locale locale, String propriedade) 
	{
		ResourceBundle resourceBundle = ResourceBundle.getBundle(PACOTE_MENSAGENS_IDIOMAS, locale);
		
		return resourceBundle.getString(propriedade);
	}
	
	// chapt 14
	public static String getMensagem(Locale locale, String propriedade, Object...parametros) 
	{
		String mensagem = getMensagem(locale, propriedade);
		MessageFormat messageFormat = new MessageFormat(mensagem);
		
		return messageFormat.format(parametros);
	}
	
	
}
