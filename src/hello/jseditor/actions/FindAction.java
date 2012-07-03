package hello.jseditor.actions;

import hello.jseditor.JSEditor;
import hello.jseditor.dialog.FindAndReplace;

import org.eclipse.jface.action.Action;

public class FindAction extends Action {
	private JSEditor jsEditor;

	public FindAction(JSEditor jsEditor) {
		super("Find@Ctrl+F");
		this.jsEditor = jsEditor;
	}
	
	@Override
	public void run() {
		new FindAndReplace(jsEditor, jsEditor.getShell()).open();
	}
}
