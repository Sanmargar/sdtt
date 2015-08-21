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

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import pl.sanmargar.sdtt.NipValidator;
import pl.sanmargar.sdtt.NipValidatorResult;
import pl.sanmargar.sdtt.RegonValidator;
import pl.sanmargar.sdtt.RegonValidatorResult;

/**
 * @author rafal_jot
 */
public class RegonValidatorStep extends BaseStep implements StepInterface {

	public RegonValidatorStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta,
			Trans trans) {
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}

	private static Class<?> PKG = RegonValidatorStep.class;

	private RegonValidatorMeta meta;
	private RegonValidatorData data;

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
		meta = (RegonValidatorMeta) smi;
		data = (RegonValidatorData) sdi;

		Object[] r = getRow();

		if (r == null) {
			setOutputDone();
			return false;
		}

		if (first) {
			first = false;
			data.inputRowMeta = getInputRowMeta();
			data.outputRowMeta = getInputRowMeta().clone();
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this, repository, metaStore);

			meta.setSourceRegonFieldName(environmentSubstitute(meta.getSourceRegonFieldName()));
			meta.setResultRegionFieldName(environmentSubstitute(meta.getResultRegionFieldName()));
			meta.setResultFieldName(environmentSubstitute(meta.getResultFieldName()));

			data.indexOfSourceRegonField = data.inputRowMeta.indexOfValue(meta.getSourceRegonFieldName());

			if (data.indexOfSourceRegonField < 0) {
				logError(BaseMessages.getString(PKG, "NIPValidator.Exception.CouldnotFindField",
						meta.getSourceRegonFieldName()));
				setErrors(1);
				stopAll();
				return false;
			}
			if (!data.inputRowMeta.getValueMeta(data.indexOfSourceRegonField).isString()) {
				logError(BaseMessages.getString(PKG, "NIPValidator.Log.InputFieldIsNotAString",
						meta.getSourceRegonFieldName()));
				setErrors(1);
				stopAll();
				return false;
			}

			data.indexOfResultField = data.outputRowMeta.indexOfValue(meta.getResultFieldName());
			if (meta.isRegionCheck())
				data.indexOfResultRegionField = data.outputRowMeta.indexOfValue(meta.getResultRegionFieldName());
			if (meta.isStandardizeCheck())
				data.indexOfResultStdRegonField = data.outputRowMeta.indexOfValue(meta.getResultStdRegonFieldName());
		}
		
/*		if (r.length < data.outputRowMeta.size()+1) {
			r = RowDataUtil.resizeArray(r, r.length+10);
		}*/
		Object[] row = calcRow(data.inputRowMeta, r);
		putRow(data.outputRowMeta, row);

		if (checkFeedback(getLinesRead())) {
			if (log.isBasic()) {
				logBasic(BaseMessages.getString(PKG, "NIPValidator.Log.LineNumber") + getLinesRead());
			}
		}
		return true;
	}


	private synchronized Object[] calcRow(RowMetaInterface inputRowMeta, Object[] inputRowData)
			throws KettleValueException {

		String inputValue = Const.NVL(data.inputRowMeta.getString(inputRowData, data.indexOfSourceRegonField), "");
		RegonValidatorResult pr = RegonValidator.RegonValidate(inputValue);

		Object[] outputRowData = inputRowData;
		/*
		if (data.indexOfResultField != -1) outputRowData[data.indexOfResultField] = pr.isPeselValid;
		if (data.indexOfResultGenderField != -1) outputRowData[data.indexOfResultGenderField] = pr.gender;
		if (data.indexOfResultBirthDateField != -1) outputRowData[data.indexOfResultBirthDateField] = pr.birthDate;
		if (data.indexOfResultStdPeselField != -1) outputRowData[data.indexOfResultStdPeselField] = pr.stdPesel;
		*/
		if (data.indexOfResultField != -1) RowDataUtil.addValueData(outputRowData, data.indexOfResultField, pr.isRegonValid);
		if (data.indexOfResultRegionField != -1) RowDataUtil.addValueData(outputRowData, data.indexOfResultRegionField, pr.Region);
		if (data.indexOfResultStdRegonField != -1) RowDataUtil.addValueData(outputRowData, data.indexOfResultStdRegonField, pr.stdRegon);
		return outputRowData;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (RegonValidatorMeta) smi;
		data = (RegonValidatorData) sdi;
		if (super.init(smi, sdi)) {
			return true;
		}
		return false;
	}

}
