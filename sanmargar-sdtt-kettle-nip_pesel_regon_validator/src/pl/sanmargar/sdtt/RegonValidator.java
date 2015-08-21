/*
 * Sanmargar Data Transformation Tools
 *
 * (C) Copyright 2015 Sanmargar Team sp z o. o. (http://sanmargar.pl/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package pl.sanmargar.sdtt;

/**
 * @author rafal.jastrzebski
 * @since 01-Jul-2015
 * 
 */
public class RegonValidator {

	private static boolean checkSum9(String regon) {
		int sum = 8 * parseInt(regon.charAt(0)) + 9 * parseInt(regon.charAt(1)) + 2 * parseInt(regon.charAt(2))
				+ 3 * parseInt(regon.charAt(3)) + 4 * parseInt(regon.charAt(4)) + 5 * parseInt(regon.charAt(5))
				+ 6 * parseInt(regon.charAt(6)) + 7 * parseInt(regon.charAt(7));
		sum %= 11;
		if (sum == 10) {
			sum = 0;
		}
		return sum == parseInt(regon.charAt(8));
	}

	private static int parseInt(char c) {
		return Integer.parseInt("" + c);
	}

	private static boolean checkSum14(String regon) {
		int sum = 2 * parseInt(regon.charAt(0)) + 4 * parseInt(regon.charAt(1)) + 8 * parseInt(regon.charAt(2))
				+ 5 * parseInt(regon.charAt(3)) + 0 * parseInt(regon.charAt(4)) + 9 * parseInt(regon.charAt(5))
				+ 7 * parseInt(regon.charAt(6)) + 3 * parseInt(regon.charAt(7)) + 6 * parseInt(regon.charAt(8))
				+ 1 * parseInt(regon.charAt(9)) + 2 * parseInt(regon.charAt(10)) + 4 * parseInt(regon.charAt(11))
				+ 8 * parseInt(regon.charAt(12));
		sum %= 11;
		if (sum == 10) {
			sum = 0;
		}
		return sum == parseInt(regon.charAt(13));
	}

	private static String checkSum(String regon) {
//		regon = trimInput(regon);
		switch (regon.length()) {
		case 9:
			return checkSum9(regon) ? regon : null;
		case 14:
			return checkSum9(regon) && checkSum14(regon) ? regon : null;
		case 7:
			return checkSum9("00".concat(regon)) ? regon : null;
		default:
			return null;
		}

	}

	public static boolean isRegonValid(String regon)
	{
		return checkSum(regon) != null;
	}
	

	public static boolean isRegonValid(long regon) {
		return isRegonValid(new Long(regon).toString());
	}

	public static RegonValidatorResult RegonValidate(String regon) {
		RegonValidatorResult r = new RegonValidatorResult();
		boolean tisvalid = false;
		String tregion = null;
		String tregon = null;
		String tregioncode = null;
		try {
			if (regon != null) {
				tregon = trimInput(regon);
				if (regon.length() >= 7) {
					tregon =  checkSum(tregon);
					tisvalid = tregon != null;
					tregioncode = getRegionCode(tregon);
					tregion = getRegionName(tregioncode);
					if (tregion == null)
						tisvalid = false;
				}
			}
		} catch (Exception e) {
			tisvalid = false;
		}
		r.isRegonValid = tisvalid;
		if (tisvalid) {
			r.stdRegon = tregon;
			r.Region = tregion;
		}
		return r;
	}

	private static String getRegionName(String regionCode) {
		return Wojewodztwa.wojewodztwaMap.get(regionCode);
	}

	private static String getRegionCode(String regon) {
		return regon.substring(0, 2);
	}

	/**
	 * Remove not numbers
	 * 
	 * @param input
	 *            string, expect REGON
	 * @return cleansed numbers
	 */
	private static String trimInput(String input) {
		input = input.replaceAll(" ", "");
		input = input.replaceAll("[\\s-]*", "").replaceAll("([0-9]+)[oO]([0-9]+)", "$10$2")
				.replaceAll("([0-9]+)[oO]([0-9]+)", "$10$2");

		return input;
	}
}
