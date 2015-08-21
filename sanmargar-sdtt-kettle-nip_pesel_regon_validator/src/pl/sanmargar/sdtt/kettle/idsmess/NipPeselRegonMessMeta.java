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
package pl.sanmargar.sdtt.kettle.idsmess;

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
	id = "SdttPeselNipRegonMessStep", 
	image = "pl/sanmargar/sdtt/kettle/idsmess/resources/SDTT_PeselNipRegonMess.svg",
	i18nPackageName = "pl.sanmargar.sdtt.kettle.idsmess",
	name = "SdttPeselNipRegonMessStep.Name", 
	description = "SdttPeselNipRegonMessStep.TooltipDesc",
	categoryDescription = "i18n:pl.sanmargar.sdtt.kettle:Steps.Category.Name"	
//	categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Transform"
)

/**
 * @author rafal_jot
 */


public class NipPeselRegonMessMeta extends BaseStepMeta implements StepMetaInterface {

	private static Class<?> PKG = NipPeselRegonMessMeta.class; // for i18n purposes
	
	private String sourcePeselFieldName;
	private String sourceNipFieldName;
	private String sourceRegonFieldName;
	
	private String resultPeselFieldName;
	private String resultNipFieldName;
	private String resultRegonFieldName;
	
	private String resultFieldName;

	private final String NAME_SOURCE_PESEL_FIELDNAME = "sourcePeselFieldName";
	private final String NAME_SOURCE_NIP_FIELDNAME = "sourceNipFieldName";
	private final String NAME_SOURCE_REGON_FIELDNAME = "sourceRegonFieldName";

	private final String NAME_RESULT_PESEL_FIELDNAME = "resultPeselFieldName";
	private final String NAME_RESULT_NIP_FIELDNAME = "resultNipFieldName";
	private final String NAME_RESULT_REGON_FIELDNAME = "resultRegonFieldName";

	private final String NAME_RESULT_FIELDNAME = "resultFieldName";
	
