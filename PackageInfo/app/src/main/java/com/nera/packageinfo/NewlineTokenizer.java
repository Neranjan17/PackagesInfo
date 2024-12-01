package com.nera.packageinfo;

import android.widget.MultiAutoCompleteTextView;


public class NewlineTokenizer implements MultiAutoCompleteTextView.Tokenizer {
	@Override
	public int findTokenStart(CharSequence text, int cursor) {
		int i = cursor;
		while (i > 0 && text.charAt(i - 1) != '\n') {
			i--;
		}
		return i;
	}

	@Override
	public int findTokenEnd(CharSequence text, int cursor) {
		int i = cursor;
		int len = text.length();
		while (i < len) {
			if (text.charAt(i) == '\n') {
				return i;
			} else {
				i++;
			}
		}
		return len;
	}

	@Override
	public CharSequence terminateToken(CharSequence text) {
		int i = text.length();
		while (i > 0 && text.charAt(i - 1) == ' ') {
			i--;
		}
		if (i > 0 && text.charAt(i - 1) == '\n') {
			return text;
		}
		// Add newline character at the end
		String result = text + "\n";
		return result;
	}
}