package pl.sanmargar.sdtt.validators.pesel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PESELValidator{

	public static boolean isPESELValid(long PESEL) {
		return isPESELValid(new Long(PESEL).toString());
	}

	public static boolean isPESELValid(String PESEL) {
		// PESEL = trimInput(PESEL);
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

	public static PESELValidatorResult PESELValidate(String pesel) {
		PESELValidatorResult rI = new PESELValidatorResult();
		rI.isPESELValid = false;
		rI.gender = null;
		rI.date = null;
		try {
			if (pesel != null) { 
				pesel = trimInput(pesel);
				if (pesel.length() == 11) {
					rI.isPESELValid = isPESELValid(pesel);
					rI.gender = getGender(pesel);
					rI.date = getDate(pesel);
				}
			}
		}
		catch (Exception e) {
			
		}
		return rI;
	}

	private static String getGender(String pesel) {
		int g = pesel.charAt(9);
		if (g % 2 == 0)
			return "K";
		else
			return "M";
	}

	private static Date getDate(String pesel) {
		if (pesel == null || pesel.length() <= 0)
			return null;

		// day
		String day = pesel.substring(4, 6);
		// month
		String month = getMonth(pesel);
		// year
		String year = getYear(pesel);

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = dateFormat.parse(day + "-" + month + "-" + year);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	private static String getMonth(String pesel) {
		int monthInt = Integer.parseInt(pesel.substring(2, 4));
		// String month = pesel.substring(2, 4);

		if (monthInt > 0 && monthInt < 13)
			return new Integer(monthInt).toString();
		else if (monthInt > 20 && monthInt < 33)
			return (new Integer(monthInt - 20)).toString();
		else if (monthInt > 80 && monthInt < 93)
			return (new Integer(monthInt - 80)).toString();
		else if (monthInt > 40 && monthInt < 53)
			return (new Integer(monthInt - 40)).toString();
		else if (monthInt > 60 && monthInt < 73)
			return (new Integer(monthInt - 60)).toString();
		return "";
	}

	private static String getYear(String pesel) {

		int monthInt = Integer.parseInt(pesel.substring(2, 4));
		// int yearInt = Integer.parseInt(pesel.substring(0, 2));
		// String month = pesel.substring(2, 4);
		String year = pesel.substring(0, 2);

		if (monthInt > 0 && monthInt < 13)
			return "19" + year;
		else if (monthInt > 20 && monthInt < 33)
			return "20" + year;
		else if (monthInt > 80 && monthInt < 93)
			return "18" + year;
		else if (monthInt > 40 && monthInt < 53)
			return "21" + year;
		else if (monthInt > 60 && monthInt < 73)
			return "22" + year;
		return "";

	}

	/**
	 * Remove not numbbers
	 * 
	 * @param input string, expect PESEL
	 * @return cleaned numbers
	 */
	private static String trimInput(String input) {
		return input.replaceAll("\\D*", "");
	}
}
