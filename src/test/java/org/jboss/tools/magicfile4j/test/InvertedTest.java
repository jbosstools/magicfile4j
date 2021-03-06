package org.jboss.tools.magicfile4j.test;

import java.nio.ByteOrder;

import org.jboss.tools.magicfile4j.internal.model.DataTypeMatcherMap;
import org.jboss.tools.magicfile4j.internal.model.IDataTypes;
import org.jboss.tools.magicfile4j.internal.model.matcher.Tester;

import junit.framework.TestCase;

public class InvertedTest extends TestCase implements IDataTypes {

	public static final String[] types = new String[] {
			MAGIC_BYTE,		MAGIC_SHORT,	MAGIC_LONG, 	MAGIC_QUAD,	MAGIC_FLOAT,
			MAGIC_DOUBLE,	MAGIC_STRING,	MAGIC_PSTRING,	MAGIC_DATE,	
			MAGIC_QDATE,	MAGIC_LDATE,	MAGIC_QLDATE,	MAGIC_QWDATE,	
			MAGIC_BEID3,	MAGIC_BESHORT,	MAGIC_BELONG,	MAGIC_BEQUAD,	
			MAGIC_BEFLOAT,	MAGIC_BEDOUBLE,	MAGIC_BEDATE,	MAGIC_BEQDATE,	
			MAGIC_BELDATE,	MAGIC_BEQLDATE,	MAGIC_BEQWDATE,	MAGIC_BESTRING16,	
			MAGIC_LEID3,	MAGIC_LESHORT,	MAGIC_LELONG,	MAGIC_LEQUAD,	
			MAGIC_LEFLOAT,	MAGIC_LEDOUBLE,	MAGIC_LEDATE,	MAGIC_LEQDATE,	
			MAGIC_LELDATE,	MAGIC_LEQLDATE,	MAGIC_LEQWDATE,	MAGIC_LESTRING16,	
			MAGIC_MELONG,	MAGIC_MEDATE,	MAGIC_MELDATE,	MAGIC_INDIRECT,	
			MAGIC_NAME,	MAGIC_USE,	MAGIC_REGEX,	MAGIC_SEARCH,	MAGIC_DEFAULT,	MAGIC_CLEAR
	};
	
	// when the native is big-endian, these are the opposites
	public static final String[] invertedNativeBig = new String[] {
			MAGIC_BYTE,		MAGIC_LESHORT,	MAGIC_LELONG, 	MAGIC_LEQUAD,	MAGIC_LEFLOAT,
			MAGIC_LEDOUBLE,	MAGIC_STRING,	MAGIC_PSTRING,	MAGIC_LEDATE,	
			MAGIC_LEQDATE,	MAGIC_LELDATE,	MAGIC_LEQLDATE,	MAGIC_LEQWDATE,	
			MAGIC_LEID3,	MAGIC_LESHORT,	MAGIC_LELONG,	MAGIC_LEQUAD,	
			MAGIC_LEFLOAT,	MAGIC_LEDOUBLE,	MAGIC_LEDATE,	MAGIC_LEQDATE,	
			MAGIC_LELDATE,	MAGIC_LEQLDATE,	MAGIC_LEQWDATE,	MAGIC_LESTRING16,	
			MAGIC_BEID3,	MAGIC_BESHORT,	MAGIC_BELONG,	MAGIC_BEQUAD,	
			MAGIC_BEFLOAT,	MAGIC_BEDOUBLE,	MAGIC_BEDATE,	MAGIC_BEQDATE,	
			MAGIC_BELDATE,	MAGIC_BEQLDATE,	MAGIC_BEQWDATE,	MAGIC_BESTRING16,	
			MAGIC_MELONG,	MAGIC_MEDATE,	MAGIC_MELDATE,	MAGIC_INDIRECT,	
			MAGIC_NAME,	MAGIC_USE,	MAGIC_REGEX,	MAGIC_SEARCH,	MAGIC_DEFAULT,	MAGIC_CLEAR
	};
	
	// when the native is little-endian, these are the opposites
	public static final String[] invertedNativeLittle = new String[] {
			MAGIC_BYTE,		MAGIC_BESHORT,	MAGIC_BELONG, 	MAGIC_BEQUAD,	MAGIC_BEFLOAT,
			MAGIC_BEDOUBLE,	MAGIC_STRING,	MAGIC_PSTRING,	MAGIC_BEDATE,	
			MAGIC_BEQDATE,	MAGIC_BELDATE,	MAGIC_BEQLDATE,	MAGIC_BEQWDATE,	
			MAGIC_LEID3,	MAGIC_LESHORT,	MAGIC_LELONG,	MAGIC_LEQUAD,	
			MAGIC_LEFLOAT,	MAGIC_LEDOUBLE,	MAGIC_LEDATE,	MAGIC_LEQDATE,	
			MAGIC_LELDATE,	MAGIC_LEQLDATE,	MAGIC_LEQWDATE,	MAGIC_LESTRING16,	
			MAGIC_BEID3,	MAGIC_BESHORT,	MAGIC_BELONG,	MAGIC_BEQUAD,	
			MAGIC_BEFLOAT,	MAGIC_BEDOUBLE,	MAGIC_BEDATE,	MAGIC_BEQDATE,	
			MAGIC_BELDATE,	MAGIC_BEQLDATE,	MAGIC_BEQWDATE,	MAGIC_BESTRING16,	
			MAGIC_MELONG,	MAGIC_MEDATE,	MAGIC_MELDATE,	MAGIC_INDIRECT,	
			MAGIC_NAME,	MAGIC_USE,	MAGIC_REGEX,	MAGIC_SEARCH,	MAGIC_DEFAULT,	MAGIC_CLEAR
	};

	public void testFindInvertedTester() {
		for( int i = 0; i < types.length; i++ ) {
			Tester original = DataTypeMatcherMap.getInstance().getTester(types[i]);
			String[] expectedArr = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? invertedNativeBig : invertedNativeLittle);
			Tester expected = DataTypeMatcherMap.getInstance().getTester(expectedArr[i]);
			Tester found = DataTypeMatcherMap.getInstance().getTester(types[i], true);
			assertEquals("Failed finding inversion for " + types[i], expected, found);
		}
	}
	
}
