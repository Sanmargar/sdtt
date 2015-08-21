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
import pl.sanmargar.sdtt.PeselValidator;
import pl.sanmargar.sdtt.PeselValidatorResult;
import pl.sanmargar.sdtt.RegonValidator;
import pl.sanmargar.sdtt.RegonValidatorResult;

/**
 * @author rafal_jot
 */
public class NipPeselRegonMessStep extends BaseStep implements StepInterface {

	public NipPeselRegonMessStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta,
			Trans trans) {
		super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
	}

	private static Class<?> PKG = NipPeselRegonMessStep.class;

	private NipPeselRegonMessMeta meta;
	private NipPeselRegonMessData data;

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
		meta = (NipPeselRegonMessMeta) smi;
		data = (NipPeselRegonMessData) sdi;

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
			meta.setSourceNipFieldName(environmentSubstitute(meta.getSourceNipFieldName()));
			meta.setSourceRegonFieldName(environmentSubstitute(meta.getSourceRegonFieldName()));
			
			meta.setResultPeselFieldName(environmentSubstitute(meta.getResultPeselFieldName()));
			meta.setResultNipFieldName(environmentSubstitute(meta.getResultNipFieldName()));
			meta.setResultRegonFieldName(environmentSubstitute(meta.getResultRegonFieldName()));

			meta.setResultFieldName(environmentSubstitute(meta.getResultFieldName()));

			data.indexOfSourcePeselField = data.inputRowMeta.indexOfValue(meta.getSourcePeselFieldName());
			data.indexOfSourceNipField = data.inputRowMeta.indexOfValue(meta.getSourceNipFieldName());
			data.indexOfSourceRegonField = data.inputRowMeta.indexOfValue(meta.getSourceRegonFieldName());

			data.indexOfResultField = data.outputRowMeta.indexOfValue(meta.getResultFieldName());

			data.indexOfResultPeselField = data.outputRowMeta.indexOfValue(meta.getResultPeselFieldName());
			data.indexOfResultNipField = data.outputRowMeta.indexOfValue(meta.getResultNipFieldName());
			data.indexOfResultRegonField = data.outputRowMeta.indexOfValue(meta.getResultRegonFieldName());
			
/*			if (data.indexOfSourcePeselField < 0) {
				logError(BaseMessages.getString(PKG, "PeselNipRegonMess.Exception.CouldnotFindField",
						meta.getSourcePeselFieldName()));
				setErrors(1);
				stopAll();
				return false;
			}
			if (!data.inputRowMeta.getValueMeta(data.indexOfSourcePeselField).isString()) {
				logError(BaseMessages.getString(PKG, "PeselNipRegonMess.Log.InputFieldIsNotAString",
						meta.getSourcePeselFieldName()));
				setErrors(1);
				stopAll();
				return false;
			}*/
/*
			if (meta.isBirthDateCheck())
				data.indexOfResultBirthDateField = data.outputRowMeta.indexOfValue(meta.getResultBirthDateFieldName());
			if (meta.isGenderCheck())
				data.indexOfResultGenderField = data.outputRowMeta.indexOfValue(meta.getResultGenderFieldName());
			if (meta.isStandardizeCheck())
				data.indexOfResultStdPeselField = data.outputRowMeta.indexOfValue(meta.getResultStdPeselFieldName());
				*/
		}
		
