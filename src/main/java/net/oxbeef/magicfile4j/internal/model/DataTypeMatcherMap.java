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
package net.oxbeef.magicfile4j.internal.model;

import java.nio.ByteOrder;
import java.util.HashMap;

import net.oxbeef.magicfile4j.internal.model.matcher.NumericTest;
import net.oxbeef.magicfile4j.internal.model.matcher.Tester;
import net.oxbeef.magicfile4j.internal.model.matcher.id3Test;
import net.oxbeef.magicfile4j.internal.model.matcher.beqwdateTest;
import net.oxbeef.magicfile4j.internal.model.matcher.bestring16Test;
import net.oxbeef.magicfile4j.internal.model.matcher.byteTest;
import net.oxbeef.magicfile4j.internal.model.matcher.clearTest;
import net.oxbeef.magicfile4j.internal.model.matcher.dateTest;
import net.oxbeef.magicfile4j.internal.model.matcher.defaultTest;
import net.oxbeef.magicfile4j.internal.model.matcher.doubleTest;
import net.oxbeef.magicfile4j.internal.model.matcher.floatTest;
import net.oxbeef.magicfile4j.internal.model.matcher.indirectTest;
import net.oxbeef.magicfile4j.internal.model.matcher.leid3Test;
import net.oxbeef.magicfile4j.internal.model.matcher.leqwdateTest;
import net.oxbeef.magicfile4j.internal.model.matcher.lestring16Test;
import net.oxbeef.magicfile4j.internal.model.matcher.medateTest;
import net.oxbeef.magicfile4j.internal.model.matcher.meldateTest;
import net.oxbeef.magicfile4j.internal.model.matcher.melongTest;
import net.oxbeef.magicfile4j.internal.model.matcher.nameTest;
import net.oxbeef.magicfile4j.internal.model.matcher.pstringTest;
import net.oxbeef.magicfile4j.internal.model.matcher.quadTest;
import net.oxbeef.magicfile4j.internal.model.matcher.qwdateTest;
import net.oxbeef.magicfile4j.internal.model.matcher.regexTest;
import net.oxbeef.magicfile4j.internal.model.matcher.searchTest;
import net.oxbeef.magicfile4j.internal.model.matcher.shortTest;
import net.oxbeef.magicfile4j.internal.model.matcher.stringTest;
import net.oxbeef.magicfile4j.internal.model.matcher.useTest;

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
		
		byteTester = new byteTest();
		
		shortTester = new shortTest(ByteOrder.nativeOrder());
		beshortTester = new shortTest(ByteOrder.BIG_ENDIAN);
		leshortTester = new shortTest(ByteOrder.LITTLE_ENDIAN);

		floatTester = new floatTest(ByteOrder.nativeOrder());
		befloatTester = new floatTest(ByteOrder.BIG_ENDIAN);
		lefloatTester = new floatTest(ByteOrder.LITTLE_ENDIAN);
		
		longTester = new NumericTest(4, ByteOrder.nativeOrder());
		belongTester = new NumericTest(4, ByteOrder.BIG_ENDIAN);
		lelongTester = new NumericTest(4, ByteOrder.LITTLE_ENDIAN);

		doubleTester = new doubleTest(ByteOrder.nativeOrder());
		bedoubleTester = new doubleTest(ByteOrder.BIG_ENDIAN);
		ledoubleTester = new doubleTest(ByteOrder.LITTLE_ENDIAN);

		stringTester = new stringTest(); // partially complete, doesn't support flags
		pstringTester = new pstringTest();

		quadTester = new quadTest(ByteOrder.nativeOrder());
		bequadTester = new quadTest(ByteOrder.BIG_ENDIAN);
		lequadTester = new quadTest(ByteOrder.LITTLE_ENDIAN);
		
		
		nameTester = new nameTest();
		useTester = new useTest();
		
		// Not yet done
				
		
		dateTester = new dateTest(4, ByteOrder.nativeOrder(), null);
		bedateTester = new dateTest(4, ByteOrder.BIG_ENDIAN, null);
		ledateTester = new dateTest(4, ByteOrder.LITTLE_ENDIAN, null);
		
		qdateTester = new dateTest(8, ByteOrder.nativeOrder(), null);
		beqdateTester = new dateTest(8, ByteOrder.BIG_ENDIAN, null);
		leqdateTester = new dateTest(8, ByteOrder.LITTLE_ENDIAN, null);
		
		ldateTester = new dateTest(4, ByteOrder.nativeOrder(), dateTest.UTC_TIME_ZONE);
		beldateTester = new dateTest(4, ByteOrder.BIG_ENDIAN, dateTest.UTC_TIME_ZONE);
		leldateTester = new dateTest(4, ByteOrder.LITTLE_ENDIAN, dateTest.UTC_TIME_ZONE);
		
		qldateTester = new dateTest(8, ByteOrder.nativeOrder(),dateTest.UTC_TIME_ZONE);
		beqldateTester = new dateTest(8, ByteOrder.BIG_ENDIAN,dateTest.UTC_TIME_ZONE);
		leqldateTester = new dateTest(8, ByteOrder.LITTLE_ENDIAN,dateTest.UTC_TIME_ZONE);
		
		leid3Tester = new id3Test(ByteOrder.LITTLE_ENDIAN);
		beid3Tester = new id3Test(ByteOrder.BIG_ENDIAN);
		
		bestring16Tester = new bestring16Test();
		lestring16Tester = new lestring16Test();
		
		
		qwdateTester = new qwdateTest();
		beqwdateTester = new beqwdateTest();
		leqwdateTester = new leqwdateTest();

		melongTester = new melongTest();
		medateTester = new medateTest();
		meldateTester = new meldateTest();


		// Different types requiring special behavior
		indirectTester = new indirectTest();
		regexTester = new regexTest();
		searchTester = new searchTest();
		defaultTester = new defaultTest(); 
		clearTester = new clearTest();

		
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
		} else {
			return "le" + t;
		}
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