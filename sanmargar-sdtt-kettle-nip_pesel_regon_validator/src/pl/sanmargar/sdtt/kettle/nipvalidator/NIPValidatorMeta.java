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
package pl.sanmargar.sdtt.kettle.nipvalidator;

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
	id = "SdttNipValidatorStep", 
	image = "pl/sanmargar/sdtt/kettle/nipvalidator/resources/SDTT_NipValidator.svg",
	i18nPackageName = "pl.sanmargar.sdtt.kettle.nipvalidator",
	name = "SdttNipValidatorStep.Name", 
	description = "SdttNipValidatorStep.TooltipDesc",
	categoryDescription = "i18n:pl.sanmargar.sdtt.kettle.nipvalidator:Steps.Category.Name"	
)

/**
 * @author rafal_jot
 */

public class NIPValidatorMeta extends BaseStepMeta implements StepMetaInterface {

	private static Class<?> PKG = NIPValidatorMeta.class; // for i18n purposes

	private String sourceNipFieldName;
	private String resultStdNipFieldName;
	private String resultFieldName;
	private String resultTaxOfficeFieldName;
	private boolean standardizeCheck;
	private boolean taxOfficeNameCheck;

	private final String NAME_SOURCE_NIP_FIELDNAME = "sourceNipFieldName";
	private final String NAME_RESULT_STD_NIP_FIELDNAME = "resultStdNipFieldName";
	private final String NAME_RESULT_TAX_OFFICE_FIELDNAME = "resultTaxOfficeFieldName";
	private final String NAME_RESULT_FIELDNAME = "resultFieldName";
	private final String NAME_STANDARDIZE_CHECK = "standardizeCheck";
	private final String NAME_TAX_OFFICE_NAME_CHECK = "taxOfficeNameCheck";

