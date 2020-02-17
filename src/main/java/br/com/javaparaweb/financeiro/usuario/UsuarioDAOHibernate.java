package br.com.javaparaweb.financeiro.usuario;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class UsuarioDAOHibernate implements UsuarioDAO {
	
	private Session session;
	
	public void setSession(Session session) {
		this.session = session;
	}

	public void salvar(Usuario usuario) {
       // usuario without permissao loaded yet
       if (usuario.getPermissao() == null || usuario.getPermissao().size() ==0) {
    	   // load the new usuario instance from database
    	   Usuario usuarioPermissao = this.carregar(usuario.getCodigo());
    	   // fill actual usuario with permissao
    	   usuario.setPermissao(usuarioPermissao.getPermissao());
    	   // delete from persistent context the object usuarioPermissao
    	   // evict avoids the error "NonUniqueObjectException: a different object with the same identifier value was already asssociated with 
    	   // the session : [br....usario.Usuario#3]".
    	   this.session.evict(usuarioPermissao);
       }
       
       this.session.save(usuario);
	}

	public void atualizar(Usuario usuario) {
       this.session.update(usuario);
	}
	
	public void excluir(Usuario usuario) {
       this.excluir(usuario);
	}
	
	public Usuario carregar(Integer codigo) {
       return (Usuario) this.session.get(Usuario.class, codigo);
	}
	
	@SuppressWarnings("unchecked")
	public List<Usuario> listar() {
		return this.session.createCriteria(Usuario.class).list();
	}
	
	public Usuario buscarPorLogin(String login) {
		// uma selecao no objeto Usuario - hql (Hibernate query language)
		String hql = "select u from Usuario u where u.login = :login";
		// utilizando o session interno para gerar a consulta
		Query query = this.session.createQuery(hql);
		// setando o parametro login do hql para o login passado no metodo
		query.setString("login", login);
		// retornando apenas um resultado na consulta
		return (Usuario) query.uniqueResult();
	}



}
