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

public class NIPValidatorDialogContent extends Group {

	private static Class<?> PKG = NIPValidatorMeta.class;

	private boolean gotPreviousFields = false;

	private Combo wSourceField;
	private Text wResultStdNipField;
	private Text wResultTaxOfficeNameField;
	private Text wResultField;

	private Button wStandardize;
	private Button wExtractTaxOfficeName;

	private Shell shell;
	private String stepname;
	private NIPValidatorMeta meta;
	private TransMeta transMeta;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */

	public NIPValidatorDialogContent(Composite parent, int style, Object in, TransMeta tMeta, String sname) {
		super(parent, style);
		setLayout(new FormLayout());

		shell = (Shell) parent;
		stepname = sname;
		transMeta = tMeta;
		meta = (NIPValidatorMeta) in;

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
		wlSourceField.setText(BaseMessages.getString(PKG, "NIPValidatorDialogContent.SourceField.Label"));

		wStandardize = new Button(this, SWT.CHECK);
		wStandardize.setSelection(true);
		wStandardize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.widget;
				wResultStdNipField.setEnabled(b.getSelection());
			}
		});
		FormData fd_wStandardize = new FormData();
		fd_wStandardize.top = new FormAttachment(wSourceField, 10);
		fd_wStandardize.left = new FormAttachment(0, 10);
		wStandardize.setLayoutData(fd_wStandardize);
		wStandardize.setText(BaseMessages.getString(PKG, "NIPValidatorDialogContent.Standardize"));

		wResultStdNipField = new Text(this, SWT.BORDER);
		FormData fd_wOutputField = new FormData();
		fd_wOutputField.left = new FormAttachment(0, 140);
		fd_wOutputField.top = new FormAttachment(wStandardize, 10);
		fd_wOutputField.right = new FormAttachment(100, 0);
		wResultStdNipField.setLayoutData(fd_wOutputField);

		Label wlOutputField = new Label(this, SWT.NONE);
		FormData fd_wlOutputField = new FormData();
		fd_wlOutputField.top = new FormAttachment(wResultStdNipField, 0, SWT.TOP);
		fd_wlOutputField.right = new FormAttachment(wResultStdNipField, 0);
		wlOutputField.setLayoutData(fd_wlOutputField);
		wlOutputField.setText(BaseMessages.getString(PKG, "NIPValidatorDialogContent.OutputField.Label")); 

		wExtractTaxOfficeName = new Button(this, SWT.CHECK);
		wExtractTaxOfficeName.setSelection(true);
		wExtractTaxOfficeName.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.widget;
				wResultTaxOfficeNameField.setEnabled(b.getSelection());
			}
		});
		FormData fd_wExtractTaxOfficeName = new FormData();
		fd_wExtractTaxOfficeName.top = new FormAttachment(wResultStdNipField, 10);
		fd_wExtractTaxOfficeName.left = new FormAttachment(0, 10);
		wExtractTaxOfficeName.setLayoutData(fd_wExtractTaxOfficeName);
		wExtractTaxOfficeName.setText(BaseMessages.getString(PKG, "NIPValidatorDialogContent.ExtractTaxOfficeName"));

		wResultTaxOfficeNameField = new Text(this, SWT.BORDER);
		FormData fd_wTaxOfficeNameField = new FormData();
		fd_wTaxOfficeNameField.left = new FormAttachment(0, 140);
		fd_wTaxOfficeNameField.top = new FormAttachment(wExtractTaxOfficeName, 10);
		fd_wTaxOfficeNameField.right = new FormAttachment(100, 0);
		wResultTaxOfficeNameField.setLayoutData(fd_wTaxOfficeNameField);

		Label wlTaxOfficeNameField = new Label(this, SWT.NONE);
		FormData fd_wlTaxOfficeNameField = new FormData();
		fd_wlTaxOfficeNameField.top = new FormAttachment(wResultTaxOfficeNameField, 0, SWT.TOP);
		fd_wlTaxOfficeNameField.right = new FormAttachment(wResultTaxOfficeNameField, 0);
		wlTaxOfficeNameField.setLayoutData(fd_wlTaxOfficeNameField);
		wlTaxOfficeNameField.setText(BaseMessages.getString(PKG, "NIPValidatorDialogContent.TaxOfficeNameField.Label")); 

		wResultField = new Text(this, SWT.BORDER);
		FormData fd_wResultField = new FormData();
		fd_wResultField.left = new FormAttachment(0, 140);
		fd_wResultField.top = new FormAttachment(wResultTaxOfficeNameField, 10);
		fd_wResultField.right = new FormAttachment(100, 0);
		wResultField.setLayoutData(fd_wResultField);

		Label wlResultFieldLabel = new Label(this, SWT.NONE);
		FormData fd_wlResultFieldLabel = new FormData();
		fd_wlResultFieldLabel.top = new FormAttachment(wResultField, 0, SWT.TOP);
		fd_wlResultFieldLabel.right = new FormAttachment(wResultField, 0);
		wlResultFieldLabel.setLayoutData(fd_wlResultFieldLabel);
		wlResultFieldLabel.setText(BaseMessages.getString(PKG, "NIPValidatorDialogContent.ResultField.Label"));

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
						BaseMessages.getString(PKG, "NIPValidatorDialogContent.FailedToGetFields.DialogTitle"),
						BaseMessages.getString(PKG, "NIPValidatorDialogContent.FailedToGetFields.DialogMessage"), ke);
			}
		}

		wSourceField.setText(meta.getSourceNipFieldName());
		wResultStdNipField.setText(meta.getResultStdNipFieldName());
		wResultTaxOfficeNameField.setText(meta.getResultTaxOfficeFieldName());
		wResultField.setText(meta.getResultFieldName());

		wStandardize.setSelection(meta.isStandardizeCheck());
		wExtractTaxOfficeName.setSelection(meta.isTaxOfficeNameCheck());
	}

	public void populateDialog() {
		if (meta.getSourceNipFieldName() != null)
			wSourceField.setText(meta.getSourceNipFieldName());
		if (meta.getResultStdNipFieldName() != null)
			wResultStdNipField.setText(meta.getResultStdNipFieldName());
		if (meta.getResultTaxOfficeFieldName() != null)
			wResultTaxOfficeNameField.setText(meta.getResultTaxOfficeFieldName());
		if (meta.getResultFieldName() != null)
			wResultField.setText(meta.getResultFieldName());

		wStandardize.setSelection(meta.isStandardizeCheck());
		wResultStdNipField.setEnabled(meta.isStandardizeCheck());

		wExtractTaxOfficeName.setSelection(meta.isTaxOfficeNameCheck());
		wResultTaxOfficeNameField.setEnabled(meta.isTaxOfficeNameCheck());

	}

	public void ok() {
		meta.setSourceNipFieldName(wSourceField.getText());
		meta.setResultStdNipFieldName(wResultStdNipField.getText());
		meta.setResultFieldName(wResultField.getText());
		meta.setResultTaxOfficeNameFieldName(wResultTaxOfficeNameField.getText());
		meta.setStandardizeCheck(wStandardize.getSelection());
		meta.setTaxOfficeNameCheck(wExtractTaxOfficeName.getSelection());
	}

	@Override
	protected void checkSubclass() {
	}
}
