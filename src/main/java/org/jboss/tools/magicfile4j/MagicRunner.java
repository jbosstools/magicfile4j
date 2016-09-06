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
package org.jboss.tools.magicfile4j;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jboss.tools.magicfile4j.internal.model.DataTypeMatcherMap;
import org.jboss.tools.magicfile4j.internal.model.Magic;
import org.jboss.tools.magicfile4j.internal.model.MagicFileModel;
import org.jboss.tools.magicfile4j.internal.model.MagicNode;
import org.jboss.tools.magicfile4j.internal.model.NameNode;
import org.jboss.tools.magicfile4j.internal.model.TestableNode;
import org.jboss.tools.magicfile4j.internal.model.matcher.Tester;

public class MagicRunner {

	private MagicFileModel model;
	public MagicRunner(IMagicFileModel model) {
		this.model = (MagicFileModel)model;
	}

	public MagicResult runMatcher(File toTest) throws IOException {
		return runMatcher(toTest, 16383);
	} 
	
	public MagicResult runMatcher(File toTest, int maxSize) throws IOException {
		byte[] bytes = read(toTest, 16383 );
		return runMatcherInternal(bytes, toTest.getName());
	}

	
	private byte[] read(File file, int maxBytes) throws IOException {
		int bytesRead = 0;
	    ByteArrayOutputStream ous = null;
	    InputStream ios = null;
	    try {
	        byte[] buffer = new byte[4096];
	        ous = new ByteArrayOutputStream();
	        ios = new FileInputStream(file);
	        int read = 0;
	        while (((bytesRead < maxBytes) && ((read = ios.read(buffer))) != -1)) {
	        	bytesRead += 4096;
	            ous.write(buffer, 0, read);
	        }
	    }finally {
	        try {
	            if (ous != null)
	                ous.close();
	        } catch (IOException e) {
	        }

	        try {
	            if (ios != null)
	                ios.close();
	        } catch (IOException e) {
	        }
	    }
	    return ous.toByteArray();
	}
	
	
	public MagicResult runMatcher(String label, byte[] contents) {
		return runMatcherInternal(contents, label); 
	}

	private MagicResult runMatcherInternal(byte[] contents, String label) {
		// should sort top level nodes, but not during testing
		MagicNode[] magics = model.getSortedChildren(); 
												
		for (int i = 0; i < magics.length; i++) {
			MagicNode m = magics[i];
			// If we're a named node, we shouldn't be eligible as a top-level result???
			// This is when comparing my results to those of linux 'file' 
			if( !(m instanceof NameNode)) {
				MagicNode matched = findFirstMatchingDescriptionNode(m, contents);
				if (matched != null) {
					// Now we should traverse it to accumulate output, etc
					return handleWinner(matched, label, contents);
				}
			}
		}
		return null;
	}

	/**
	 * Returns the first magic that we match which has a description 
	 * 
	 * @param m
	 * @param contents
	 * @return
	 */
	protected TestableNode findFirstMatchingDescriptionNode(MagicNode mn, byte[] contents) {
		TestableNode node = null;
		if( mn instanceof TestableNode) {
			node = (TestableNode)mn;
		}
		
		Tester tester = getTester(node);
		if( tester == null ) {
			return null;
		}

		// Tester found
		Object result = tester.getValue(node, contents);
		if (result != null && tester.matches(node, contents, result)) {
			if( node.hasOutput()) {
				String nodeOutput = ((Magic)node).getOutput();
				if (nodeOutput != null && !nodeOutput.isEmpty()) {
					// we have a description, and we match, so we're the matching
					// top-level
					return node;
				}
			}
			// We match, but no description. We have to check children.
			MagicNode[] magics = node.getChildren(); 
			for (int i = 0; i < magics.length; i++) {
				MagicNode child = magics[i];
				TestableNode matchedChild = findFirstMatchingDescriptionNode(child, contents); 
				if (matchedChild != null) {
					return matchedChild;
				}
			}
		}
		// we don't match
		return null;
	}

	private MagicResult handleWinner(MagicNode m, String label, byte[] contents) {
		MagicResult result = new MagicResult(m);
		result.appendOutput(label + ":");
		fillResult(m, result, contents);
		return result;
	}
	
	private Tester getTester(TestableNode m) {
		String type = m.getType();
		boolean shouldFlip = m.shouldInvertEndian();
		Tester tester = DataTypeMatcherMap.getInstance().getTester(type, shouldFlip);
		if (tester == null) {
			return null;
		}
		return tester;
	}
	
	private void fillResult(MagicNode node2, MagicResult matchResult, byte[] contents) {
		TestableNode node = null;
		if( node2 instanceof TestableNode) {
			node = (TestableNode)node2;
		}

		Tester tester = getTester(node);
		if( tester == null ) {
			// TODO log?
			return;
		}
		Object dataAtOffset = tester.getValue(node, contents);
		if (dataAtOffset != null && tester.matches(node, contents, dataAtOffset)) {
			matchResult.addMatchedMimeType(node.getLevel(), node.getMimeType());
			matchResult.addMatchingNode(node);
			if( node.hasOutput()) {
				String out = ((Magic)node).getOutput();
				if( out != null && !out.isEmpty()) {
					String toPrint = tester.formatString((Magic)node, out, dataAtOffset);
					if( toPrint.startsWith("\\b")) {
						toPrint = toPrint.substring(2);
					} else {
						toPrint = " " + toPrint;
					}
					matchResult.appendOutput(toPrint);
				}
			}
			MagicNode[] children = node.getChildren();
			for( int i = 0; i < children.length; i++ ) {
				fillResult(children[i], matchResult, contents);
			}
		}
	}
}
