package hello;

import java.io.File;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class TreeViewerExample extends ApplicationWindow {

	public TreeViewerExample() {
		super(null);
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setSize(300,500);
		shell.setText("TreeViewer demo");
	}
	@Override
	protected Control createContents(Composite parent) {
		TreeViewer tv = new TreeViewer(parent);
		tv.setContentProvider(new MyTreeContentProvider());
		tv.setLabelProvider(new MyTreeLabelProvider());
		tv.setInput("anything");
		return parent;
	}
	
	public static void main(String[] args) {
		TreeViewerExample test = new TreeViewerExample();
		test.setBlockOnOpen(true);
		
		test.open();
	}
	
	
	//content provider provide data array.
	class MyTreeContentProvider implements ITreeContentProvider{
		public void dispose() {
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		
		//getElements provides first level data.
		public Object[] getElements(Object inputElement) {
			File[] roots = File.listRoots();
			return roots;
		}
		public Object[] getChildren(Object parentElement) {
			File parent = (File) parentElement;
			return parent.listFiles();
		}
		public Object getParent(Object element) {
			return ((File)element).getParentFile();
		}
		public boolean hasChildren(Object element) {
			Object[] obj = getChildren(element);
			return obj!=null && obj.length>0;
		}
	}
	//
	class MyTreeLabelProvider implements ILabelProvider {
		public void removeListener(ILabelProviderListener listener) {
		}
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}
		public void dispose() {
		}
		public void addListener(ILabelProviderListener listener) {
		}
		public String getText(Object element) {
			File file = (File)element;
			String name = file.getName();
			//root disk has no name
			if (name!=null && name.length()>0) {
				return name;
			}else{
				return file.getPath();
			}
		}
		public Image getImage(Object element) {
			return null;
		}
	}
}


