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

package pl.sanmargar.sdtt.kettle.peselvalidator;


import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;
//import org.pentaho.di.core.row.ValueMetaInterface;
/**
 * @author rafal_jot
 * @since 01-jun-2015
 */

public class PESELValidatorData extends BaseStepData implements StepDataInterface {
	public int indexOfField = -1;
	
/*	public String realStdPesel;
	public String realResult;
	public Date realBirthDate;
	public String realGender;
	public String dateFieldname;*/

	  public RowMetaInterface outputRowMeta;
	  public RowMetaInterface inputRowMeta;

	  public int indexOfSourcePeselField = -1;
	  
	  public int indexOfResultField  = -1;
	  public int indexOfResultGenderField  = -1;
	  public int indexOfResultBirthDateField = -1;
	  public int indexOfResultStdPeselField  = -1;
	  
	  public int numResultFields;
	  
//	  public ValueMetaInterface inputMeta;
//	  public ValueMetaInterface outputMeta;
	  

	public PESELValidatorData() {
		super();

	}

}
	
