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

import pl.sanmargar.sdtt.PeselValidator;
import pl.sanmargar.sdtt.PeselValidatorResult;

/**
 * @author rafal_jot
 */
public class PESELValidatorStep extends BaseStep implements StepInterface {

	public PESELValidatorStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta,
			Trans trans) {
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}

	private static Class<?> PKG = PESELValidatorStep.class;

	private PESELValidatorMeta meta;
	private PESELValidatorData data;

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
		meta = (PESELValidatorMeta) smi;
		data = (PESELValidatorData) sdi;

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

			meta.setSourcePeselFieldName(environmentSubstitute(meta.getSourcePeselFieldName()));
			meta.setResultBirthDateFieldName(environmentSubstitute(meta.getResultBirthDateFieldName()));
			meta.setResultGenderFieldName(environmentSubstitute(meta.getResultGenderFieldName()));
			meta.setResultFieldName(environmentSubstitute(meta.getResultFieldName()));

			data.indexOfSourcePeselField = data.inputRowMeta.indexOfValue(meta.getSourcePeselFieldName());

			if (data.indexOfSourcePeselField < 0) {
				logError(BaseMessages.getString(PKG, "PESELValidator.Exception.CouldnotFindField",
						meta.getSourcePeselFieldName()));
				setErrors(1);
				stopAll();
				return false;
			}
			if (!data.inputRowMeta.getValueMeta(data.indexOfSourcePeselField).isString()) {
				logError(BaseMessages.getString(PKG, "PESELValidator.Log.InputFieldIsNotAString",
						meta.getSourcePeselFieldName()));
				setErrors(1);
				stopAll();
				return false;
			}

			data.indexOfResultField = data.outputRowMeta.indexOfValue(meta.getResultFieldName());
			if (meta.isBirthDateCheck())
				data.indexOfResultBirthDateField = data.outputRowMeta.indexOfValue(meta.getResultBirthDateFieldName());
			if (meta.isGenderCheck())
				data.indexOfResultGenderField = data.outputRowMeta.indexOfValue(meta.getResultGenderFieldName());
			if (meta.isStandardizeCheck())
				data.indexOfResultStdPeselField = data.outputRowMeta.indexOfValue(meta.getResultStdPeselFieldName());
		}
		
/*		if (r.length < data.outputRowMeta.size()+1) {
			r = RowDataUtil.resizeArray(r, r.length+10);
		}*/
		Object[] row = calcRow(data.inputRowMeta, r);
		putRow(data.outputRowMeta, row);

		if (checkFeedback(getLinesRead())) {
			if (log.isBasic()) {
				logBasic(BaseMessages.getString(PKG, "PESELValidator.Log.LineNumber") + getLinesRead());
			}
		}
		return true;
	}


	private synchronized Object[] calcRow(RowMetaInterface inputRowMeta, Object[] inputRowData)
			throws KettleValueException {

		String inputValue = Const.NVL(data.inputRowMeta.getString(inputRowData, data.indexOfSourcePeselField), "");
		PeselValidatorResult pr = PeselValidator.getPESELValidation(inputValue);

		Object[] outputRowData = inputRowData;
		/*
		if (data.indexOfResultField != -1) outputRowData[data.indexOfResultField] = pr.isPeselValid;
		if (data.indexOfResultGenderField != -1) outputRowData[data.indexOfResultGenderField] = pr.gender;
		if (data.indexOfResultBirthDateField != -1) outputRowData[data.indexOfResultBirthDateField] = pr.birthDate;
		if (data.indexOfResultStdPeselField != -1) outputRowData[data.indexOfResultStdPeselField] = pr.stdPesel;
		*/
		if (data.indexOfResultField != -1) RowDataUtil.addValueData(outputRowData, data.indexOfResultField, pr.isPeselValid);
		if (data.indexOfResultGenderField != -1) RowDataUtil.addValueData(outputRowData, data.indexOfResultGenderField, pr.gender);
		if (data.indexOfResultBirthDateField != -1) RowDataUtil.addValueData(outputRowData, data.indexOfResultBirthDateField, pr.birthDate);
		if (data.indexOfResultStdPeselField != -1) RowDataUtil.addValueData(outputRowData, data.indexOfResultStdPeselField, pr.stdPesel);
		return outputRowData;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (PESELValidatorMeta) smi;
		data = (PESELValidatorData) sdi;
		if (super.init(smi, sdi)) {
			return true;
		}
		return false;
	}

}
