package pl.sanmargar.sdtt.validators.pesel;

import java.util.Date;

public class PESELValidatorResult {

	public boolean isPESELValid;
	public String gender;
	public Date date;
	
	public PESELValidatorResult() {
		super();
		this.isPESELValid = false;
		this.gender = null;
		this.date = null;	
	}
}