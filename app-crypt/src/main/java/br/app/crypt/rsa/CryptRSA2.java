package br.app.crypt.rsa;

import java.io.PrintStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CryptRSA2 implements Crypt {
	public static final String ALGORITHM = "RSA";
	private KeyPair keyPair;
	private Cipher cipher;
	static PrintStream _log = System.out;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public CryptRSA2() {

	}

	/**
	 * Generate key which contains a pair of privae and public key using 1024
	 * bytes
	 * 
	 * @return key pair
	 * @throws NoSuchAlgorithmException
	 */
	public KeyPair generateKey() throws NoSuchAlgorithmException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		keyGen.initialize(1024);
		KeyPair key = keyGen.generateKeyPair();
		return key;
	}

	/**
	 * Encrypt a text using public key.
	 * 
	 * @param text
	 *            The original unencrypted text
	 * @param key
	 *            The public key
	 * @return Encrypted text
	 * @throws java.lang.Exception
	 */
	public byte[] encrypt(byte[] text, PublicKey key) throws Exception {
		byte[] cipherText = null;
		try {
			_log.println("\nProvider is: " + this.cipher.getProvider().getInfo());
			_log.println("\nStart encryption with public key");

			this.cipher.init(Cipher.ENCRYPT_MODE, key);
			cipherText = this.cipher.doFinal(text);
		} catch (Exception e) {
			throw e;
		}
		return cipherText;
	}

	/**
	 * Encrypt a text using public key. The result is enctypted BASE64 encoded
	 * text
	 * 
	 * @param text
	 *            The original unencrypted text
	 * @param key
	 *            The public key
	 * @return Encrypted text encoded as BASE64
	 * @throws java.lang.Exception
	 */
	private String encrypt(String text, PublicKey key) throws Exception {
		String encryptedText;
		try {
			byte[] cipherText = encrypt(text.getBytes("UTF8"), key);
			encryptedText = new String(encodeBASE64(cipherText));
			_log.println("Enctypted text is: " + encryptedText);
		} catch (Exception e) {
			throw e;
		}
		return encryptedText;
	}

	/**
	 * Decrypt text using private key
	 * 
	 * @param text
	 *            The encrypted text
	 * @param key
	 *            The private key
	 * @return The unencrypted text
	 * @throws java.lang.Exception
	 */
	private byte[] decrypt(byte[] text, PrivateKey key) throws Exception {
		byte[] dectyptedText = null;
		try {
			// decrypt the text using the private key
			_log.println("Start decryption");
			this.cipher.init(Cipher.DECRYPT_MODE, key);
			dectyptedText = cipher.doFinal(text);
		} catch (Exception e) {
			throw e;
		}
		return dectyptedText;

	}

	@Override
	public void init() {

		try {

			this.keyPair = generateKey();
			this.cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (Exception e) {

		}

	}

	@Override
	public String codifica(String mensagem) throws Exception {

		if (mensagem != null && this.keyPair != null) {
			String mensagemCriptografada = new String(encrypt(mensagem, this.keyPair.getPublic()));
			return mensagemCriptografada;
		}
		return null;
	}

	/**
	 * Decrypt BASE64 encoded text using private key
	 * 
	 * @param text
	 *            The encrypted text, encoded as BASE64
	 * @param key
	 *            The private key
	 * @return The unencrypted text encoded as UTF8
	 * @throws java.lang.Exception
	 */
	private String decrypt(String text, PrivateKey key) throws Exception {
		String result;
		try {
			// decrypt the text using the private key
			byte[] dectyptedText = decrypt(decodeBASE64(text.getBytes()), key);
			result = new String(dectyptedText, "UTF8");
			_log.println("Decrypted text is: " + result);
		} catch (Exception e) {
			throw e;
		}
		return result;

	}

	@Override
	public String decodificar(String mensagem) throws Exception {

		try {
			if (mensagem != null && this.keyPair != null) {
				return this.decrypt(mensagem, this.keyPair.getPrivate());
			}

		} catch (Exception e) {

			throw new Exception(e.getMessage());
		}
		return null;

	}

	public static void main(String[] args) throws Exception {

		CryptRSA2 rsa = new CryptRSA2();
		rsa.init();
		String codificado = rsa.codifica("daniel pereira #$#$#$#%&%^^&(*&&*)");
		String decodificado = rsa.decodificar(codificado);
		System.out.println(codificado);
		System.out.println(decodificado);
	}

	private static byte[] encodeBASE64(byte[] bytes) {

		byte[] encodedBytes = Base64.encodeBase64(bytes);
		System.out.println("encodedBytes " + new String(encodedBytes));

		return encodedBytes;
	}

	private static byte[] decodeBASE64(byte[] bytes) {

		byte[] decodedBytes = Base64.decodeBase64(bytes);
		System.out.println("decodedBytes " + new String(decodedBytes));
		return decodedBytes;
	}

}
