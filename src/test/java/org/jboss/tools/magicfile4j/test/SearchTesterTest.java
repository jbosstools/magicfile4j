package org.jboss.tools.magicfile4j.test;

import java.io.IOException;

import org.jboss.tools.magicfile4j.internal.offset.StringUtils;

public class SearchTesterTest extends AbstractMagicTest {
	public void testFailSearch() throws IOException {
		String magicFile = "0 search TestRob  match\n";
		byte[] filler = StringUtils.string2ASCII("TestRob");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		runMagicAndFileTestOpNullMatch(magicFile, fileToTest);
	}
	
	public void testSimpleString() throws IOException {
		String magicFile = "0 search/10 TestRob  match\n";
		byte[] filler = StringUtils.string2ASCII("There is TestRob");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		String expectedResults = "Test: match";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}	

	public void testSimpleString1() throws IOException {
		String magicFile = "0 search/10/Cc TestRob  match %s\n";
		byte[] filler = StringUtils.string2ASCII("There is testrob");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		String expectedResults = "Test: match testrob";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}	

}
