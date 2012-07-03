package hello.jseditor.actions;

import hello.jseditor.JSEditor;

import org.eclipse.jface.action.Action;

public class SaveAction extends Action{
	private JSEditor editor;
	public SaveAction(JSEditor editor) {
		this.editor = editor;
	}
	
	public String getText() {
		//没有图片可以，必须有文字，不然就是一个红点，丑得很.
		return "Open@Ctrl+O";
	}

	@Override
	public void run() {
		try {
			editor.document.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
