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
public class NipValidator{

	public static boolean isNipValid(String nip)
	{

		if (nip.length() != 10)
			return false;

		//Checking whether value is on list of excluded fake numbers
		if(UrzedySkarbowe.fakeNipMap.contains(nip)) {
			return false;
		}
		
		
		int[] weights = { 6, 5, 7, 2, 3, 4, 5, 6, 7 };
		//String[] aNip = nip.split("");
		try {
			int sum = 0;
			for (int i = 0; i < weights.length; i++) {
				sum += Character.getNumericValue(nip.charAt(i)) * weights[i];
			}
			int modulo = sum % 11;
			
			//Correct NIP cannot have control sum of 10
			if (modulo == 10) {
				return false;
			} else {
				return modulo == Character.getNumericValue(nip.charAt(9));
			}
			
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isNipValid(long NIP) {
		return isNipValid(new Long(NIP).toString());
	}

	public static NipValidatorResult NipValidate(String nip) {
		NipValidatorResult r = new NipValidatorResult();
		boolean tisvalid = false;
		String tuscode = null;
		String tusname = null;
		try {
			if (nip != null) { 
				nip = trimInput(nip);
				if (nip.length() == 10) {
					tisvalid  = isNipValid(nip);
					tuscode = getUsCode(nip);
					tusname = getUsName(tuscode);
					if (tuscode == null) tisvalid = false;
				}
			}
		}
		catch (Exception e) {
			tisvalid = false;
		}
		r.isNipValid = tisvalid;
		if (tisvalid) {
			r.stdNIP = nip;
			r.USName = tusname;
		}
		return r;
	}

	private static String getUsName(String uscode) {
		return UrzedySkarbowe.urzedySkarboweMap.get(uscode);
	}
	private static String getUsCode(String nip) {
		return nip.substring(0,3);
	}


	/**
	 * Remove not numbers
	 * 
	 * @param input string, expect NIP
	 * @return cleansed numbers
	 */
	private static String trimInput(String input) {
		input = input.replaceAll(" ", "");
		input = input.replaceFirst("^PL", "");
		input = input.replaceAll("-", "");
		input = input.replaceAll("[\\s-]*", "").replaceAll("([0-9]+)[oO]([0-9]+)", "$10$2").replaceAll("([0-9]+)[oO]([0-9]+)", "$10$2");

		return input;
	}
}
