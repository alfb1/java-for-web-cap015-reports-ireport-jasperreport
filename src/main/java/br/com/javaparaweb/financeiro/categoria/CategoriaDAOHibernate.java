package br.com.javaparaweb.financeiro.categoria;

import java.util.List;
import br.com.javaparaweb.financeiro.usuario.Usuario;

import org.hibernate.*;

public class CategoriaDAOHibernate implements CategoriaDAO {
    private Session session;
    
    public void setSession(Session session) {
    	this.session = session;
    }
    
	@Override
	public Categoria salvar(Categoria categoria) {
		//avoid the message :
		//org.hibernate.NonUniqueObjectException: a different object with the same identifier value
		//was already associated with the sesion : [financeiro.categoria.Categoria#123]
		//
		Categoria merged =  (Categoria) this.session.merge(categoria);
		
		//sincronize with database
		this.session.flush();
		//reload objects in memory
		this.session.clear();
		
		return merged;
	}

	@Override
	public void excluir(Categoria categoria) {
		//loads its children with exist for cascade delete
        categoria = (Categoria) this.carregar(categoria.getCodigo());
       
        this.session.delete(categoria);
	    this.session.flush();
		this.session.clear();       
	}

	@Override
	public Categoria carregar(Integer categoria) {
		return (Categoria) this.session.get(Categoria.class, categoria);
	}

	@Override
	public List<Categoria> listar(Usuario usuario) {
		String hql = "select c from Categoria c where c.pai is null and c.usuario = :usuario";
		Query query = this.session.createQuery(hql);
		
		query.setInteger("usuario", usuario.getCodigo());
		List<Categoria> lista = query.list();
		
		return lista;
	}

}
