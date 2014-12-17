package pt.ipleiria.sas.mobile.cantinaipl.parser;

public class FormatString {

	
	public String stringToDecimal(String val) {
		int dotPos = -1;
		String str = new String();

		for (int i = 0; i < val.length(); i++) {
			char c = val.charAt(i);
			if (c == '.')
				dotPos = i;
		}

		if (dotPos == -1) {
			str = (val + ".00");
		} else {
			if (val.length() - dotPos == 1) {
				str = (val + "00");
			} else if (val.length() - dotPos == 2) {
				str = (val + "0");
			} else {
				str = val;
			}
		}
		return str;
	}
	
	public String dotToComma(String val){
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < val.length(); i++) {
			char c = val.charAt(i);
			if (c == '.')
				c = ',';
			str.append(c);
		}
		return str.toString();
	}

}
