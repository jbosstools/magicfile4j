package org.jboss.tools.magicfile4j.test;

import java.io.IOException;

import org.jboss.tools.magicfile4j.internal.offset.StringUtils;

public class StringTesterTest extends AbstractMagicTest {
	public void testSimpleString() throws IOException {
		String magicFile = "0 string TestRob  match\n";
		byte[] filler = StringUtils.string2ASCII("TestRob");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		String expectedResults = "Test: match";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}
	
	public void testSimpleString2() throws IOException {
		String magicFile = "0 string Test\\ Rob  match\n";
		byte[] filler = StringUtils.string2ASCII("Test Rob");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		String expectedResults = "Test: match";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	public void testSimpleString3() throws IOException {
		String magicFile = "0 string Test\\ \\ Rob  match\n";
		byte[] filler = StringUtils.string2ASCII("Test  Rob");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		String expectedResults = "Test: match";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	public void testBigInsensitive1() throws IOException {
		String magicFile = "0 string/C TEST\\ ROB  match\n";
		byte[] filler = StringUtils.string2ASCII("Test Rob");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		String expectedResults = "Test: match";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
		
		
		magicFile = "0 string/C test\\ ROB  match\n";
		runMagicAndFileTestOpNullMatch(magicFile, fileToTest);
		
	}

	public void testLittleInsensitive1() throws IOException {
		String magicFile = "0 string/c TEst\\ Rob  match\n";
		byte[] filler = StringUtils.string2ASCII("TEST ROB");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		String expectedResults = "Test: match";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
		
		
		magicFile = "0 string/C test\\ ROB  match\n";
		runMagicAndFileTestOpNullMatch(magicFile, fileToTest);
		
	}

	public void testOptionalBlank() throws IOException {
		String magicFile = "0 string/w test\\ \\ \\ r\\ ob  match\n";
		byte[] filler = StringUtils.string2ASCII("testrob");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		String expectedResults = "Test: match";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);		
	}

	public void testCompressBlank() throws IOException {
		String magicFile = "0 string/W test\\ rob  match %s\n";
		byte[] filler = StringUtils.string2ASCII("test    rob");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		String expectedResults = "Test: match test rob";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);		
	}

	
	
}
