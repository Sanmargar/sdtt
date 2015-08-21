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

public class NipPeselRegonMessDialogContent extends Group {

	private static Class<?> PKG = NipPeselRegonMessMeta.class;

	private boolean gotPreviousFields = false;

	private Combo wSourcePeselField;
	private Combo wSourceNipField;
	private Combo wSourceRegonField;

	private Text wResultPeselField;
	private Text wResultNipField;
	private Text wResultRegonField;

	private Text wResultField;

	private Shell shell;
	private String stepname;
	private NipPeselRegonMessMeta meta;
	private TransMeta transMeta;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */

	public NipPeselRegonMessDialogContent(Composite parent, int style, Object in, TransMeta tMeta, String sname) {
		super(parent, style);
		setLayout(new FormLayout());

		shell = (Shell) parent;
		stepname = sname;
		transMeta = tMeta;
		meta = (NipPeselRegonMessMeta) in;

		// Source PESEL field
		wSourcePeselField = new Combo(this, SWT.BORDER);
		FormData fd_wSourcePeselField = new FormData();
		fd_wSourcePeselField.left = new FormAttachment(0, 140);
		fd_wSourcePeselField.top = new FormAttachment(0, 0);
		fd_wSourcePeselField.right = new FormAttachment(100, 0);
		wSourcePeselField.setLayoutData(fd_wSourcePeselField);
		wSourcePeselField.addFocusListener(new FocusListener() {
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
		Label wlSourcePeselField = new Label(this, SWT.NONE);
		FormData fd_wlSourcePeselField = new FormData();
		fd_wlSourcePeselField.top = new FormAttachment(wSourcePeselField, 0, SWT.TOP);
		fd_wlSourcePeselField.right = new FormAttachment(wSourcePeselField, 0);
		wlSourcePeselField.setLayoutData(fd_wlSourcePeselField);
		wlSourcePeselField
				.setText(BaseMessages.getString(PKG, "PeselNipRegonMessDialogContent.SourcePeselField.Label"));

		// Source NIP field
		wSourceNipField = new Combo(this, SWT.BORDER);
		FormData fd_wSourceNipField = new FormData();
		fd_wSourceNipField.left = new FormAttachment(0, 140);
		fd_wSourceNipField.top = new FormAttachment(wSourcePeselField, 10);
		fd_wSourceNipField.right = new FormAttachment(100, 0);
		wSourceNipField.setLayoutData(fd_wSourceNipField);
		wSourceNipField.addFocusListener(new FocusListener() {
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
		Label wlSourceNipField = new Label(this, SWT.NONE);
		FormData fd_wlSourceNipField = new FormData();
		fd_wlSourceNipField.top = new FormAttachment(wSourceNipField, 0, SWT.TOP);
		fd_wlSourceNipField.right = new FormAttachment(wSourceNipField, 0);
		wlSourceNipField.setLayoutData(fd_wlSourceNipField);
		wlSourceNipField.setText(BaseMessages.getString(PKG, "PeselNipRegonMessDialogContent.SourceNipField.Label"));

		// Source REGON field
		wSourceRegonField = new Combo(this, SWT.BORDER);
		FormData fd_wSourceRegonField = new FormData();
		fd_wSourceRegonField.left = new FormAttachment(0, 140);
		fd_wSourceRegonField.top = new FormAttachment(wSourceNipField, 10);
		fd_wSourceRegonField.right = new FormAttachment(100, 0);
		wSourceRegonField.setLayoutData(fd_wSourceRegonField);
		wSourceRegonField.addFocusListener(new FocusListener() {
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
		Label wlSourceRegonField = new Label(this, SWT.NONE);
		FormData fd_wlSourceRegonField = new FormData();
		fd_wlSourceRegonField.top = new FormAttachment(wSourceRegonField, 0, SWT.TOP);
		fd_wlSourceRegonField.right = new FormAttachment(wSourceRegonField, 0);
		wlSourceRegonField.setLayoutData(fd_wlSourceRegonField);
		wlSourceRegonField
				.setText(BaseMessages.getString(PKG, "PeselNipRegonMessDialogContent.SourceRegonField.Label"));

		// Result PESEL field
		wResultPeselField = new Text(this, SWT.BORDER);
		FormData fd_wResultPeselField = new FormData();
		fd_wResultPeselField.top = new FormAttachment(wSourceRegonField, 10);
		fd_wResultPeselField.left = new FormAttachment(0, 140);
		fd_wResultPeselField.right = new FormAttachment(100, 0);
		wResultPeselField.setLayoutData(fd_wResultPeselField);

		Label wlResultPeselField = new Label(this, SWT.NONE);
		FormData fd_wlResultPeselField = new FormData();
		fd_wlResultPeselField.top = new FormAttachment(wResultPeselField, 0, SWT.TOP);
		fd_wlResultPeselField.right = new FormAttachment(wResultPeselField, 0);
		wlResultPeselField.setLayoutData(fd_wlResultPeselField);
		wlResultPeselField
				.setText(BaseMessages.getString(PKG, "PeselNipRegonMessDialogContent.ResultPeselField.Label"));

		// Result NIP field
		wResultNipField = new Text(this, SWT.BORDER);
		FormData fd_wResultNipField = new FormData();
		fd_wResultNipField.top = new FormAttachment(wResultPeselField, 10);
		fd_wResultNipField.left = new FormAttachment(0, 140);
		fd_wResultNipField.right = new FormAttachment(100, 0);
		wResultNipField.setLayoutData(fd_wResultNipField);

		Label wlResultNipField = new Label(this, SWT.NONE);
		FormData fd_wlResultNipField = new FormData();
		fd_wlResultNipField.top = new FormAttachment(wResultNipField, 0, SWT.TOP);
		fd_wlResultNipField.right = new FormAttachment(wResultNipField, 0);
		wlResultNipField.setLayoutData(fd_wlResultNipField);
		wlResultNipField.setText(BaseMessages.getString(PKG, "PeselNipRegonMessDialogContent.ResultNipField.Label"));

		// Result REGON field
		wResultRegonField = new Text(this, SWT.BORDER);
		FormData fd_wResultRegonField = new FormData();
		fd_wResultRegonField.top = new FormAttachment(wResultNipField, 10);
		fd_wResultRegonField.left = new FormAttachment(0, 140);
		fd_wResultRegonField.right = new FormAttachment(100, 0);
		wResultRegonField.setLayoutData(fd_wResultRegonField);

		Label wlResultRegonField = new Label(this, SWT.NONE);
		FormData fd_wlResultRegonField = new FormData();
		fd_wlResultRegonField.top = new FormAttachment(wResultRegonField, 0, SWT.TOP);
		fd_wlResultRegonField.right = new FormAttachment(wResultRegonField, 0);
		wlResultRegonField.setLayoutData(fd_wlResultRegonField);
		wlResultRegonField
				.setText(BaseMessages.getString(PKG, "PeselNipRegonMessDialogContent.ResultRegonField.Label"));
		
		//Result Info field
		wResultField = new Text(this, SWT.BORDER);
		FormData fd_wResultField = new FormData();
		fd_wResultField.left = new FormAttachment(0, 140);
		fd_wResultField.top = new FormAttachment(wResultRegonField, 10);
		fd_wResultField.right = new FormAttachment(100, 0);
		wResultField.setLayoutData(fd_wResultField);

		Label wlResultFieldLabel = new Label(this, SWT.NONE);
		FormData fd_wlResultFieldLabel = new FormData();
		fd_wlResultFieldLabel.top = new FormAttachment(wResultField, 0, SWT.TOP);
		fd_wlResultFieldLabel.right = new FormAttachment(wResultField, 0);
		wlResultFieldLabel.setLayoutData(fd_wlResultFieldLabel);
		wlResultFieldLabel.setText(BaseMessages.getString(PKG, "PeselNipRegonMessDialogContent.ResultField.Label"));

	}

	private void get() {
		if (!gotPreviousFields) {
			try {
				String fnip = wSourceNipField.getText();
				String fpesel = wSourcePeselField.getText();
				String fregon = wSourceRegonField.getText();

				wSourceNipField.removeAll();
				wSourcePeselField.removeAll();
				wSourceRegonField.removeAll();

				RowMetaInterface r = transMeta.getPrevStepFields(stepname);
				if (r != null) {
					wSourceNipField.setItems(r.getFieldNames());
					wSourcePeselField.setItems(r.getFieldNames());
					wSourceRegonField.setItems(r.getFieldNames());
				}
				wSourceNipField.setText(fnip);
				wSourcePeselField.setText(fpesel);
				wSourceRegonField.setText(fregon);

				gotPreviousFields = true;
			} catch (KettleException ke) {
				new ErrorDialog(shell,
						BaseMessages.getString(PKG, "PESELValidatorDialogContent.FailedToGetFields.DialogTitle"),
						BaseMessages.getString(PKG, "PESELValidatorDialogContent.FailedToGetFields.DialogMessage"), ke);
			}
		}

		if (meta.getSourcePeselFieldName() != null	) wSourcePeselField.setText(meta.getSourcePeselFieldName());
		if (meta.getSourceNipFieldName() != null	) wSourceNipField.setText(meta.getSourceNipFieldName());
		if (meta.getSourceRegonFieldName() != null	) wSourceRegonField.setText(meta.getSourceRegonFieldName());
		
		if (meta.getResultPeselFieldName() != null	) wResultPeselField.setText(meta.getResultPeselFieldName());
		if (meta.getResultNipFieldName() != null	) wResultNipField.setText(meta.getResultNipFieldName());
		if (meta.getResultRegonFieldName() != null	) wResultRegonField.setText(meta.getResultRegonFieldName());

		if (meta.getResultFieldName() != null	) wResultField.setText(meta.getResultFieldName());
	}

	public void populateDialog() {
		if (meta.getSourcePeselFieldName() != null)
			wSourcePeselField.setText(meta.getSourcePeselFieldName());
		if (meta.getSourceNipFieldName() != null)
			wSourceNipField.setText(meta.getSourceNipFieldName());
		if (meta.getSourceRegonFieldName() != null)
			wSourceRegonField.setText(meta.getSourceRegonFieldName());

		if (meta.getResultPeselFieldName() != null)
			wResultPeselField.setText(meta.getResultPeselFieldName());
		if (meta.getResultNipFieldName() != null)
			wResultNipField.setText(meta.getResultNipFieldName());
		if (meta.getResultRegonFieldName() != null)
			wResultRegonField.setText(meta.getResultRegonFieldName());

		if (meta.getResultFieldName() != null)
			wResultField.setText(meta.getResultFieldName());

	}

	public void ok() {
		meta.setSourcePeselFieldName(wSourcePeselField.getText());
		meta.setSourceNipFieldName(wSourceNipField.getText());
		meta.setSourceRegonFieldName(wSourceRegonField.getText());

		meta.setResultPeselFieldName(wResultPeselField.getText());
		meta.setResultNipFieldName(wResultNipField.getText());
		meta.setResultRegonFieldName(wResultRegonField.getText());
		
		meta.setResultFieldName(wResultField.getText());
	}

	@Override
	protected void checkSubclass() {
	}
}
