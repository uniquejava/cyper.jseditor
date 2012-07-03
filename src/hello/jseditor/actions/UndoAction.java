package hello.jseditor.actions;

import hello.jseditor.JSEditor;

import org.eclipse.jface.action.Action;

public class UndoAction extends Action {
	private JSEditor jsEditor;

	public UndoAction(JSEditor jsEditor) {
		super("Undo@Ctrl+Z");
		this.jsEditor = jsEditor;
	}

	@Override
	public void run() {
		jsEditor.undoManager.undo();
	}
}
