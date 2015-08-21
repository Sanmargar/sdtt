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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Combo;

/**
 * @author rafal_jot
 */

public class RegonValidatorDialogContent extends Group {

	private static Class<?> PKG = RegonValidatorMeta.class;

	private boolean gotPreviousFields = false;

	private Combo wSourceField;
	private Text wResultStdRegonField;
	private Text wResultRegionField;
	private Text wResultField;

	private Button wStandardize;
	private Button wExtractRegion;

	private Shell shell;
	private String stepname;
	private RegonValidatorMeta meta;
	private TransMeta transMeta;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */

	public RegonValidatorDialogContent(Composite parent, int style, Object in, TransMeta tMeta, String sname) {
		super(parent, style);
		setLayout(new FormLayout());

		shell = (Shell) parent;
		stepname = sname;
		transMeta = tMeta;
		meta = (RegonValidatorMeta) in;

		wSourceField = new Combo(this, SWT.BORDER);
		FormData fd_wSourceField = new FormData();
		fd_wSourceField.left = new FormAttachment(0, 140);
		fd_wSourceField.top = new FormAttachment(0, 0);
		fd_wSourceField.right = new FormAttachment(100, 0);
		wSourceField.setLayoutData(fd_wSourceField);
		wSourceField.addFocusListener(new FocusListener() {
			public void focusLost(org.eclipse.swt.events.FocusEvent e) {
			}

			public void focusGained(org.eclipse.swt.events.FocusEvent e) {
				Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
				shell.setCursor(busy);
				get();
				shell.setCursor(null);
				busy.dispose();
			}
		});
		Label wlSourceField = new Label(this, SWT.NONE);
		FormData fd_wlSourceField = new FormData();
		fd_wlSourceField.top = new FormAttachment(wSourceField, 0, SWT.TOP);
		fd_wlSourceField.right = new FormAttachment(wSourceField, 0);
		wlSourceField.setLayoutData(fd_wlSourceField);
		wlSourceField.setText(BaseMessages.getString(PKG, "RegonValidatorDialogContent.SourceField.Label"));

		wStandardize = new Button(this, SWT.CHECK);
		wStandardize.setSelection(true);
		wStandardize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.widget;
				wResultStdRegonField.setEnabled(b.getSelection());
			}
		});
		FormData fd_wStandardize = new FormData();
		fd_wStandardize.top = new FormAttachment(wSourceField, 10);
		fd_wStandardize.left = new FormAttachment(0, 10);
		wStandardize.setLayoutData(fd_wStandardize);
		wStandardize.setText(BaseMessages.getString(PKG, "RegonValidatorDialogContent.Standardize"));

		wResultStdRegonField = new Text(this, SWT.BORDER);
		FormData fd_wOutputField = new FormData();
		fd_wOutputField.left = new FormAttachment(0, 140);
		fd_wOutputField.top = new FormAttachment(wStandardize, 10);
		fd_wOutputField.right = new FormAttachment(100, 0);
		wResultStdRegonField.setLayoutData(fd_wOutputField);

		Label wlOutputField = new Label(this, SWT.NONE);
		FormData fd_wlOutputField = new FormData();
		fd_wlOutputField.top = new FormAttachment(wResultStdRegonField, 0, SWT.TOP);
		fd_wlOutputField.right = new FormAttachment(wResultStdRegonField, 0);
		wlOutputField.setLayoutData(fd_wlOutputField);
		wlOutputField.setText(BaseMessages.getString(PKG, "RegonValidatorDialogContent.OutputField.Label")); 

		wExtractRegion = new Button(this, SWT.CHECK);
		wExtractRegion.setSelection(true);
		wExtractRegion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.widget;
				wResultRegionField.setEnabled(b.getSelection());
			}
		});
		FormData fd_wExtractRegion = new FormData();
		fd_wExtractRegion.top = new FormAttachment(wResultStdRegonField, 10);
		fd_wExtractRegion.left = new FormAttachment(0, 10);
		wExtractRegion.setLayoutData(fd_wExtractRegion);
		wExtractRegion.setText(BaseMessages.getString(PKG, "RegonValidatorDialogContent.ExtractRegion"));

		wResultRegionField = new Text(this, SWT.BORDER);
		FormData fd_wTaxOfficeNameField = new FormData();
		fd_wTaxOfficeNameField.left = new FormAttachment(0, 140);
		fd_wTaxOfficeNameField.top = new FormAttachment(wExtractRegion, 10);
		fd_wTaxOfficeNameField.right = new FormAttachment(100, 0);
		wResultRegionField.setLayoutData(fd_wTaxOfficeNameField);

		Label wlRegionField = new Label(this, SWT.NONE);
		FormData fd_wlRegionField = new FormData();
		fd_wlRegionField.top = new FormAttachment(wResultRegionField, 0, SWT.TOP);
		fd_wlRegionField.right = new FormAttachment(wResultRegionField, 0);
		wlRegionField.setLayoutData(fd_wlRegionField);
		wlRegionField.setText(BaseMessages.getString(PKG, "RegonValidatorDialogContent.RegionField.Label")); 

		wResultField = new Text(this, SWT.BORDER);
		FormData fd_wResultField = new FormData();
		fd_wResultField.left = new FormAttachment(0, 140);
		fd_wResultField.top = new FormAttachment(wResultRegionField, 10);
		fd_wResultField.right = new FormAttachment(100, 0);
		wResultField.setLayoutData(fd_wResultField);

		Label wlResultFieldLabel = new Label(this, SWT.NONE);
		FormData fd_wlResultFieldLabel = new FormData();
		fd_wlResultFieldLabel.top = new FormAttachment(wResultField, 0, SWT.TOP);
		fd_wlResultFieldLabel.right = new FormAttachment(wResultField, 0);
		wlResultFieldLabel.setLayoutData(fd_wlResultFieldLabel);
		wlResultFieldLabel.setText(BaseMessages.getString(PKG, "RegonValidatorDialogContent.ResultField.Label"));

	}

	private void get() {
		if (!gotPreviousFields) {
			try {
				String f = null;
				if (wSourceField.getText() != null) {
					f = wSourceField.getText();
				}

				wSourceField.removeAll();

				RowMetaInterface r = transMeta.getPrevStepFields(stepname);
				if (r != null) {
					wSourceField.setItems(r.getFieldNames());
				}
				if (f != null) {
					wSourceField.setText(f);
				}

				gotPreviousFields = true;
			} catch (KettleException ke) {
				new ErrorDialog(shell,
						BaseMessages.getString(PKG, "RegonValidatorDialogContent.FailedToGetFields.DialogTitle"),
						BaseMessages.getString(PKG, "RegonValidatorDialogContent.FailedToGetFields.DialogMessage"), ke);
			}
		}

		wSourceField.setText(meta.getSourceRegonFieldName());
		wResultStdRegonField.setText(meta.getResultStdRegonFieldName());
		wResultRegionField.setText(meta.getResultRegionFieldName());
		wResultField.setText(meta.getResultFieldName());

		wStandardize.setSelection(meta.isStandardizeCheck());
		wExtractRegion.setSelection(meta.isRegionCheck());
	}

	public void populateDialog() {
		if (meta.getSourceRegonFieldName() != null)
			wSourceField.setText(meta.getSourceRegonFieldName());
		if (meta.getResultStdRegonFieldName() != null)
			wResultStdRegonField.setText(meta.getResultStdRegonFieldName());
		if (meta.getResultRegionFieldName() != null)
			wResultRegionField.setText(meta.getResultRegionFieldName());
		if (meta.getResultFieldName() != null)
			wResultField.setText(meta.getResultFieldName());

		wStandardize.setSelection(meta.isStandardizeCheck());
		wResultStdRegonField.setEnabled(meta.isStandardizeCheck());

		wExtractRegion.setSelection(meta.isRegionCheck());
		wResultRegionField.setEnabled(meta.isRegionCheck());

	}

	public void ok() {
		meta.setSourceRegonFieldName(wSourceField.getText());
		meta.setResultStdRegonFieldName(wResultStdRegonField.getText());
		meta.setResultFieldName(wResultField.getText());
		meta.setResultRegionFieldName(wResultRegionField.getText());
		meta.setStandardizeCheck(wStandardize.getSelection());
		meta.setRegionCheck(wExtractRegion.getSelection());
	}

	@Override
	protected void checkSubclass() {
	}
}
