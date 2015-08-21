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
package pl.sanmargar.sdtt.kettle.regonvalidator;

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
	id = "SdttRegonValidatorStep", 
	image = "pl/sanmargar/sdtt/kettle/regonvalidator/resources/SDTT_RegonValidator.svg",
	i18nPackageName = "pl.sanmargar.sdtt.kettle.regonvalidator",
	name = "SdttRegonValidatorStep.Name", 
	description = "SdttRegonValidatorStep.TooltipDesc",
	categoryDescription = "i18n:pl.sanmargar.sdtt.kettle:Steps.Category.Name"	
)

/**
 * @author rafal_jot
 */


public class RegonValidatorMeta extends BaseStepMeta implements StepMetaInterface {

	private static Class<?> PKG = RegonValidatorMeta.class; 
	
	private String sourceRegonFieldName;
	private String resultStdRegonFieldName;
	private String resultFieldName;
	private String resultRegionFieldName;
	private boolean standardizeCheck;
	private boolean regionCheck;

	private final String NAME_SOURCE_REGON_FIELDNAME = "sourceRegonFieldName";
	private final String NAME_RESULT_STD_REGON_FIELDNAME = "resultStdRegonFieldName";
	private final String NAME_RESULT_REGION_FIELDNAME = "resultRegionFieldName";
	private final String NAME_RESULT_FIELDNAME = "resultFieldName";
	private final String NAME_STANDARDIZE_CHECK = "standardizeCheck";
	private final String NAME_REGION_CHECK = "regionCheck";

	public RegonValidatorMeta() {
		super();
	}

	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
		return new RegonValidatorDialog(shell, meta, transMeta, name);
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans disp) {
		return new RegonValidatorStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	public StepDataInterface getStepData() {
		return new RegonValidatorData();
	}

	public void setDefault() {
		regionCheck = true;
		standardizeCheck = true;
		sourceRegonFieldName = "SRC_REGON";
		resultStdRegonFieldName = "STD_REGON";
		resultFieldName = "VAL_REGON_RESULT";
		resultRegionFieldName = "STD_REGON_REGION";
	}

	public String getSourceRegonFieldName() {
		return this.sourceRegonFieldName;
	}

	public String getResultStdRegonFieldName() {
		return this.resultStdRegonFieldName;
	}

	public void setSourceRegonFieldName(String fieldname) {
		this.sourceRegonFieldName = fieldname;
	}

	public String getResultFieldName() {
		return resultFieldName;
	}

	public void setResultFieldName(String resultfieldname) {
		this.resultFieldName = resultfieldname;
	}

	public String getResultRegionFieldName() {
		return resultRegionFieldName;
	}

	public void setResultRegionFieldName(String resultRegionFieldname) {
		this.resultRegionFieldName = resultRegionFieldname;
	}


	public void setResultStdRegonFieldName(String resultStdRegonFieldName) {
		this.resultStdRegonFieldName = resultStdRegonFieldName;
	}



	public boolean isStandardizeCheck() {
		return standardizeCheck;
	}

	public void setStandardizeCheck(boolean standardizeCheck) {
		this.standardizeCheck = standardizeCheck;
	}

	public boolean isRegionCheck() {
		return regionCheck;
	}

	public void setRegionCheck(boolean regionCheck) {
		this.regionCheck = regionCheck;
	}

	public Object clone() {
		RegonValidatorMeta retval = (RegonValidatorMeta) super.clone();
		return retval;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer();
		retval.append("    " + XMLHandler.addTagValue(NAME_SOURCE_REGON_FIELDNAME, sourceRegonFieldName));
		retval.append("    " + XMLHandler.addTagValue(NAME_RESULT_FIELDNAME, resultFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_RESULT_REGION_FIELDNAME, resultRegionFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_RESULT_STD_REGON_FIELDNAME, resultStdRegonFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_STANDARDIZE_CHECK, standardizeCheck));
		retval.append("	   " + XMLHandler.addTagValue(NAME_REGION_CHECK, regionCheck));
		return retval.toString();
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {

		try {
			setSourceRegonFieldName(XMLHandler.getTagValue(stepnode, NAME_SOURCE_REGON_FIELDNAME));
			setResultFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_FIELDNAME));
			setResultStdRegonFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_STD_REGON_FIELDNAME));
			setResultRegionFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_REGION_FIELDNAME));
			setStandardizeCheck("Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, NAME_STANDARDIZE_CHECK)));
			setRegionCheck("Y".equalsIgnoreCase(XMLHandler.getTagValue(stepnode, NAME_REGION_CHECK)));
		} catch (Exception e) {
			throw new KettleXMLException(
					BaseMessages.getString(PKG, "REGONValidatorMeta.Exception.UnableToReadStepInfo"), e);
		}
	}

	public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step)
			throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, NAME_SOURCE_REGON_FIELDNAME, sourceRegonFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_STD_REGON_FIELDNAME, resultStdRegonFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_FIELDNAME, resultFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_REGION_FIELDNAME, resultRegionFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_STANDARDIZE_CHECK, standardizeCheck);
			rep.saveStepAttribute(id_transformation, id_step, NAME_REGION_CHECK, regionCheck);

		} catch (Exception e) {
			throw new KettleException(
					BaseMessages.getString(PKG, "PESELValidatorMeta.Exception.UnableToSaveStepInfo") + id_step, e);
		}
	}

	public void readRep(Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases)
			throws KettleException {

		try {
			sourceRegonFieldName = rep.getStepAttributeString(id_step, NAME_SOURCE_REGON_FIELDNAME);
			resultStdRegonFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_STD_REGON_FIELDNAME);
			resultFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_FIELDNAME);
			resultRegionFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_REGION_FIELDNAME);
			standardizeCheck = rep.getStepAttributeBoolean(id_step, NAME_STANDARDIZE_CHECK);
			regionCheck = rep.getStepAttributeBoolean(id_step, NAME_REGION_CHECK);
		} catch (Exception e) {
			throw new KettleException(
					BaseMessages.getString(PKG, "REGONValidatorMeta.Exception.UnexpectedErrorReadingStepInfo"), e);
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
		if (isRegionCheck())
			getField(inputRowMeta, name, space, getResultRegionFieldName(), ValueMeta.TYPE_STRING);
		if (isStandardizeCheck())
			getField(inputRowMeta, name, space, getResultStdRegonFieldName(), ValueMeta.TYPE_STRING);
	}

	private void checkNotEmpty(List<CheckResultInterface> remarks, StepMeta stepMeta, String fieldName) {
		CheckResult cr;
		String error_message = "";
		if (Const.isEmpty(fieldName)) {
			error_message = BaseMessages.getString(PKG, "SdttREGONValidatorMeta.Check.FieldMissing", fieldName);
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
			remarks.add(cr);
		}
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev,
			String input[], String output[], RowMetaInterface info) {
		CheckResult cr;

		checkNotEmpty(remarks, stepMeta, resultFieldName);
		checkNotEmpty(remarks, stepMeta, sourceRegonFieldName);
		if (isRegionCheck())
			checkNotEmpty(remarks, stepMeta, getResultRegionFieldName());
		if (isStandardizeCheck())
			checkNotEmpty(remarks, stepMeta, getResultStdRegonFieldName());

		if (input.length > 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK,
					BaseMessages.getString(PKG, "REGONValidatorMeta.Check.ReceivingInfoFromOtherSteps"), stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR,
					BaseMessages.getString(PKG, "REGONValidatorMeta.Check.NoInpuReceived"), stepMeta);
			remarks.add(cr);
		}

	}

}
