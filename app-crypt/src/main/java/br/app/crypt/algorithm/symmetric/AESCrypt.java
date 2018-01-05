package br.app.crypt.algorithm.symmetric;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

import br.app.crypt.algorithm.SymmetricAlgorithm;

public class AESCrypt extends SymmetricCrypterImpl{

	/**
     * Indica o Algoritmo implementado
     */
    public static final SymmetricAlgorithm ALGORITHM = SymmetricAlgorithm.AES;

    /**
     * Cria uma nova instancia de AESCrypt, com a chave especificada.
     * 
     * @param key
     *            - Chave a ser utilizada
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public AESCrypt(final byte[] key) throws IOException, ClassNotFoundException {
	super(ALGORITHM, key);
    }

    /**
     * Cria uma nova instancia de AESCrypt, com a chave especificada.
     * 
     * @param key
     *            - Chave a ser utilizada
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     */
    public AESCrypt(final String key) throws IOException, ClassNotFoundException {
	super(ALGORITHM, key);
    }

    /**
     * Cria uma nova instancia de AESCrypt, com a chave gerada no momento da construção.
     * 
     */
    public AESCrypt() {
	super(ALGORITHM);
    }
}
