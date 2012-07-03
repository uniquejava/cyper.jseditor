package hello.jseditor;


import java.util.ArrayList;
import java.util.List;

import static hello.jseditor.Constants.*;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

public class MyTokenScanner extends RuleBasedScanner {
	private TextAttribute ta4Keyword;
	private TextAttribute ta4String;
	private TextAttribute ta4Comment;
	public MyTokenScanner(){
		ta4Keyword = new TextAttribute(ResourceManager.getColor(COLOR_KEYWORD), null, SWT.BOLD);
		ta4String = new TextAttribute(ResourceManager.getColor(COLOR_STRING));
		ta4Comment = new TextAttribute(ResourceManager.getColor(COLOR_COMMENT));
		
		//setup rules
		List rules = new ArrayList();
		
		//空格的规则
		rules.add(new WhitespaceRule(new IWhitespaceDetector() {
			public boolean isWhitespace(char c) {
				return Character.isWhitespace(c);
			}
		}));
		
		//字符串的
		rules.add(new SingleLineRule("'", "'",new Token(ta4String),'\\'));
		rules.add(new SingleLineRule("\"", "\"",new Token(ta4String),'\\'));
		//注释的
		rules.add(new MultiLineRule("/*", "*/", new Token(ta4Comment),'\\'));
		rules.add(new MultiLineRule("/**", "*/", new Token(ta4Comment),'\\'));
		rules.add(new EndOfLineRule("//", new Token(ta4Comment)));
		
		
		//关键字的规则
		WordRule keywordRule = new WordRule(new IWordDetector() {
			public boolean isWordStart(char c) {
				for (int i = 0; i < colored_keywords.length; i++) {
					if (c == colored_keywords[i].charAt(0)) {
						return true;
					}
				}
				return false;
			}
			public boolean isWordPart(char c) {
				for (int i = 0; i < colored_keywords.length; i++) {
					if (colored_keywords[i].indexOf(c)!=-1) {
						return true;
					}
				}
				return false;
			}
		});
		for (int i = 0; i < colored_keywords.length; i++) {
			keywordRule.addWord(colored_keywords[i], new Token(ta4Keyword));
		}
		rules.add(keywordRule);
		
		
		
		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);
	}
}
