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

import java.util.List;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

@Step(
	id = "SdttPeselValidatorStep", 
	image = "pl/sanmargar/sdtt/kettle/peselvalidator/resources/SDTT_PeselValidator.svg",
	i18nPackageName = "pl.sanmargar.sdtt.kettle.peselvalidator",
	name = "SdttPeselValidatorStep.Name", 
	description = "SdttPeselValidatorStep.TooltipDesc",
	categoryDescription = "i18n:pl.sanmargar.sdtt.kettle:Steps.Category.Name"	
//	categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Transform"
)

/**
 * @author rafal_jot
 */


public class PESELValidatorMeta extends BaseStepMeta implements StepMetaInterface {

	private static Class<?> PKG = PESELValidatorMeta.class; // for i18n purposes
	
	private String sourcePeselFieldName;
	private String resultStdPeselFieldName;
	private String resultFieldName;
	private String resultGenderFieldName;
	private String resultBirthDateFieldName;
	private boolean standardizeCheck;
	private boolean genderCheck;
	private boolean birthDateCheck;

	private final String NAME_SOURCE_PESEL_FIELDNAME = "sourcePeselFieldName";
	private final String NAME_RESULT_STD_PESEL_FIELDNAME = "sourceStdPeselFieldName";
	private final String NAME_RESULT_BIRTH_DATE_FIELDNAME = "resultBirthDateFieldName";
	private final String NAME_RESULT_GENDER_FIELDNAME = "resultGenderFieldName";
	private final String NAME_RESULT_FIELDNAME = "resultFieldName";
	private final String NAME_STANDARDIZE_CHECK = "standardizeCheck";
	private final String NAME_GENDER_CHECK = "genderCheck";
	private final String NAME_BIRTH_DATE_CHECK = "birthDateCheck";

