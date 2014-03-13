package gov.ncbi.lucene.analysis;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class MeshTokenizer extends Tokenizer {
	public final static char SEPARATOR = '/';
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private boolean done;
	private final StringBuffer sb = new StringBuffer();
	public enum Mode {
		HEADING, SUBHEADING
	}
	private final Mode mode;

	protected MeshTokenizer(AttributeFactory factory, Reader input, Mode mode) {
		super(factory, input);
		this.mode = mode;
	}
	
	/* Splits input by SEPARATOR, takes first part in HEADING mode and 
	 * the rest in SUBHEADING mode
	 */
	@Override
	public boolean incrementToken() throws IOException {
		if (done) return false;
		clearAttributes();
		done = true;
		char[] buffer = new char[64];
		int length;
		int separatorIndex = -1;
		while ((length = input.read(buffer)) > 0) {
			if (separatorIndex < 0) {
				for (int i = 0; i < length; i++) {
					if (buffer[i] == SEPARATOR) {
						separatorIndex = i;
						break;
					}
				}
				if (separatorIndex >= 0) {
					if (mode == Mode.HEADING && separatorIndex > 1) {
						sb.append(buffer, 0, separatorIndex);
					}
					else if (mode == Mode.SUBHEADING && separatorIndex < length - 1) {
						sb.append(buffer, separatorIndex + 1, length - separatorIndex - 1);
					}
					continue;
				}
			}
			if ((mode == Mode.HEADING && separatorIndex < 0)
					|| (mode == Mode.SUBHEADING && separatorIndex >= 0)) {
		        sb.append(buffer, 0, length);
			}
		}
		if (sb.length() > 0) {
			termAtt.append(sb.toString());
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void reset() throws IOException {
		super.reset();
		done = false;
		sb.setLength(0);
	}
}
