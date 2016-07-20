/*
 * Copyright (c) 2016 Red Hat, Inc., Rob Stryker, and Contributors
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
package org.jboss.tools.magicfile4j.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jboss.tools.magicfile4j.IMagicFileModel;
import org.jboss.tools.magicfile4j.MagicFileLoader;
import org.jboss.tools.magicfile4j.MagicResult;
import org.jboss.tools.magicfile4j.MagicRunner;

import junit.framework.TestCase;

public abstract class AbstractMagicTest extends TestCase {

	protected void runMagicAndFileTestOp(
			String magicFileContents, byte[] fileToTest,
			String expectedResult) throws IOException {
		IMagicFileModel mfm = new MagicFileLoader().readMagicFile(
				new ByteArrayInputStream(magicFileContents.getBytes()));
		MagicRunner mrunner = null;
		mrunner = new MagicRunner("Test", fileToTest);
		MagicResult mr = mrunner.runMatcher(mfm);
		String out = mr.getOutput();
		assertEquals(expectedResult, out);
	}
	
	protected void runMagicAndFileTestOpNullMatch(
			String magicFileContents, byte[] fileToTest) throws IOException {
		IMagicFileModel mfm = new MagicFileLoader().readMagicFile(
				new ByteArrayInputStream(magicFileContents.getBytes()));
		MagicRunner mrunner = null;
		mrunner = new MagicRunner("Test", fileToTest);
		MagicResult mr = mrunner.runMatcher(mfm);
		assertNull(mr);
	}
}