	public PESELValidatorMeta() {
		super();
	}

	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
		return new PESELValidatorDialog(shell, meta, transMeta, name);
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans disp) {
		return new PESELValidatorStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	public StepDataInterface getStepData() {
		return new PESELValidatorData();
	}

	public void setDefault() {
		genderCheck = true;
		birthDateCheck = true;
		standardizeCheck = true;
		sourcePeselFieldName = "SRC_PESEL";
		resultStdPeselFieldName = "STD_PESEL";
		resultFieldName = "VAL_PESEL_RESULT";
		resultGenderFieldName = "STD_PESEL_GENDER";
		resultBirthDateFieldName = "STD_PESEL_BIRTHDATE";
	}

	public String getSourcePeselFieldName() {
		return this.sourcePeselFieldName;
	}

	public String getResultStdPeselFieldName() {
		return this.resultStdPeselFieldName;
	}

	public void setSourcePeselFieldName(String fieldname) {
		this.sourcePeselFieldName = fieldname;
	}

	public String getResultFieldName() {
		return resultFieldName;
	}

	public void setResultFieldName(String resultfieldname) {
		this.resultFieldName = resultfieldname;
	}

	public String getResultGenderFieldName() {
		return resultGenderFieldName;
	}

	public void setResultGenderFieldName(String resultGenderFieldname) {
		this.resultGenderFieldName = resultGenderFieldname;
	}

	public String getResultBirthDateFieldName() {
		return resultBirthDateFieldName;
	}

	public void setResultStdPeselFieldName(String resultStdPeselFieldName) {
		this.resultStdPeselFieldName = resultStdPeselFieldName;
	}

	public void setResultBirthDateFieldName(String resultBirthDateFieldName) {
		this.resultBirthDateFieldName = resultBirthDateFieldName;
	}

	public boolean isStandardizeCheck() {
		return standardizeCheck;
	}

	public void setStandardizeCheck(boolean standardizeCheck) {
		this.standardizeCheck = standardizeCheck;
	}

	public boolean isGenderCheck() {
		return genderCheck;
	}

	public void setGenderCheck(boolean genderCheck) {
		this.genderCheck = genderCheck;
	}

	public boolean isBirthDateCheck() {
		return birthDateCheck;
	}

	public void setBirthDateCheck(boolean birthDateCheck) {
		this.birthDateCheck = birthDateCheck;
	}

	public Object clone() {
		PESELValidatorMeta retval = (PESELValidatorMeta) super.clone();
		return retval;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer();
		retval.append("    " + XMLHandler.addTagValue(NAME_SOURCE_PESEL_FIELDNAME, sourcePeselFieldName));
		retval.append("    " + XMLHandler.addTagValue(NAME_RESULT_FIELDNAME, resultFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_RESULT_GENDER_FIELDNAME, resultGenderFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_RESULT_BIRTH_DATE_FIELDNAME, resultBirthDateFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_RESULT_STD_PESEL_FIELDNAME, resultStdPeselFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_STANDARDIZE_CHECK, standardizeCheck));
		retval.append("	   " + XMLHandler.addTagValue(NAME_GENDER_CHECK, genderCheck));
		retval.append("	   " + XMLHandler.addTagValue(NAME_BIRTH_DATE_CHECK, birthDateCheck));
		return retval.toString();
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {

		try {
			setSourcePeselFieldName(XMLHandler.getTagValue(stepnode, NAME_SOURCE_PESEL_FIELDNAME));
			setResultFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_FIELDNAME));
			setResultStdPeselFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_STD_PESEL_FIELDNAME));
			setResultGenderFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_GENDER_FIELDNAME));
			setResultBirthDateFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_BIRTH_DATE_FIELDNAME));
			setStandardizeCheck("Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, NAME_STANDARDIZE_CHECK)));
			setBirthDateCheck("Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, NAME_BIRTH_DATE_CHECK)));
			setGenderCheck("Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, NAME_GENDER_CHECK)));
		} catch (Exception e) {
			throw new KettleXMLException(
					BaseMessages.getString(PKG, "PESELValidatorMeta.Exception.UnableToReadStepInfo"), e);
		}
	}

	public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step)
			throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, NAME_SOURCE_PESEL_FIELDNAME, sourcePeselFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_STD_PESEL_FIELDNAME, resultStdPeselFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_FIELDNAME, resultFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_GENDER_FIELDNAME, resultGenderFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_BIRTH_DATE_FIELDNAME,
					resultBirthDateFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_STANDARDIZE_CHECK, standardizeCheck);
			rep.saveStepAttribute(id_transformation, id_step, NAME_GENDER_CHECK, genderCheck);
			rep.saveStepAttribute(id_transformation, id_step, NAME_BIRTH_DATE_CHECK, birthDateCheck);

		} catch (Exception e) {
			throw new KettleException(
					BaseMessages.getString(PKG, "PESELValidatorMeta.Exception.UnableToSaveStepInfo") + id_step, e);
		}
	}

	public void readRep(Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases)
			throws KettleException {

		try {
			sourcePeselFieldName = rep.getStepAttributeString(id_step, NAME_SOURCE_PESEL_FIELDNAME);
			resultStdPeselFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_STD_PESEL_FIELDNAME);
			resultFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_FIELDNAME);
			resultGenderFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_GENDER_FIELDNAME);
			resultBirthDateFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_BIRTH_DATE_FIELDNAME);
			standardizeCheck = rep.getStepAttributeBoolean(id_step, NAME_STANDARDIZE_CHECK);
			genderCheck = rep.getStepAttributeBoolean(id_step, NAME_GENDER_CHECK);
			birthDateCheck = rep.getStepAttributeBoolean(id_step, NAME_BIRTH_DATE_CHECK);

		} catch (Exception e) {
			throw new KettleException(
					BaseMessages.getString(PKG, "PESELValidatorMeta.Exception.UnexpectedErrorReadingStepInfo"), e);
		}

	}

	private void getField(RowMetaInterface inputRowMeta, String name, VariableSpace space, String fieldName, int type) {
		String s = space.environmentSubstitute(fieldName);
		if (!Const.isEmpty(s)) {
			ValueMetaInterface v = new ValueMeta(s, type);
			if (type == ValueMeta.TYPE_STRING) {
				v.setTrimType(ValueMeta.TRIM_TYPE_BOTH);
			}
			v.setOrigin(name);
			inputRowMeta.addValueMeta(v);
		}
	}

	public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
			VariableSpace space, Repository repository, IMetaStore metaStore) throws KettleStepException {
		getField(inputRowMeta, name, space, getResultFieldName(), ValueMeta.TYPE_BOOLEAN);
		if (isGenderCheck())
			getField(inputRowMeta, name, space, getResultGenderFieldName(), ValueMeta.TYPE_STRING);
		if (isBirthDateCheck())
			getField(inputRowMeta, name, space, getResultBirthDateFieldName(), ValueMeta.TYPE_DATE);
		if (isStandardizeCheck())
			getField(inputRowMeta, name, space, getResultStdPeselFieldName(), ValueMeta.TYPE_STRING);
	}

	private void checkNotEmpty(List<CheckResultInterface> remarks, StepMeta stepMeta, String fieldName) {
		CheckResult cr;
		String error_message = "";
		if (Const.isEmpty(fieldName)) {
			error_message = BaseMessages.getString(PKG, "SdttPeselValidatorMeta.Check.FieldMissing", fieldName);
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
			remarks.add(cr);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, 
			String[] input, String[] output, RowMetaInterface info, VariableSpace space, Repository repository, IMetaStore metaStore ) {
		CheckResult cr;

		checkNotEmpty(remarks, stepMeta, resultFieldName);
		checkNotEmpty(remarks, stepMeta, sourcePeselFieldName);
		if (isGenderCheck())
			checkNotEmpty(remarks, stepMeta, getResultGenderFieldName());
		if (isBirthDateCheck())
			checkNotEmpty(remarks, stepMeta, getResultBirthDateFieldName());
		if (isStandardizeCheck())
			checkNotEmpty(remarks, stepMeta, getResultStdPeselFieldName());

		if (input.length > 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK,
					BaseMessages.getString(PKG, "PESELValidatorMeta.Check.ReceivingInfoFromOtherSteps"), stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR,
					BaseMessages.getString(PKG, "PESELValidatorMeta.Check.NoInpuReceived"), stepMeta);
			remarks.add(cr);
		}

	}

}
