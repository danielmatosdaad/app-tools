package br.app.crypt.rsa;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

public class CryptKeyEspAES implements Crypt {

	private static final String AES = "AES";
	private Cipher cipher;
	private SecretKey secretKeySpec;
	private String chaveSimetrica;
	private String idUnico;

	public CryptKeyEspAES(String chaveSimetrica, String idUnico) {

		if (chaveSimetrica == null || idUnico == null) {
			throw new RuntimeException("A chave simetrica ou idUnico nao pode ser nulo");
		}
		if (chaveSimetrica.length() > 16 || idUnico.length() > 16) {
			throw new RuntimeException("A chave simetrica ou idUnico nao pode ter mais que 16 caracteres");
		}
		this.idUnico = idUnico;
		this.chaveSimetrica = chaveSimetrica;
	}

	@Override
	public void init() throws Exception {

		this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
		this.secretKeySpec = criarSecretKeySpec(this.chaveSimetrica);

	}

	@Override
	public String codifica(String mensagem) throws Exception {

		if (this.secretKeySpec == null || this.cipher == null) {
			return null;
		}
		byte[] cipherText = encryptText(mensagem, this.secretKeySpec);

		return new String(encodeBASE64(cipherText));
	}

	@Override
	public String decodificar(String decmensagem) throws Exception {

		if (this.secretKeySpec == null || this.cipher == null) {
			return null;
		}
		String decryptedText = decryptText(decodeBASE64(decmensagem.getBytes("UTF-8")), this.secretKeySpec);
		return decryptedText;
	}

	public static void main(String[] args) throws Exception {
		String plainText = "Hello joao";
		String key = "Bar12345Bar12345"; // 128 bit key
        String initVector = "RandomInitVector"; // 16 bytes IV
		CryptKeyEspAES aes = new CryptKeyEspAES(key,initVector);
		aes.init();
		System.out.println("Original Text:" + plainText);
		String codificado = aes.codifica(plainText);
		System.out.println("codificado Text:" + codificado);
		String decodificado = aes.decodificar(codificado);
		System.out.println("decodificado Text:" + decodificado);

	}

	public SecretKey criarSecretKeySpec(String chaveencriptacao)
			throws UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException {
		
        SecretKeySpec skeySpec = new SecretKeySpec(chaveencriptacao.getBytes("UTF-8"), "AES");
		return skeySpec;
	}


	private byte[] encryptText(String plainText, SecretKey secKey) throws Exception {
		IvParameterSpec iv = new IvParameterSpec(idUnico.getBytes("UTF-8"));
		this.cipher.init(Cipher.ENCRYPT_MODE, this.secretKeySpec, iv);
		byte[] byteCipherText = this.cipher.doFinal(plainText.getBytes("UTF-8"));
		return byteCipherText;
	}

	private String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
		// AES defaults to AES/ECB/PKCS5Padding in Java 7
		this.cipher.init(Cipher.DECRYPT_MODE, this.secretKeySpec, new IvParameterSpec(idUnico.getBytes("UTF-8")));
		byte[] bytePlainText = this.cipher.doFinal(byteCipherText);
		return new String(bytePlainText);
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

	private static String bytesToHex(byte[] hash) {
		return DatatypeConverter.printHexBinary(hash);
	}

}
