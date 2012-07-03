package hello.jseditor.actions;

import hello.jseditor.JSEditor;

import org.eclipse.jface.action.Action;

public class OpenAction extends Action{
	private JSEditor editor;
	public OpenAction(JSEditor editor) {
		this.editor = editor;
	}
	
	public String getText() {
		//没有图片可以，必须有文字，不然就是一个红点，丑得很.
		return "Open";
	}

	@Override
	public void run() {
		try {
			editor.getEventManager().openFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