	public NIPValidatorMeta() {
		super();
	}

	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
		return new NIPValidatorDialog(shell, meta, transMeta, name);
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans disp) {
		return new NIPValidatorStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	public StepDataInterface getStepData() {
		return new NIPValidatorData();
	}

	public void setDefault() {
		taxOfficeNameCheck = true;
		standardizeCheck = true;
		sourceNipFieldName = "SRC_NIP";
		resultStdNipFieldName = "STD_NIP";
		resultFieldName = "VAL_NIP_RESULT";
		resultTaxOfficeFieldName = "STD_NIP_TAX_OFFICE_NAME";
	}

	public String getSourceNipFieldName() {
		return this.sourceNipFieldName;
	}

	public String getResultStdNipFieldName() {
		return this.resultStdNipFieldName;
	}

	public void setSourceNipFieldName(String fieldname) {
		this.sourceNipFieldName = fieldname;
	}

	public String getResultFieldName() {
		return resultFieldName;
	}

	public void setResultFieldName(String resultfieldname) {
		this.resultFieldName = resultfieldname;
	}

	public String getResultTaxOfficeFieldName() {
		return resultTaxOfficeFieldName;
	}

	public void setResultTaxOfficeNameFieldName(String resultTaxOfficeFieldname) {
		this.resultTaxOfficeFieldName = resultTaxOfficeFieldname;
	}

	public void setResultStdNipFieldName(String resultStdNipFieldName) {
		this.resultStdNipFieldName = resultStdNipFieldName;
	}

	public boolean isStandardizeCheck() {
		return standardizeCheck;
	}

	public void setStandardizeCheck(boolean standardizeCheck) {
		this.standardizeCheck = standardizeCheck;
	}

	public boolean isTaxOfficeNameCheck() {
		return taxOfficeNameCheck;
	}

	public void setTaxOfficeNameCheck(boolean taxOfficeNameCheck) {
		this.taxOfficeNameCheck = taxOfficeNameCheck;
	}

	public Object clone() {
		NIPValidatorMeta retval = (NIPValidatorMeta) super.clone();
		return retval;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer();
		retval.append("    " + XMLHandler.addTagValue(NAME_SOURCE_NIP_FIELDNAME, sourceNipFieldName));
		retval.append("    " + XMLHandler.addTagValue(NAME_RESULT_FIELDNAME, resultFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_RESULT_TAX_OFFICE_FIELDNAME, resultTaxOfficeFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_RESULT_STD_NIP_FIELDNAME, resultStdNipFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_STANDARDIZE_CHECK, standardizeCheck));
		retval.append("	   " + XMLHandler.addTagValue(NAME_TAX_OFFICE_NAME_CHECK, taxOfficeNameCheck));
		return retval.toString();
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {

		try {
			setSourceNipFieldName(XMLHandler.getTagValue(stepnode, NAME_SOURCE_NIP_FIELDNAME));
			setResultFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_FIELDNAME));
			setResultStdNipFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_STD_NIP_FIELDNAME));
			setResultTaxOfficeNameFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_TAX_OFFICE_FIELDNAME));
			setStandardizeCheck("Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, NAME_STANDARDIZE_CHECK)));
			setTaxOfficeNameCheck("Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, NAME_TAX_OFFICE_NAME_CHECK)));
		} catch (Exception e) {
			throw new KettleXMLException(BaseMessages.getString(PKG, "NIPValidatorMeta.Exception.UnableToReadStepInfo"),
					e);
		}
	}

	public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step)
			throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, NAME_SOURCE_NIP_FIELDNAME, sourceNipFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_STD_NIP_FIELDNAME, resultStdNipFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_FIELDNAME, resultFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_TAX_OFFICE_FIELDNAME,
					resultTaxOfficeFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_STANDARDIZE_CHECK, standardizeCheck);
			rep.saveStepAttribute(id_transformation, id_step, NAME_TAX_OFFICE_NAME_CHECK, taxOfficeNameCheck);

		} catch (Exception e) {
			throw new KettleException(
					BaseMessages.getString(PKG, "PESELValidatorMeta.Exception.UnableToSaveStepInfo") + id_step, e);
		}
	}

	public void readRep(Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases)
			throws KettleException {

		try {
			sourceNipFieldName = rep.getStepAttributeString(id_step, NAME_SOURCE_NIP_FIELDNAME);
			resultStdNipFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_STD_NIP_FIELDNAME);
			resultFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_FIELDNAME);
			resultTaxOfficeFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_TAX_OFFICE_FIELDNAME);
			standardizeCheck = rep.getStepAttributeBoolean(id_step, NAME_STANDARDIZE_CHECK);
			taxOfficeNameCheck = rep.getStepAttributeBoolean(id_step, NAME_TAX_OFFICE_NAME_CHECK);
		} catch (Exception e) {
			throw new KettleException(
					BaseMessages.getString(PKG, "NIPValidatorMeta.Exception.UnexpectedErrorReadingStepInfo"), e);
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
		if (isTaxOfficeNameCheck())
			getField(inputRowMeta, name, space, getResultTaxOfficeFieldName(), ValueMeta.TYPE_STRING);
		if (isStandardizeCheck())
			getField(inputRowMeta, name, space, getResultStdNipFieldName(), ValueMeta.TYPE_STRING);
	}

	private void checkNotEmpty(List<CheckResultInterface> remarks, StepMeta stepMeta, String fieldName) {
		CheckResult cr;
		String error_message = "";
		if (Const.isEmpty(fieldName)) {
			error_message = BaseMessages.getString(PKG, "SdttNIPValidatorMeta.Check.FieldMissing", fieldName);
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
			remarks.add(cr);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev,
			String[] input, String[] output, RowMetaInterface info, VariableSpace space, Repository repository,
			IMetaStore metaStore) {
		CheckResult cr;

		checkNotEmpty(remarks, stepMeta, resultFieldName);
		checkNotEmpty(remarks, stepMeta, sourceNipFieldName);
		if (isTaxOfficeNameCheck())
			checkNotEmpty(remarks, stepMeta, getResultTaxOfficeFieldName());
		if (isStandardizeCheck())
			checkNotEmpty(remarks, stepMeta, getResultStdNipFieldName());

		if (input.length > 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK,
					BaseMessages.getString(PKG, "NIPValidatorMeta.Check.ReceivingInfoFromOtherSteps"), stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR,
					BaseMessages.getString(PKG, "NIPValidatorMeta.Check.NoInpuReceived"), stepMeta);
			remarks.add(cr);
		}

	}

}
