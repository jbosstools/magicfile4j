/*
 * Copyright Rob Stryker and Contributors (as per source history)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.tools.magicfile4j.internal.model;

import java.nio.ByteOrder;
import java.util.HashMap;

import org.jboss.tools.magicfile4j.internal.endian.Endian;
import org.jboss.tools.magicfile4j.internal.model.matcher.ByteTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.ClearTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.DateTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.DefaultTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.DoubleTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.FloatTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.ID3Test;
import org.jboss.tools.magicfile4j.internal.model.matcher.IndirectTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.NameTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.NumericTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.PstringTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.QuadTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.QwdateTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.RegexTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.SearchTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.ShortTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.String16Test;
import org.jboss.tools.magicfile4j.internal.model.matcher.StringTest;
import org.jboss.tools.magicfile4j.internal.model.matcher.Tester;
import org.jboss.tools.magicfile4j.internal.model.matcher.UseTest;

public class DataTypeMatcherMap implements IDataTypes {
	private static DataTypeMatcherMap instance = new DataTypeMatcherMap();
	public static DataTypeMatcherMap getInstance() {
		return instance;
	}
	
	private Tester byteTester;
	private Tester shortTester;
	private Tester longTester;
	private Tester quadTester;
	private Tester floatTester;
	private Tester doubleTester;
	private Tester stringTester;
	private Tester pstringTester;
	private Tester dateTester;
	private Tester qdateTester;
	private Tester ldateTester;
	private Tester qldateTester;
	private Tester qwdateTester;
	private Tester beid3Tester;
	private Tester beshortTester;
	private Tester belongTester;
	private Tester bequadTester;
	private Tester befloatTester;
	private Tester bedoubleTester;
	private Tester bedateTester;
	private Tester beqdateTester;
	private Tester beldateTester;
	private Tester beqldateTester;
	private Tester beqwdateTester;
	private Tester bestring16Tester;
	private Tester leid3Tester;
	private Tester leshortTester;
	private Tester lelongTester;
	private Tester lequadTester;
	private Tester lefloatTester;
	private Tester ledoubleTester;
	private Tester ledateTester;
	private Tester leqdateTester;
	private Tester leldateTester;
	private Tester leqldateTester;
	private Tester leqwdateTester;
	private Tester lestring16Tester;
	private Tester melongTester;
	private Tester medateTester;
	private Tester meldateTester;
	private Tester indirectTester;
	private Tester nameTester;
	private Tester useTester;
	private Tester regexTester;
	private Tester searchTester;
	private Tester defaultTester;
	private Tester clearTester;
	
	
	
	private final HashMap<String, Tester> testMap;
	public DataTypeMatcherMap() {
		
		byteTester = new ByteTest();
		
		shortTester = new ShortTest(Endian.NATIVE);
		beshortTester = new ShortTest(Endian.BIG);
		leshortTester = new ShortTest(Endian.LITTLE);

		floatTester = new FloatTest(Endian.NATIVE);
		befloatTester = new FloatTest(Endian.BIG);
		lefloatTester = new FloatTest(Endian.LITTLE);
		
		longTester = new NumericTest(4, Endian.NATIVE);
		belongTester = new NumericTest(4, Endian.BIG);
		lelongTester = new NumericTest(4, Endian.LITTLE);

		doubleTester = new DoubleTest(Endian.NATIVE);
		bedoubleTester = new DoubleTest(Endian.BIG);
		ledoubleTester = new DoubleTest(Endian.LITTLE);

		stringTester = new StringTest(); // partially complete, doesn't support flags
		pstringTester = new PstringTest();

		quadTester = new QuadTest(Endian.NATIVE);
		bequadTester = new QuadTest(Endian.BIG);
		lequadTester = new QuadTest(Endian.LITTLE);
		
		
		nameTester = new NameTest();
		useTester = new UseTest();
		
		dateTester = new DateTest(4, Endian.NATIVE, null);
		bedateTester = new DateTest(4, Endian.BIG, null);
		ledateTester = new DateTest(4, Endian.LITTLE, null);
		
		qdateTester = new DateTest(8, Endian.NATIVE, null);
		beqdateTester = new DateTest(8, Endian.BIG, null);
		leqdateTester = new DateTest(8, Endian.LITTLE, null);
		
		ldateTester = new DateTest(4, Endian.NATIVE, DateTest.UTC_TIME_ZONE);
		beldateTester = new DateTest(4, Endian.BIG, DateTest.UTC_TIME_ZONE);
		leldateTester = new DateTest(4, Endian.LITTLE, DateTest.UTC_TIME_ZONE);
		
		qldateTester = new DateTest(8, Endian.NATIVE,DateTest.UTC_TIME_ZONE);
		beqldateTester = new DateTest(8, Endian.BIG,DateTest.UTC_TIME_ZONE);
		leqldateTester = new DateTest(8, Endian.LITTLE,DateTest.UTC_TIME_ZONE);
		
		leid3Tester = new ID3Test(Endian.LITTLE);
		beid3Tester = new ID3Test(Endian.BIG);
		
		bestring16Tester = new String16Test(Endian.BIG);
		lestring16Tester = new String16Test(Endian.LITTLE);
		
		melongTester = new NumericTest(4, Endian.MIDDLE);
		medateTester = new DateTest(4, Endian.MIDDLE, null);
		meldateTester = new DateTest(4, Endian.MIDDLE, DateTest.UTC_TIME_ZONE);

		
		// next to do 
		regexTester = new RegexTest();
		searchTester = new SearchTest();

		
		
		// Not yet done, no idea wtf this is
		qwdateTester = new QwdateTest(Endian.NATIVE);
		beqwdateTester = new QwdateTest(Endian.BIG);
		leqwdateTester = new QwdateTest(Endian.LITTLE);


		// Different types requiring special behavior
		indirectTester = new IndirectTest();
		defaultTester = new DefaultTest(); 
		clearTester = new ClearTest();

		
		testMap  = new HashMap<String, Tester>();
		testMap.put(MAGIC_BYTE, byteTester);
		testMap.put(MAGIC_SHORT, shortTester);
		testMap.put(MAGIC_LONG, longTester);
		testMap.put(MAGIC_QUAD, quadTester);
		testMap.put(MAGIC_FLOAT, floatTester);
		testMap.put(MAGIC_DOUBLE, doubleTester);
		testMap.put(MAGIC_STRING, stringTester);
		testMap.put(MAGIC_PSTRING, pstringTester);
		testMap.put(MAGIC_DATE, dateTester);
		testMap.put(MAGIC_QDATE, qdateTester);
		testMap.put(MAGIC_LDATE, ldateTester);
		testMap.put(MAGIC_QLDATE, qldateTester);
		testMap.put(MAGIC_QWDATE, qwdateTester);
		testMap.put(MAGIC_BEID3, beid3Tester);
		testMap.put(MAGIC_BESHORT, beshortTester);
		testMap.put(MAGIC_BELONG, belongTester);
		testMap.put(MAGIC_BEQUAD, bequadTester);
		testMap.put(MAGIC_BEFLOAT, befloatTester);
		testMap.put(MAGIC_BEDOUBLE, bedoubleTester);
		testMap.put(MAGIC_BEDATE, bedateTester);
		testMap.put(MAGIC_BEQDATE, beqdateTester);
		testMap.put(MAGIC_BELDATE, beldateTester);
		testMap.put(MAGIC_BEQLDATE, beqldateTester);
		testMap.put(MAGIC_BEQWDATE, beqwdateTester);
		testMap.put(MAGIC_BESTRING16, bestring16Tester);
		testMap.put(MAGIC_LEID3, leid3Tester);
		testMap.put(MAGIC_LESHORT, leshortTester);
		testMap.put(MAGIC_LELONG, lelongTester);
		testMap.put(MAGIC_LEQUAD, lequadTester);
		testMap.put(MAGIC_LEFLOAT, lefloatTester);
		testMap.put(MAGIC_LEDOUBLE, ledoubleTester);
		testMap.put(MAGIC_LEDATE, ledateTester);
		testMap.put(MAGIC_LEQDATE, leqdateTester);
		testMap.put(MAGIC_LELDATE, leldateTester);
		testMap.put(MAGIC_LEQLDATE, leqldateTester);
		testMap.put(MAGIC_LEQWDATE, leqwdateTester);
		testMap.put(MAGIC_LESTRING16, lestring16Tester);
		testMap.put(MAGIC_MELONG, melongTester);
		testMap.put(MAGIC_MEDATE, medateTester);
		testMap.put(MAGIC_MELDATE, meldateTester);
		testMap.put(MAGIC_INDIRECT, indirectTester);
		testMap.put(MAGIC_NAME, nameTester);
		testMap.put(MAGIC_USE, useTester);
		testMap.put(MAGIC_REGEX, regexTester);
		testMap.put(MAGIC_SEARCH, searchTester);
		testMap.put(MAGIC_DEFAULT, defaultTester);
		testMap.put(MAGIC_CLEAR, clearTester);
	}
	
	public Tester getTester(TestableNode node) {
		return getTester(node.getType(), ((TestableNode)node).shouldInvertEndian());
	}

	
	public Tester getTester(String type, boolean flip) {
		Tester ret = null;
		String id = getTesterNameInternal(type);
		if( flip ) {
			String idFlipped = inverted(id);
			ret = getTester(idFlipped);
		}
		if( ret == null ) {
			ret = getTester(id);
		}
		return ret;
	}
	
	private String inverted(String t) {
		// crappy hacks
		if( t.startsWith("be")) {
			return "le" + t.substring(2);
		}
		if( t.startsWith("le")) {
			return "be" + t.substring(2);
		} else if( !t.startsWith("me")){
			// we're inverting a  'long'  or 'short' or 'byte' type
			ByteOrder nativebo = ByteOrder.nativeOrder();
			if( nativebo == ByteOrder.BIG_ENDIAN) {
				return "le" + t;
			} else {
				return "be" + t;
			}
		}
		return t;
	}
	
	public Tester getTester(String type) {
		String id = getTesterNameInternal(type);
		return testMap.get(id);
	}

	private String getTesterNameInternal(String type) {
		boolean unsigned = false;
		int nameBeginInd = -1;
		int nameEndInd = -1;
		String name = null;
		for( int i = 0; i < type.length() && name == null; i++ ) {
			char c = type.charAt(i);
			if( i == 0 && c == 'u' && !type.startsWith("use")) {
				unsigned = true;
			} else if( nameBeginInd == -1 ){
				nameBeginInd = i;
			} else {
				switch(c) {
				case '+':
				case '-':
				case '*':
				case '/':
				case '%':
				case '|':
				case '^':
				case '&':
					nameEndInd = i;
					name = type.substring(nameBeginInd, nameEndInd);
				}
			}
		}
		if( name == null && nameBeginInd != -1 ) {
			name = type.substring(nameBeginInd);
		}
		return name;
	}
}
