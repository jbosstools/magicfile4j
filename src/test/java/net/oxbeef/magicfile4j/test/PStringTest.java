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
package net.oxbeef.magicfile4j.test;

import java.io.IOException;

import net.oxbeef.magicfile4j.offset.util.StringUtils;

public class PStringTest extends AbstractMagicTest {
	
	public void testPString_Default() throws IOException {
		String magicFile = "0 pstring x match";
		byte[] objpart = StringUtils.hex2Bytes("05");
		byte[] str = StringUtils.string2ASCII("RobHere");
		byte[] toTest = StringUtils.combine(new byte[][]{objpart, str});

		runMagicAndFileTestOp(magicFile, toTest, "Test: match");
		
		objpart = StringUtils.hex2Bytes("77aa83475234");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}

	
	public void testPString_B() throws IOException {
		String magicFile = "0 pstring/B x match";
		byte[] objpart = StringUtils.hex2Bytes("05");
		byte[] str = StringUtils.string2ASCII("RobHere");
		byte[] toTest = StringUtils.combine(new byte[][]{objpart, str});

		runMagicAndFileTestOp(magicFile, toTest, "Test: match");
		
		objpart = StringUtils.hex2Bytes("77aa83475234");
		runMagicAndFileTestOpNullMatch(magicFile, objpart);
	}

	public void testPString_H() throws IOException {
		// succeed on big endian
		String magicFile = "0 pstring/H x match";
		byte[] objpart = StringUtils.hex2Bytes("0005");
		byte[] str = StringUtils.string2ASCII("RobHere");
		byte[] toTest = StringUtils.combine(new byte[][]{objpart, str});

		runMagicAndFileTestOp(magicFile, toTest, "Test: match");
		
		// fail on little-endian
		objpart = StringUtils.hex2Bytes("0500");
		str = StringUtils.string2ASCII("RobHere");
		toTest = StringUtils.combine(new byte[][]{objpart, str});
		runMagicAndFileTestOpNullMatch(magicFile, toTest);
	}

	public void testPString_h() throws IOException {
		// succeed on little endian
		String magicFile = "0 pstring/h x match";
		byte[] objpart = StringUtils.hex2Bytes("0500");
		byte[] str = StringUtils.string2ASCII("RobHere");
		byte[] toTest = StringUtils.combine(new byte[][]{objpart, str});

		runMagicAndFileTestOp(magicFile, toTest, "Test: match");
		
		// fail on big-endian
		objpart = StringUtils.hex2Bytes("0005");
		str = StringUtils.string2ASCII("RobHere");
		toTest = StringUtils.combine(new byte[][]{objpart, str});
		runMagicAndFileTestOpNullMatch(magicFile, toTest);
	}


	public void testPString_L() throws IOException {
		// succeed on big endian
		String magicFile = "0 pstring/L x match";
		byte[] objpart = StringUtils.hex2Bytes("00000005");
		byte[] str = StringUtils.string2ASCII("RobHere");
		byte[] toTest = StringUtils.combine(new byte[][]{objpart, str});

		runMagicAndFileTestOp(magicFile, toTest, "Test: match");
		
		// fail on little-endian
		objpart = StringUtils.hex2Bytes("05000000");
		str = StringUtils.string2ASCII("RobHere");
		toTest = StringUtils.combine(new byte[][]{objpart, str});
		runMagicAndFileTestOpNullMatch(magicFile, toTest);
	}

	public void testPString_l() throws IOException {
		// succeed on little endian
		String magicFile = "0 pstring/l x match";
		byte[] objpart = StringUtils.hex2Bytes("05000000");
		byte[] str = StringUtils.string2ASCII("RobHere");
		byte[] toTest = StringUtils.combine(new byte[][]{objpart, str});

		runMagicAndFileTestOp(magicFile, toTest, "Test: match");
		
		// fail on big-endian
		objpart = StringUtils.hex2Bytes("00000005");
		str = StringUtils.string2ASCII("RobHere");
		toTest = StringUtils.combine(new byte[][]{objpart, str});
		runMagicAndFileTestOpNullMatch(magicFile, toTest);
	}

}
