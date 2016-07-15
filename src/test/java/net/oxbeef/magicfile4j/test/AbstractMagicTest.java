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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.TestCase;
import net.oxbeef.magicfile4j.MagicFileLoader;
import net.oxbeef.magicfile4j.MagicResult;
import net.oxbeef.magicfile4j.MagicRunner;
import net.oxbeef.magicfile4j.internal.model.MagicFileModel;

public abstract class AbstractMagicTest extends TestCase {

	protected void runMagicAndFileTestOp(
			String magicFileContents, byte[] fileToTest,
			String expectedResult) throws IOException {
		MagicFileModel mfm = new MagicFileLoader().readMagicFile(
				new ByteArrayInputStream(magicFileContents.getBytes()));
		MagicRunner mrunner = null;
		mrunner = new MagicRunner("Test", fileToTest);
		MagicResult mr = mrunner.runMatcher(mfm);
		String out = mr.getOutput();
		assertEquals(expectedResult, out);
	}
	
	protected void runMagicAndFileTestOpNullMatch(
			String magicFileContents, byte[] fileToTest) throws IOException {
		MagicFileModel mfm = new MagicFileLoader().readMagicFile(
				new ByteArrayInputStream(magicFileContents.getBytes()));
		MagicRunner mrunner = null;
		mrunner = new MagicRunner("Test", fileToTest);
		MagicResult mr = mrunner.runMatcher(mfm);
		assertNull(mr);
	}

}
