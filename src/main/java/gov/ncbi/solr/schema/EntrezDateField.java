package gov.ncbi.solr.schema;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.util.DateUtil;
import org.apache.solr.schema.DateField;
import org.apache.solr.schema.DateValueFieldType;
import org.apache.solr.util.DateMathParser;

@SuppressWarnings("deprecation")
public class EntrezDateField extends DateField implements DateValueFieldType {
	public static final Collection<String> DATE_FORMATS = new ArrayList<String>();

	static {
		DATE_FORMATS.add("yyyy-MM-dd'T'HH:mm:ss'Z'");
		DATE_FORMATS.add("yyyy-MM-dd'T'HH:mm:ss");
		DATE_FORMATS.add("yyyy-MM-dd");
		DATE_FORMATS.add("yyyy-MM-dd hh:mm:ss");
		DATE_FORMATS.add("yyyy-MM-dd HH:mm:ss");
		DATE_FORMATS.add("yyyy MMM dd"); // 1991 Jun 15
		DATE_FORMATS.add("yyyy MMM");
		DATE_FORMATS.add("yyyy/MM/dd");
		DATE_FORMATS.add("yyyy/MM");
		DATE_FORMATS.add("yyyy");
	}

	public Date parseDate2(String s) throws ParseException {
		return DateUtil.parseDate(s, DATE_FORMATS);
	}

	public Date parseMath(Date now, String val) {
		if (val.length() == 4) {
			
		}
		String math = null;
		final DateMathParser p = new DateMathParser();

		if (null != now)
			p.setNow(now);

		if (val.startsWith(NOW)) {
			math = val.substring(NOW.length());
		} else {
			final int zz = val.indexOf(Z);
			if (0 < zz) {
				math = val.substring(zz + 1);
				try {
					// p.setNow(toObject(val.substring(0,zz)));
					p.setNow(parseDate(val.substring(0, zz + 1)));
				} catch (ParseException e) {
					throw new SolrException(
							SolrException.ErrorCode.BAD_REQUEST,
							"Invalid Date in Date Math String:'" + val + '\'',
							e);
				}
			} else {
				try {
					Date d = parseDate2(val);
					p.setNow(d);
				}
				catch (ParseException e) {
					throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
							"Invalid Date String:'" + val + '\'', e);
				}
			}
		}

		if (null == math || math.equals("")) {
			return p.getNow();
		}

		try {
			return p.parseMath(math);
		} catch (ParseException e) {
			throw new SolrException(SolrException.ErrorCode.BAD_REQUEST,
					"Invalid Date Math String:'" + val + '\'', e);
		}
	}
}
