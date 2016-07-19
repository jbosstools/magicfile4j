package net.oxbeef.magicfile4j.test;

import java.io.IOException;

import net.oxbeef.magicfile4j.internal.offset.StringUtils;

public class RegexTesterTest extends AbstractMagicTest {
	public void testFailRegexString() throws IOException {
		String magicFile = "0 regex ^[a-zA-Z]{2,5}  match\n";
		byte[] filler = StringUtils.string2ASCII("A man was here");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		runMagicAndFileTestOpNullMatch(magicFile, fileToTest);
	}
	
	public void testSimpleString2() throws IOException {
		String magicFile = "0 regex ^[a-zA-Z]{2,5}  match\n";
		byte[] filler = StringUtils.string2ASCII("Amanwashere");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		String expectedResults = "Test: match";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}

	public void testSimpleString3() throws IOException {
		String magicFile = "0 regex ^[a-zA-Z]{2,5}  match %s\n";
		byte[] filler = StringUtils.string2ASCII("Amanwashere");
		byte[] objpart = StringUtils.hex2Bytes("00");
		byte[] fileToTest = StringUtils.combine(new byte[][] { filler, objpart});
		String expectedResults = "Test: match Amanw";
		runMagicAndFileTestOp(magicFile, fileToTest, expectedResults);
	}
}
