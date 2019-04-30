package com.docomo.fraudwall.tracking.util;

import java.util.Random;

public class TokenGenerator {

	static final String[] UPPER = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	static final String[] LOWER = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
			"r", "s", "t", "u", "v", "w", "x", "y", "z" };
	static final String[] DIGIT = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	private static final int DEFAULTLENGTH = 18;
	private static final String FRAUDWALL_IDENTIF = "FW";
	private static Random genNum = new Random();
	private int arrTam = 0;
	private int contL = 0;
	private int contU = 0;
	private String res = "";

	private int length;

	public TokenGenerator() {
		super();
		this.length = DEFAULTLENGTH;
	}

	public TokenGenerator(int length) {
		super();
		this.length = length;
	}

	public String generate() {
		String pass = "";

		String timeStamp = getTimestamp();
		// Avoid using timestamp as token if the length doesn�t allow generating a
		// random token
		if (timeStamp.length() >= length) {
			timeStamp = "";
		}
		for (int i = 0; i < (length - timeStamp.length()); i++) {
			if ((contL == 2) || (contU) == 1) {
				arrTam = 9;
				// Adding a digit to password				
				pass = pass.concat( getDigit(genNum()));
				contL = 0;
				contU = 0;
			} else {
				arrTam = 26;
				if (genNum() % 2 == 0) {
					// Adding a Upper character to password					
					pass = pass.concat(getUpper(genNum()));					
					contU++;
				} else {
					// Adding a Lower character to password
					pass = pass.concat( getLower(genNum()));
					contL++;
				}
			}
		}

		pass = pass + timeStamp;

		return FRAUDWALL_IDENTIF.concat(pass);

	}

	private String getTimestamp() {
		return String.valueOf(System.currentTimeMillis());
	}

	private int genNum() {
		int iGeRaNu = 0;
		String sTam;
		String sChar;

		iGeRaNu = genNum.nextInt() % arrTam;

		sTam = Integer.toString(iGeRaNu);
		sChar = String.valueOf(sTam.charAt(0));

		// Converting the possible negative number to positive
		if (sChar.equals("-")) {
			iGeRaNu = iGeRaNu * -1;
		}
		return iGeRaNu;
	}

	private String getUpper(int val) {
		res = UPPER[val];
		return res;
	}

	private String getLower(int val) {
		res = LOWER[val];
		return res;
	}

	private String getDigit(int val) {
		res = DIGIT[val];
		return res;
	}

}