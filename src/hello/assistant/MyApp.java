package hello.assistant;
import java.util.ArrayList;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class MyApp extends ApplicationWindow {   

    private SourceViewer sourceViewer;

   
   
    final static char[] TRIGGER_TOKENS = new char[] { '.', ',' };

    VerifyKeyListener verifyKeyListener = new VerifyKeyListener() {
        public void verifyKey(VerifyEvent event) {
            // Check for Alt+/
            if (event.stateMask == SWT.ALT && event.character == '/') {
                // Check if source viewer is able to perform operation
                if (sourceViewer.canDoOperation(SourceViewer.CONTENTASSIST_PROPOSALS))
                    // Perform operation
                    sourceViewer.doOperation(SourceViewer.CONTENTASSIST_PROPOSALS);
                // Veto this key press to avoid further processing
                event.doit = false;
            }
        }
    };
   
   
    public MyApp(Shell parentShell) {
        super(parentShell);
        // TODO Auto-generated constructor stub
    }

   
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        MyApp app = new MyApp(null);
        app.setBlockOnOpen(true);
        app.open();
    }

    @Override
    protected void configureShell(Shell shell) {
        // TODO Auto-generated method stub
        super.configureShell(shell);
        shell.setSize(300, 200);
        shell.setText("Application");
    }

    @Override
    protected Control createContents(Composite parent) {
        // TODO Auto-generated method stub
        Control createContents = super.createContents(parent);
        Composite com = (Composite)createContents;
       
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth = 5;
        gridLayout.marginHeight = 5;
        gridLayout.verticalSpacing = 0;
        gridLayout.horizontalSpacing = 0;
        com.setLayout(gridLayout);
       
        new Label(com, SWT.NONE).setText("Input a text (Alt+/ for content assistant):");

       
        //Create a new source viewer
        sourceViewer = new SourceViewer(com, null, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
       
        //Configure source viewer, add content assistant support
        sourceViewer.configure(new SourceViewerConfiguration() {
            @Override
            public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
                ContentAssistant assistant = new ContentAssistant();
                IContentAssistProcessor cap = new MyContentAssistProcessor();
                assistant.setContentAssistProcessor(cap, IDocument.DEFAULT_CONTENT_TYPE);
                assistant.setInformationControlCreator(getInformationControlCreator(sourceViewer));
                assistant.enableAutoActivation(true);
                return assistant;
            }
        });
       
        //Set a blank document
        sourceViewer.setDocument(new Document(""));
        sourceViewer.setEditable(true);
       
        //Enable key stroke activation: Alt+/
        sourceViewer.appendVerifyKeyListener(verifyKeyListener);

        StyledText txtSource = sourceViewer.getTextWidget();
        GridData gd = new GridData(GridData.FILL_BOTH);
        txtSource.setLayoutData(gd);
       
       
       
        return com;
    }
   
   
    class MyContentAssistProcessor implements IContentAssistProcessor {

        public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
            String content = viewer.getTextWidget().getText();

            try {

                //Demo options
                final String[] options = new String[] { "sum()", "count()", "sort()" };

                //Dynamically generate proposal
                ArrayList result = new ArrayList();
                for (int i = 0; i < options.length; i++) {
                    CompletionProposal proposal = new CompletionProposal(options[i], offset, 0, options[i].length());
                    result.add(proposal);
                }
                return (ICompletionProposal[]) result.toArray(new ICompletionProposal[result.size()]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public char[] getCompletionProposalAutoActivationCharacters() {
            return TRIGGER_TOKENS;
        }

        public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
            return null;
        }

        public char[] getContextInformationAutoActivationCharacters() {
            return null;
        }

        public IContextInformationValidator getContextInformationValidator() {
            return null;
        }

        public String getErrorMessage() {
            return null;
        }

    }

}