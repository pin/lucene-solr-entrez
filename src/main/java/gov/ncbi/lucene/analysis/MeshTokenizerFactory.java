package gov.ncbi.lucene.analysis;

import java.io.Reader;
import java.util.Map;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeSource.AttributeFactory;

public class MeshTokenizerFactory extends TokenizerFactory {
	private final MeshTokenizer.Mode mode;
	
	public MeshTokenizerFactory(Map<String, String> args) {
		super(args);
		String mode = require(args, "mode");
		if (mode.equalsIgnoreCase("heading")) {
			this.mode = MeshTokenizer.Mode.HEADING;
		}
		else if (mode.equalsIgnoreCase("subheading")) {
			this.mode = MeshTokenizer.Mode.SUBHEADING;
		}
		else {
			throw new IllegalArgumentException("invalid mode: " + mode);
		}
	}

	@Override
	public Tokenizer create(AttributeFactory factory, Reader input) {
		return new MeshTokenizer(factory, input, mode);
	}
}
