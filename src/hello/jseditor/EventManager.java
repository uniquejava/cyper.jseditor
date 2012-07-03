package hello.jseditor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.FileDialog;

public class EventManager {
	private JSEditor jsEditor;

	public EventManager(JSEditor jsEditor) {
		this.jsEditor = jsEditor;
	}

//	public void openFile() throws Exception {
//		MyDocument mydoc = (MyDocument) jsEditor.document;
//		mydoc.open();
//	}
	
//	public void saveFile(){
//		if (!jsEditor.document.isDirty()) {
//			return;
//		}
//		
//		boolean b = MessageDialog.openConfirm(jsEditor.getShell(), "cyper", "would you like to save?");
//		if (b) {
//			if (jsEditor.document.getFileName()==null) {
//				FileDialog dialog = new FileDialog(jsEditor.getShell(), SWT.SAVE);
//				dialog.setFilterExtensions(new String[] { "*.js", "*.*" });
//				String name = dialog.open();
//				jsEditor.document.setFileName(name);
//			}
//			jsEditor.document.save();
//		}
//	}

	public boolean howAboutFind(FindReplaceDocumentAdapter findAdapter,
			String findString, boolean forwardSearch, boolean caseSensitive,
			boolean wholeWord, boolean regExSearch) {
		boolean bFind = false;
		IRegion region = null;
		try {
			int startOffset = jsEditor.sourceViewer.getTextWidget()
					.getCaretOffset();
			if (!forwardSearch) {
				Point pt = jsEditor.sourceViewer.getSelectedRange();
				if (pt.x != pt.y) {
					startOffset = pt.x - 1;
				}
			}
			if (startOffset > findAdapter.length()) {
				startOffset = findAdapter.length() - 1;
			}
			if (startOffset < 0) {
				startOffset = 0;
			}
			region = findAdapter.find(startOffset, findString, forwardSearch,
					caseSensitive, wholeWord, regExSearch);
			if (region != null) {
				jsEditor.sourceViewer.setSelectedRange(region.getOffset(),
						region.getLength());
				bFind = true;
			}

		} catch (Exception e) {
		}
		return bFind;
	}

	public void doReplace(FindReplaceDocumentAdapter findAdapter,
			String replaceText, boolean regExReplace) {
		try {
			findAdapter.replace(replaceText, regExReplace);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
