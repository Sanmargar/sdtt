package pl.sanmargar.sdtt;

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


import java.util.Calendar;
import java.util.Date;

/**
 * @author rafal.jastrzebski
 * @since 01-Jul-2015
 * 
 */

public class PeselValidator {
//	static DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//	Calendar calendar = Calendar.getInstance();


	public static boolean isPESELValid(long PESEL) {
		return isPESELValid(new Long(PESEL).toString());
	}

	public static boolean isPESELValid(String PESEL) {
		int psize = PESEL.length();
		if (psize != 11) {
			return false;
		}
		int[] weights = { 1, 3, 7, 9, 1, 3, 7, 9, 1, 3 };
		int j = 0, sum = 0, control = 0;
		int csum = new Integer(PESEL.substring(psize - 1)).intValue();
		for (int i = 0; i < psize - 1; i++) {
			char c = PESEL.charAt(i);
			j = new Integer(String.valueOf(c)).intValue();
			sum += j * weights[i];
		}
		control = 10 - (sum % 10);
		if (control == 10) {
			control = 0;
		}
		return (control == csum);
	}

	public static PeselValidatorResult getPESELValidation(String pesel) {
		pesel = trimInput(pesel);

		try {
			PeselValidatorResult r = new PeselValidatorResult();
			if (pesel != null) { 
				pesel = trimInput(pesel);
				if (pesel.length() == 11) {
					r.isPeselValid = isPESELValid(pesel);
					if (r.isPeselValid) {
						String g = getGender(pesel);
						Date d = getDate(pesel);
						if (d != null && g != null) {
							r.gender = g;
							r.birthDate = d;
							r.stdPesel = pesel;
						}
						else r.isPeselValid = false;
					}
				}
			}
			return r;
		}
		catch (Exception e) {
			return new PeselValidatorResult();
		}
	}

	public static String getGender(String pesel) {
		int g = pesel.charAt(9);
		if (g % 2 == 0)
			return PeselValidatorResult.GENDER_FEMALE;
		else
			return PeselValidatorResult.GENDER_MALE;
	}

	public static Date getDate(String pesel) {
		if (pesel == null || pesel.length() != 11)
			return null;

		try {
			int day = getDay(pesel);
			int month = getMonth(pesel);
			int year = getYear(pesel);	
			
			Calendar c = Calendar.getInstance();
			Long now = c.getTime().getTime();
			c.set(year, month-1, day, 0, 0); 
			Date d = c.getTime();
			if (c.get(Calendar.DAY_OF_MONTH) != day) return null;
			if (c.get(Calendar.MONTH)+1 != month) return null;
			if (c.get(Calendar.YEAR) != year) return null;
			if (d.getTime() > now) return null;
			return c.getTime();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static int getMonth(String pesel) {
		int monthInt = Integer.parseInt(pesel.substring(2, 4));
//		if (monthInt > 0 && monthInt < 13)
//			monthInt = monthInt;
		if (monthInt > 20 && monthInt < 33)
			 monthInt -= 20;
		else if (monthInt > 80 && monthInt < 93)
			monthInt -= 80;
		else if (monthInt > 40 && monthInt < 53)
			monthInt -= 40;
		else if (monthInt > 60 && monthInt < 73)
			monthInt -= 60;
		if (monthInt >= 1 && monthInt <= 12) return monthInt;
		else return -1;
	}
	
	public static int getDay(String pesel) {
		return Integer.parseInt(pesel.substring(4, 6));
	}
	
	public static int getYear(String pesel) {
		int monthInt = Integer.parseInt(pesel.substring(2, 4));
		String year = pesel.substring(0, 2);
		int y = Integer.parseInt(year);
		if (monthInt > 0 && monthInt < 13)
			return 1900+y;
		else if (monthInt > 20 && monthInt < 33)
			return 2000 + y;
		else if (monthInt > 80 && monthInt < 93)
			return 1800 + y;
		else if (monthInt > 40 && monthInt < 53)
			return 2100 + y;
		else if (monthInt > 60 && monthInt < 73)
			return 2200 + y;
		return -1;
	}

	/**
	 * Remove non numbers
	 * 
	 * @param input
	 * @return cleansed string
	 */
	private static String trimInput(String input) {
		return input.replaceAll("[\\s-]*", "").replaceAll("([0-9]+)[oO]([0-9]+)", "$10$2").replaceAll("([0-9]+)[oO]([0-9]+)", "$10$2");
	}
}