/*		if (r.length < data.outputRowMeta.size()+1) {
			r = RowDataUtil.resizeArray(r, r.length+10);
		}*/
		
		Object[] row = calcRow(data.inputRowMeta, r);
		putRow(data.outputRowMeta, row);

		if (checkFeedback(getLinesRead())) {
			if (log.isBasic()) {
				logBasic(BaseMessages.getString(PKG, "PeselNipRegonMess.Log.LineNumber") + getLinesRead());
			}
		}
		return true;
	}


	private synchronized Object[] calcRow(RowMetaInterface inputRowMeta, Object[] inputRowData)
			throws KettleValueException {

		String inputPesel = Const.NVL(data.inputRowMeta.getString(inputRowData, data.indexOfSourcePeselField), "");
		String inputNip = Const.NVL(data.inputRowMeta.getString(inputRowData, data.indexOfSourceNipField), "");
		String inputRegon = Const.NVL(data.inputRowMeta.getString(inputRowData, data.indexOfSourceRegonField), "");
		
		String info = "";
		String peselVal = "";
		String nipVal = "";
		String regonVal ="";
		
		PeselValidatorResult vpesel = PeselValidator.getPESELValidation(inputPesel);
		if (vpesel.isPeselValid) {
			info = info.concat("PESEL:ok");
			peselVal = vpesel.stdPesel;
		}
		else {
			peselVal = "";
			vpesel = PeselValidator.getPESELValidation(inputNip);
			if (vpesel.isPeselValid) {
				info = info.concat("PESEL<-NIP");
				peselVal = vpesel.stdPesel;
			}
			else {
				vpesel = PeselValidator.getPESELValidation(inputRegon);
				if (vpesel.isPeselValid) {
					info = info.concat("PESEL<-REGON");
					peselVal = vpesel.stdPesel;
				}
				else {
					info = info.concat("PESEL:n/a");					
				}
			}
		} 
		
		NipValidatorResult vnip = NipValidator.NipValidate(inputNip);
		if (vnip.isNipValid) {
			info = info.concat(info.isEmpty() ? "":",").concat("NIP:ok");
			nipVal = vnip.stdNIP;
		} else {
			nipVal = "";
			vnip = NipValidator.NipValidate(inputPesel);
			if (vnip.isNipValid) {
				info = info.concat(info.isEmpty() ? "":",").concat("NIP<-PESEL");
				nipVal = vnip.stdNIP;
			} else {
				vnip = NipValidator.NipValidate(inputRegon);
				if (vnip.isNipValid) {
					info = info.concat(info.isEmpty() ? "":",").concat("NIP<-REGON");
					nipVal = vnip.stdNIP;;
				} else {
					info = info.concat(info.isEmpty() ? "":",").concat("NIP:n/a");
				}
			}
		}
		
		RegonValidatorResult vregon = RegonValidator.RegonValidate(inputRegon);
		if (vregon.isRegonValid) {
			info = info.concat(info.isEmpty() ? "":",").concat("REGON:ok");
			regonVal = vregon.stdRegon;
		} else {
			regonVal = "";
			vregon = RegonValidator.RegonValidate(inputPesel);
			if (vregon.isRegonValid) {
				info = info.concat(info.isEmpty() ? "":",").concat("REGON<-PESEL");
				regonVal = vregon.stdRegon;
			} else {
				vregon = RegonValidator.RegonValidate(inputNip);
				if (vregon.isRegonValid) {
					info = info.concat(info.isEmpty() ? "":",").concat("REGON<-NIP");
					regonVal = vregon.stdRegon;
				} else {
					info = info.concat(info.isEmpty() ? "":",").concat("REGON:n/a");
				}
			}
		}
		
		Object[] outputRowData = inputRowData;
		if (data.indexOfResultField != -1) RowDataUtil.addValueData(outputRowData, data.indexOfResultField, info);

		if (data.indexOfResultPeselField != -1) RowDataUtil.addValueData(outputRowData, data.indexOfResultPeselField, peselVal); 
		else {
			if (data.indexOfSourcePeselField != -1) RowDataUtil.addValueData(outputRowData, data.indexOfSourcePeselField, peselVal); 			
		}
		if (data.indexOfResultNipField != -1)  RowDataUtil.addValueData(outputRowData, data.indexOfResultNipField,   nipVal);
		else {
			if (data.indexOfSourceNipField != -1)   RowDataUtil.addValueData(outputRowData, data.indexOfSourceNipField,  nipVal);			
		}
		if (data.indexOfResultRegonField != -1) RowDataUtil.addValueData(outputRowData, data.indexOfResultRegonField, regonVal);
		else {
			if (data.indexOfSourceRegonField != -1) RowDataUtil.addValueData(outputRowData, data.indexOfSourceRegonField, regonVal);			
		}

		return outputRowData;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (NipPeselRegonMessMeta) smi;
		data = (NipPeselRegonMessData) sdi;
		if (super.init(smi, sdi)) {
			return true;
		}
		return false;
	}
}
