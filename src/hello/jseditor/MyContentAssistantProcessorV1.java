package hello.jseditor;

import static hello.jseditor.Constants.assistant_keywords;
import static hello.jseditor.Constants.built_in_obj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class MyContentAssistantProcessorV1 implements IContentAssistProcessor {

	// 获得内容的提示数组
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		System.out.println("computeCompletionProposals");
		final int cursor = offset;

		// (1)取得用户已经输入的字符
		IDocument doc = viewer.getDocument();
		// offset为当前光标所在的位置
		StringBuffer sb = new StringBuffer();
		
		
		boolean hasDot = false;
		try {
			char lastChar = doc.getChar(offset-1);
			if (lastChar == '.') {
				hasDot = true;
				offset--;//用户输入的最后一个点不计算在内.
			}
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		
		// 从当前位置向后查找，直到遇到空格 或.号为止，然后将字符串反转
		while (true) {
			try {
				// 获得前一个字符
				char c = doc.getChar(--offset);
				if (Character.isWhitespace(c)) {
					break;
				} else if (c == '.') {
					break;
				}
				sb.append(c);
			} catch (BadLocationException e) {
				break;
			}
		}
		String userEntered = sb.reverse().toString();
		System.out.println("userEntered="+userEntered);

		
		List<CompletionProposal> list = new ArrayList<CompletionProposal>();
		if (hasDot) {
			list = handleMethodProposal(cursor, userEntered);
		}else{
			list = handleKeywordProposal(cursor, userEntered);
		}
		
		return list.toArray(new CompletionProposal[list.size()]);
	}

	private List<CompletionProposal> handleMethodProposal(final int cursor, String userEntered) {
		List<CompletionProposal> list = new ArrayList<CompletionProposal>();
		Map<String,List<String>> tips = ResourceManager.getTips();
		if(tips.keySet().contains(userEntered)){
			List<String> methods = tips.get(userEntered);
			for(String method: methods){
				String replacementString = userEntered + "." +  method;
				int replacementOffset = cursor - userEntered.length() - 1;
				int replacementLength = userEntered.length() + 1;
				int newCursorPosition = replacementString.length() + 1;
				String displayString = method;
				CompletionProposal proposal = new CompletionProposal(
						replacementString, replacementOffset,
						replacementLength, newCursorPosition,null,displayString,null,null);
				list.add(proposal);
			}
		}
		
		return list;
	}
	
	private List<CompletionProposal> handleKeywordProposal(final int cursor, String userEntered) {
		List<CompletionProposal> list = new ArrayList<CompletionProposal>();
		for (int i = 0; i < assistant_keywords.length; i++) {
			if (assistant_keywords[i].startsWith(userEntered)) {
				String replacementString = assistant_keywords[i];
				int replacementOffset = cursor - userEntered.length();
				int replacementLength = userEntered.length();
				int newCursorPosition = replacementString.length() + 1;
				String displayString = assistant_keywords[i];
				CompletionProposal proposal = new CompletionProposal(
						replacementString, replacementOffset,
						replacementLength, newCursorPosition,null,displayString,null,null);
				list.add(proposal);
			}
		}
		return list;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[]{'.'};
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

}
