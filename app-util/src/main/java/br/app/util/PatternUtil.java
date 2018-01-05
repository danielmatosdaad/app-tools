package br.app.util;

import java.util.regex.Pattern;

public class PatternUtil {

	public static boolean isNumberPositivo(String valor) {

		if (valor == null) {

			return true;
		}
		return Pattern.matches(RegexUtil.NUMBER_POSITIVO.getValue(), valor);

	}
	
	public static boolean isDataDDMMAAA(String valor) {

		if (valor == null) {

			return true;
		}
		return Pattern.matches(RegexUtil.DATA_DD_MM_AAAA.getValue(), valor);

	}


	private enum RegexUtil {

		NUMBER_POSITIVO("^[0-9]+$"),
		DATA_DD_MM_AAAA("(^(((0[1-9]|1[0-9]|2[0-8])[\\/](0[1-9]|1[012]))|((29|30|31)[\\/](0[13578]|1[02]))|((29|30)[\\/](0[4,6,9]|11)))[\\/](19|[2-9][0-9])\\d\\d$)|(^29[\\/]02[\\/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)");

		private RegexUtil(String valor) {

			this.value = valor;
		}

		private String value;

		public String getValue() {
			return value;
		}

	}
}
