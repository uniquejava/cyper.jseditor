package hello.jseditor.actions;

import hello.jseditor.JSEditor;

import org.eclipse.jface.action.Action;

public class RedoAction extends Action {
	private JSEditor jsEditor;

	public RedoAction(JSEditor jsEditor) {
		super("Redo@Ctrl+Y");
		this.jsEditor = jsEditor;
	}

	@Override
	public void run() {
		jsEditor.undoManager.redo();
	}
}
