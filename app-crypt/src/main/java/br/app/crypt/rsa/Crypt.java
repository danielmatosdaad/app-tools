package br.app.crypt.rsa;


public interface Crypt {

	public void init() throws Exception;

	public String codifica(String mensagem) throws Exception;

	public String decodificar(String decmensagem) throws Exception;
}
