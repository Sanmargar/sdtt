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

public class PESELValidatorDialogContent extends Group {

	private static Class<?> PKG = PESELValidatorMeta.class;

	private boolean gotPreviousFields = false;

	private Combo wSourceField;
	private Text wResultStdPeselField;
	private Text wResultGenderField;
	private Text wResultBirthDateField;
	private Text wResultField;

	private Button wStandardize;
	private Button wExtractGender;
	private Button wExtractBirthDate;

	private Shell shell;
	private String stepname;
	private PESELValidatorMeta meta;
	private TransMeta transMeta;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */

	public PESELValidatorDialogContent(Composite parent, int style, Object in, TransMeta tMeta, String sname) {
		super(parent, style);
		setLayout(new FormLayout());

		shell = (Shell) parent;
		stepname = sname;
		transMeta = tMeta;
		meta = (PESELValidatorMeta) in;

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
		wlSourceField.setText(BaseMessages.getString(PKG, "PESELValidatorDialogContent.SourceField.Label"));

		wStandardize = new Button(this, SWT.CHECK);
		wStandardize.setSelection(true);
		wStandardize.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.widget;
				wResultStdPeselField.setEnabled(b.getSelection());
			}
		});
		FormData fd_wStandardize = new FormData();
		fd_wStandardize.top = new FormAttachment(wSourceField, 10);
		fd_wStandardize.left = new FormAttachment(0, 10);
		wStandardize.setLayoutData(fd_wStandardize);
		wStandardize.setText(BaseMessages.getString(PKG, "PESELValidatorDialogContent.Standardize"));

		wResultStdPeselField = new Text(this, SWT.BORDER);
		FormData fd_wOutputField = new FormData();
		fd_wOutputField.left = new FormAttachment(0, 140);
		fd_wOutputField.top = new FormAttachment(wStandardize, 10);
		fd_wOutputField.right = new FormAttachment(100, 0);
		wResultStdPeselField.setLayoutData(fd_wOutputField);

		Label wlOutputField = new Label(this, SWT.NONE);
		FormData fd_wlOutputField = new FormData();
		fd_wlOutputField.top = new FormAttachment(wResultStdPeselField, 0, SWT.TOP);
		fd_wlOutputField.right = new FormAttachment(wResultStdPeselField, 0);
		wlOutputField.setLayoutData(fd_wlOutputField);
		wlOutputField.setText(BaseMessages.getString(PKG, "PESELValidatorDialogContent.OutputField.Label")); //$NON-NLS-1$

		wExtractGender = new Button(this, SWT.CHECK);
		wExtractGender.setSelection(true);
		wExtractGender.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.widget;
				wResultGenderField.setEnabled(b.getSelection());
			}
		});
		FormData fd_wExtractGender = new FormData();
		fd_wExtractGender.top = new FormAttachment(wResultStdPeselField, 10);
		fd_wExtractGender.left = new FormAttachment(0, 10);
		wExtractGender.setLayoutData(fd_wExtractGender);
		wExtractGender.setText(BaseMessages.getString(PKG, "PESELValidatorDialogContent.ExtractGender"));

		wResultGenderField = new Text(this, SWT.BORDER);
		FormData fd_wGenderField = new FormData();
		fd_wGenderField.left = new FormAttachment(0, 140);
		fd_wGenderField.top = new FormAttachment(wExtractGender, 10);
		fd_wGenderField.right = new FormAttachment(100, 0);
		wResultGenderField.setLayoutData(fd_wGenderField);

		Label wlGenderField = new Label(this, SWT.NONE);
		FormData fd_wlGenderField = new FormData();
		fd_wlGenderField.top = new FormAttachment(wResultGenderField, 0, SWT.TOP);
		fd_wlGenderField.right = new FormAttachment(wResultGenderField, 0);
		wlGenderField.setLayoutData(fd_wlGenderField);
		wlGenderField.setText(BaseMessages.getString(PKG, "PESELValidatorDialogContent.GenderField.Label")); //$NON-NLS-1$

		wExtractBirthDate = new Button(this, SWT.CHECK);
		wExtractBirthDate.setSelection(true);
		wExtractBirthDate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.widget;
				wResultBirthDateField.setEnabled(b.getSelection());
			}
		});
		FormData fd_wExtractBirthDate = new FormData();
		fd_wExtractBirthDate.top = new FormAttachment(wResultGenderField, 10);
		fd_wExtractBirthDate.left = new FormAttachment(0, 10);
		wExtractBirthDate.setLayoutData(fd_wExtractBirthDate);
		wExtractBirthDate.setText(BaseMessages.getString(PKG, "PESELValidatorDialogContent.ExtractBirthDate"));

		wResultBirthDateField = new Text(this, SWT.BORDER);
		FormData fd_wBirthDateField = new FormData();
		fd_wBirthDateField.left = new FormAttachment(0, 140);
		fd_wBirthDateField.top = new FormAttachment(wExtractBirthDate, 10);
		fd_wBirthDateField.right = new FormAttachment(100, 0);
		wResultBirthDateField.setLayoutData(fd_wBirthDateField);

		Label wlBirthDateField = new Label(this, SWT.NONE);
		FormData fd_wlBirthDateField = new FormData();
		fd_wlBirthDateField.top = new FormAttachment(wResultBirthDateField, 0, SWT.TOP);
		fd_wlBirthDateField.right = new FormAttachment(wResultBirthDateField, 0);
		wlBirthDateField.setLayoutData(fd_wlBirthDateField);
		wlBirthDateField.setText(BaseMessages.getString(PKG, "PESELValidatorDialogContent.BirthDateField.Label")); //$NON-NLS-1$

		wResultField = new Text(this, SWT.BORDER);
		FormData fd_wResultField = new FormData();
		fd_wResultField.left = new FormAttachment(0, 140);
		fd_wResultField.top = new FormAttachment(wResultBirthDateField, 10);
		fd_wResultField.right = new FormAttachment(100, 0);
		wResultField.setLayoutData(fd_wResultField);

		Label wlResultFieldLabel = new Label(this, SWT.NONE);
		FormData fd_wlResultFieldLabel = new FormData();
		fd_wlResultFieldLabel.top = new FormAttachment(wResultField, 0, SWT.TOP);
		fd_wlResultFieldLabel.right = new FormAttachment(wResultField, 0);
		wlResultFieldLabel.setLayoutData(fd_wlResultFieldLabel);
		wlResultFieldLabel.setText(BaseMessages.getString(PKG, "PESELValidatorDialogContent.ResultField.Label"));

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
						BaseMessages.getString(PKG, "PESELValidatorDialogContent.FailedToGetFields.DialogTitle"),
						BaseMessages.getString(PKG, "PESELValidatorDialogContent.FailedToGetFields.DialogMessage"), ke);
			}
		}

		wSourceField.setText(meta.getSourcePeselFieldName());
		wResultStdPeselField.setText(meta.getResultStdPeselFieldName());
		wResultGenderField.setText(meta.getResultGenderFieldName());
		wResultBirthDateField.setText(meta.getResultBirthDateFieldName());
		wResultField.setText(meta.getResultFieldName());

		wStandardize.setSelection(meta.isStandardizeCheck());
		wExtractGender.setSelection(meta.isGenderCheck());
		wExtractBirthDate.setSelection(meta.isBirthDateCheck());
	}

	public void populateDialog() {
		if (meta.getSourcePeselFieldName() != null)
			wSourceField.setText(meta.getSourcePeselFieldName());
		if (meta.getResultStdPeselFieldName() != null)
			wResultStdPeselField.setText(meta.getResultStdPeselFieldName());
		if (meta.getResultGenderFieldName() != null)
			wResultGenderField.setText(meta.getResultGenderFieldName());
		if (meta.getResultBirthDateFieldName() != null)
			wResultBirthDateField.setText(meta.getResultBirthDateFieldName());
		if (meta.getResultFieldName() != null)
			wResultField.setText(meta.getResultFieldName());

		wStandardize.setSelection(meta.isStandardizeCheck());
		wResultStdPeselField.setEnabled(meta.isStandardizeCheck());

		wExtractGender.setSelection(meta.isGenderCheck());
		wResultGenderField.setEnabled(meta.isGenderCheck());

		wExtractBirthDate.setSelection(meta.isBirthDateCheck());
		wResultBirthDateField.setEnabled(meta.isBirthDateCheck());
	}

	public void ok() {
		meta.setSourcePeselFieldName(wSourceField.getText());
		meta.setResultStdPeselFieldName(wResultStdPeselField.getText());
		meta.setResultFieldName(wResultField.getText());
		meta.setResultGenderFieldName(wResultGenderField.getText());
		meta.setResultBirthDateFieldName(wResultBirthDateField.getText());
		meta.setStandardizeCheck(wStandardize.getSelection());
		meta.setGenderCheck(wExtractGender.getSelection());
		meta.setBirthDateCheck(wExtractBirthDate.getSelection());
	}

	@Override
	protected void checkSubclass() {
	}
}
