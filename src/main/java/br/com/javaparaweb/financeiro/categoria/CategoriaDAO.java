package br.com.javaparaweb.financeiro.categoria;

import br.com.javaparaweb.financeiro.usuario.Usuario;
import java.util.List ;

public interface CategoriaDAO 
{
   public Categoria salvar( Categoria categoria );
   public void excluir( Categoria categoria );
   public Categoria carregar( Integer categoria );
   public List<Categoria> listar( Usuario usuario );
}
