package hello.jseditor;

import hello.jseditor.actions.OpenAction;
import hello.jseditor.actions.RedoAction;
import hello.jseditor.actions.SaveAction;
import hello.jseditor.actions.FindAction;
import hello.jseditor.actions.UndoAction;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.TextViewerUndoManager;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author cyper.yin
 * 
 */
public class JSEditor extends ApplicationWindow {

	public SourceViewer sourceViewer;
	public IUndoManager undoManager;
	public MyDocument document = new MyDocument(this);
	public EventManager eventManager = new EventManager(this);
	
	private IAction openAction = new OpenAction(this);
	private IAction saveAction = new SaveAction(this);
	private IAction undoAction = new UndoAction(this);
	private IAction redoAction = new RedoAction(this);
	private IAction findAction = new FindAction(this);

	public JSEditor() {
		super(null);

		// 还要手动添加toolbar，真TMD的恶心.
		this.addToolBar(SWT.FLAT);

	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setSize(600, 400);
		shell.setText("Cyper JavaScript Editor");
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite top = new Composite(parent, SWT.None);
		top.setLayout(new FillLayout());

		// VerticalRuler是神马?
		sourceViewer = new SourceViewer(top, new VerticalRuler(10),
				SWT.V_SCROLL | SWT.H_SCROLL);

		final Font font = new Font(getShell().getDisplay(), /* "Tahoma" */
		"Verdana", 12, SWT.NORMAL);
		sourceViewer.getTextWidget().setFont(font);
		sourceViewer.getTextWidget().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				font.dispose();
			}
		});

		sourceViewer.setDocument(document);

		// 语法着色 + 代码提示
		final SourceViewerConfiguration config = new MySourceViewerConfiguration();
		sourceViewer.configure(config);

		// 使Alt+/也能触发代码提示
		VerifyKeyListener verifyKeyListener = new VerifyKeyListener() {
			public void verifyKey(VerifyEvent event) {
				// Check for Alt+/
				if (event.stateMask == SWT.ALT && event.character == '/') {
					// Check if source viewer is able to perform operation
					if (sourceViewer
							.canDoOperation(SourceViewer.CONTENTASSIST_PROPOSALS)) {
						// Perform operation
						sourceViewer
								.doOperation(SourceViewer.CONTENTASSIST_PROPOSALS);
					}
					// Veto this key press to avoid further processing
					event.doit = false;
				}
			}
		};
		sourceViewer.appendVerifyKeyListener(verifyKeyListener);

		undoManager = new TextViewerUndoManager(100);
		undoManager.connect(sourceViewer);

		// read here:
		// http://www.eclipse.org/articles/StyledText%201/article1.html
		final StyledText text = sourceViewer.getTextWidget();
		text.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {

				if (e.stateMask == SWT.CTRL && e.keyCode == 'a') {
					// 实现Ctrl + A
					text.selectAll();

				} else if (e.stateMask == SWT.CTRL && e.keyCode == 'd') {
					// 模拟实现Eclipse中Ctrl+D的功能
					// note that select.y is the length of the selection
					Point select = text.getSelectionRange();
					int startLine = text.getLineAtOffset(select.x);
					int startLineOffset = text.getOffsetAtLine(startLine);
					int endLine = text.getLineAtOffset(select.x + select.y);
					int lineCount = endLine - startLine + 1;
					for (int i = 0; i < lineCount; i++) {
						String line = text.getLine(startLine);
						// 尝试删除行(包含行尾的\r\n，如果有，没有会报异常)
						try {
							text.replaceTextRange(startLineOffset,
									line.length() + 2, "");
						} catch (Exception e2) {
							text.replaceTextRange(startLineOffset,
									line.length(), "");
						}
					}
				} else if (e.stateMask == SWT.CTRL && e.keyCode == 'z') {
					undoAction.run();
				} else if (e.stateMask == SWT.CTRL && e.keyCode == 'y') {
					redoAction.run();
				} else if (e.stateMask == SWT.CTRL && e.keyCode == 'f') {
					findAction.run();
				} else if (e.stateMask == SWT.CTRL && e.keyCode == 'o') {
					openAction.run();
				} else if (e.stateMask == SWT.CTRL && e.keyCode == 's') {
					saveAction.run();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		return parent;
	}

	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolbarManager = new ToolBarManager();
		toolbarManager.add(openAction);
		toolbarManager.add(saveAction);
		toolbarManager.add(undoAction );
		toolbarManager.add(redoAction );
		toolbarManager.add(findAction);
		return toolbarManager;
	}

	public static void main(String[] args) {
		JSEditor test = new JSEditor();
		test.setBlockOnOpen(true);
		test.open();
	}

}
