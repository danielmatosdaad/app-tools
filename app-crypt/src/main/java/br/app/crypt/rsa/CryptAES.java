package br.app.crypt.rsa;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

public class CryptAES implements Crypt {

	protected static final String AES = "AES";
	protected SecretKey secKey;
	protected Cipher cipher;

	public CryptAES() {
	}

	@Override
	public void init() throws Exception {

		this.cipher = Cipher.getInstance(AES);
		this.secKey = getSecretEncryptionKey();
	}

	@Override
	public String codifica(String mensagem) throws Exception {

		if (this.secKey == null || this.cipher == null) {
			return null;
		}
		byte[] cipherText = encryptText(mensagem, this.secKey);

		return new String(encodeBASE64(cipherText));
	}

	@Override
	public String decodificar(String decmensagem) throws Exception {
		if (this.secKey == null || this.cipher == null) {
			return null;
		}
		String decryptedText = decryptText(decodeBASE64(decmensagem.getBytes("UTF-8")), this.secKey);
		return decryptedText;
	}

	public static void main(String[] args) throws Exception {
		String plainText = "Hello joao";
		CryptAES aes = new CryptAES();
		aes.init();
		System.out.println("Original Text:" + plainText);
		String codificado = aes.codifica(plainText);
		System.out.println("codificado Text:" + codificado);
		String decodificado = aes.decodificar(codificado);
		System.out.println("decodificado Text:" + decodificado);

	}

	public SecretKeySpec criarSecretKeySpec(Cipher cipher, String chaveencriptacao, String idUnico)
			throws UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException {

		SecretKeySpec key = new SecretKeySpec(chaveencriptacao.getBytes("UTF-8"), AES);
		return key;
	}

	private SecretKey getSecretEncryptionKey() throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance(AES);
		generator.init(128); // The AES key size in number of bits
		SecretKey secKey = generator.generateKey();
		return secKey;
	}

	private byte[] encryptText(String plainText, SecretKey secKey) throws Exception {
		this.cipher.init(Cipher.ENCRYPT_MODE, secKey);
		byte[] byteCipherText = this.cipher.doFinal(plainText.getBytes());
		return byteCipherText;
	}

	private String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
		// AES defaults to AES/ECB/PKCS5Padding in Java 7
		this.cipher.init(Cipher.DECRYPT_MODE, secKey);
		byte[] bytePlainText = this.cipher.doFinal(byteCipherText);
		return new String(bytePlainText);
	}

	protected static byte[] encodeBASE64(byte[] bytes) {

		byte[] encodedBytes = Base64.encodeBase64(bytes);
		System.out.println("encodedBytes " + new String(encodedBytes));

		return encodedBytes;
	}

	protected static byte[] decodeBASE64(byte[] bytes) {

		byte[] decodedBytes = Base64.decodeBase64(bytes);
		System.out.println("decodedBytes " + new String(decodedBytes));
		return decodedBytes;
	}

	private static String bytesToHex(byte[] hash) {
		return DatatypeConverter.printHexBinary(hash);
	}

}