	public NipPeselRegonMessMeta() {
		super();
	}

	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
		return new NipPeselRegonMessDialog(shell, meta, transMeta, name);
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
			Trans disp) {
		return new NipPeselRegonMessStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	public StepDataInterface getStepData() {
		return new NipPeselRegonMessData();
	}

	public void setDefault() {
		sourcePeselFieldName = "SRC_PESEL";
		sourceNipFieldName = "SRC_NIP";
		sourceRegonFieldName = "SRC_REGON";
		resultPeselFieldName = "STD_PESEL";
		resultNipFieldName = "STD_NIP";
		resultRegonFieldName = "STD_REGON";
		resultFieldName = "VAL_PESEL_NIP_REGON";

	}

	public String getSourcePeselFieldName() {
		return this.sourcePeselFieldName;
	}

	public String getSourceNipFieldName() {
		return this.sourceNipFieldName;
	}
	public String getSourceRegonFieldName() {
		return this.sourceRegonFieldName;
	}
	
	public String getResultPeselFieldName() {
		return this.resultPeselFieldName;
	}

	public String getResultNipFieldName() {
		return this.resultNipFieldName;
	}

	public String getResultRegonFieldName() {
		return this.resultRegonFieldName;
	}

	public String getResultFieldName() {
		return resultFieldName;
	}

	public void setSourcePeselFieldName(String fieldname) {
		this.sourcePeselFieldName = fieldname;
	}
	public void setSourceNipFieldName(String fieldname) {
		this.sourceNipFieldName = fieldname;
	}
	public void setSourceRegonFieldName(String fieldname) {
		this.sourceRegonFieldName = fieldname;
	}

	public void setResultPeselFieldName(String fieldname) {
		this.resultPeselFieldName = fieldname;
	}
	public void setResultNipFieldName(String fieldname) {
		this.resultNipFieldName = fieldname;
	}
	public void setResultRegonFieldName(String fieldname) {
		this.resultRegonFieldName = fieldname;
	}
	
	public void setResultFieldName(String resultfieldname) {
		this.resultFieldName = resultfieldname;
	}


	public Object clone() {
		NipPeselRegonMessMeta retval = (NipPeselRegonMessMeta) super.clone();
		return retval;
	}

	public String getXML() {
		StringBuffer retval = new StringBuffer();
		retval.append("    " + XMLHandler.addTagValue(NAME_SOURCE_PESEL_FIELDNAME, sourcePeselFieldName));
		retval.append("    " + XMLHandler.addTagValue(NAME_SOURCE_NIP_FIELDNAME, sourceNipFieldName));
		retval.append("    " + XMLHandler.addTagValue(NAME_SOURCE_REGON_FIELDNAME, sourceRegonFieldName));
		retval.append("    " + XMLHandler.addTagValue(NAME_RESULT_FIELDNAME, resultFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_RESULT_PESEL_FIELDNAME, resultPeselFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_RESULT_NIP_FIELDNAME, resultNipFieldName));
		retval.append("	   " + XMLHandler.addTagValue(NAME_RESULT_REGON_FIELDNAME, resultRegonFieldName));
		return retval.toString();
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {
		try {
			setSourcePeselFieldName(XMLHandler.getTagValue(stepnode, NAME_SOURCE_PESEL_FIELDNAME));
			setSourceNipFieldName(XMLHandler.getTagValue(stepnode, NAME_SOURCE_NIP_FIELDNAME));
			setSourceRegonFieldName(XMLHandler.getTagValue(stepnode, NAME_SOURCE_REGON_FIELDNAME));
			setResultFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_FIELDNAME));
			setResultPeselFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_PESEL_FIELDNAME));
			setResultNipFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_NIP_FIELDNAME));
			setResultRegonFieldName(XMLHandler.getTagValue(stepnode, NAME_RESULT_REGON_FIELDNAME));
		} catch (Exception e) {
			throw new KettleXMLException(
					BaseMessages.getString(PKG, "PeselNipMessMeta.Exception.UnableToReadStepInfo"), e);
		}
	}

	public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step)
			throws KettleException {
		try {
			rep.saveStepAttribute(id_transformation, id_step, NAME_SOURCE_PESEL_FIELDNAME, sourcePeselFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_SOURCE_NIP_FIELDNAME, sourceNipFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_SOURCE_REGON_FIELDNAME, sourceRegonFieldName);
			
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_FIELDNAME, resultFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_PESEL_FIELDNAME, resultPeselFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_NIP_FIELDNAME, resultNipFieldName);
			rep.saveStepAttribute(id_transformation, id_step, NAME_RESULT_REGON_FIELDNAME, resultRegonFieldName);
		} catch (Exception e) {
			throw new KettleException(
					BaseMessages.getString(PKG, "PeselNipMessMeta.Exception.UnableToSaveStepInfo") + id_step, e);
		}
	}

	public void readRep(Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases)
			throws KettleException {

		try {
			sourcePeselFieldName = rep.getStepAttributeString(id_step, NAME_SOURCE_PESEL_FIELDNAME);
			sourceNipFieldName = rep.getStepAttributeString(id_step, NAME_SOURCE_NIP_FIELDNAME);
			sourceRegonFieldName = rep.getStepAttributeString(id_step, NAME_SOURCE_REGON_FIELDNAME);
			
			resultPeselFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_PESEL_FIELDNAME);
			resultNipFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_NIP_FIELDNAME);
			resultRegonFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_REGON_FIELDNAME);
			resultFieldName = rep.getStepAttributeString(id_step, NAME_RESULT_FIELDNAME);
		} catch (Exception e) {
			throw new KettleException(
					BaseMessages.getString(PKG, "PeselNipMessMeta.Exception.UnexpectedErrorReadingStepInfo"), e);
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
		getField(inputRowMeta, name, space, getResultFieldName(), ValueMeta.TYPE_STRING);
		getField(inputRowMeta, name, space, getResultPeselFieldName(), ValueMeta.TYPE_STRING);
		getField(inputRowMeta, name, space, getResultNipFieldName(), ValueMeta.TYPE_STRING);
		getField(inputRowMeta, name, space, getResultRegonFieldName(), ValueMeta.TYPE_STRING);
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, 
			String[] input, String[] output, RowMetaInterface info, VariableSpace space, Repository repository, IMetaStore metaStore ) {
		CheckResult cr;
		if (Const.isEmpty(sourcePeselFieldName) && Const.isEmpty(sourceNipFieldName) && Const.isEmpty(sourceRegonFieldName) ) {
			String error_message = "";
			error_message = BaseMessages.getString(PKG, "PeselNipMessMeta.Check.FieldMissing");
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, error_message, stepMeta);
			remarks.add(cr);
		}

		if (input.length > 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK,
					BaseMessages.getString(PKG, "PeselNipMessMeta.Check.ReceivingInfoFromOtherSteps"), stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR,
					BaseMessages.getString(PKG, "PeselNipMessMeta.Check.NoInpuReceived"), stepMeta);
			remarks.add(cr);
		}

	}

}
