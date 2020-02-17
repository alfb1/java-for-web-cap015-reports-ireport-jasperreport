package br.com.javaparaweb.financeiro.cheque;

import java.util.Date;
import java.util.List;

import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.lancamento.Lancamento;
import br.com.javaparaweb.financeiro.util.DAOFactory;
import br.com.javaparaweb.financeiro.util.RNException;

public class ChequeRN {
   private ChequeDAO chequeDAO;
   
   public ChequeRN() {
	   this.chequeDAO = DAOFactory.criarChequeDAO();
   }
   
   public void salvar(Cheque cheque) {
	   this.chequeDAO.salvar(cheque);
   }
   
   public int salvarSequencia(Conta conta, Integer chequeInicial, Integer chequeFinal) 
   {
	   Cheque cheque = null;
	   ChequeId chequeId = null;
	   int contaTotal = 0;
	   
	   for( int i = chequeInicial; i <= chequeFinal; i++) {
		   chequeId = new ChequeId( conta.getConta(), i);
		   cheque = this.carregar( chequeId );
		   
		   if ( cheque == null ) {
			   cheque = new Cheque();
			   cheque.setChequeId(chequeId);
			   cheque.setSituacao(Cheque.SITUACAO_CHEQUE_NAO_EMITIDO);
			   cheque.setDataCadastro(new Date());
			   
			   this.salvar(cheque);
			   contaTotal++;
		   }
	   }
	   
	   return contaTotal;
   }
   
   public void excluir(Cheque cheque) throws RNException
   {
	   if ( cheque.getSituacao() == Cheque.SITUACAO_CHEQUE_NAO_EMITIDO )
	   {
		   this.chequeDAO.excluir(cheque);
	   }
	   else {
		  throw new RNException("Erro ao excluir, operação não permitida para o status do cheque.");
	   }
   }
   
   public Cheque carregar(ChequeId chequeId) {
	   return this.chequeDAO.carregar(chequeId);
   }
   
   public List<Cheque> listar(Conta conta){
	   return this.chequeDAO.listar(conta);
   }
   
   public void cancelarCheque( Cheque cheque ) throws RNException
   {
	   Character situacao = cheque.getSituacao();
	   
	   if ( situacao == Cheque.SITUACAO_CHEQUE_NAO_EMITIDO || situacao == Cheque.SITUACAO_CHEQUE_CANCELADO) 
	   {
		  cheque.setSituacao(Cheque.SITUACAO_CHEQUE_CANCELADO);   
	   }else
	   {
		   throw new RNException("Erro ao cancelar, operação não permitida para esse status.");
	   }
	   
   }
   
   public void baixarCheque(ChequeId chequeId, Lancamento lancamento)
   {
	   Cheque cheque = this.carregar(chequeId);
	   
	   if ( cheque != null ) {
		   cheque.setSituacao(Cheque.SITUACAO_CHEQUE_BAIXADO);
		   cheque.setLancamento(lancamento);
		   this.chequeDAO.salvar(cheque);
	   }
   }
   
   public void desviculaLancamento( Conta conta, Integer numeroCheque ) throws RNException
   {
	   ChequeId chequeId = new ChequeId( conta.getConta(), numeroCheque );
	   Cheque cheque = this.carregar(chequeId);
	   
	   if ( cheque == null ) {
		   return;
	   }
	   
	   if ( cheque.getSituacao() == Cheque.SITUACAO_CHEQUE_CANCELADO ) {
		   throw new RNException("Não é possível usar um cheque cancelado.");
	   }else 
	   {
		   cheque.setSituacao(Cheque.SITUACAO_CHEQUE_NAO_EMITIDO);
		   cheque.setLancamento(null);
		   this.salvar(cheque);
	   }
   }
}
