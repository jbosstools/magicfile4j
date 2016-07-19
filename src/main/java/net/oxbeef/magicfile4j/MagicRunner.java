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
package net.oxbeef.magicfile4j;


import java.nio.ByteBuffer;

import net.oxbeef.magicfile4j.internal.model.DataTypeMatcherMap;
import net.oxbeef.magicfile4j.internal.model.Magic;
import net.oxbeef.magicfile4j.internal.model.MagicFileModel;
import net.oxbeef.magicfile4j.internal.model.MagicNode;
import net.oxbeef.magicfile4j.internal.model.NameNode;
import net.oxbeef.magicfile4j.internal.model.TestableNode;
import net.oxbeef.magicfile4j.internal.model.matcher.Tester;
import net.oxbeef.magicfile4j.internal.offset.StringUtils;

public class MagicRunner {

	private String filename;
	private byte[] contents;
	public MagicRunner(String filename, byte[] contents) {
		this.filename = filename;
		this.contents = contents;
	}
	
	public MagicResult runMatcher(IMagicFileModel model2) {
		MagicFileModel model = (MagicFileModel)model2;
		// should sort top level nodes, but not during testing
		MagicNode[] magics = model.getSortedChildren(); 
												
		for (int i = 0; i < magics.length; i++) {
			MagicNode m = magics[i];
			// If we're a named node, we shouldn't be eligible as a top-level result???
			// This is when comparing my results to those of linux 'file' 
			if( !(m instanceof NameNode)) {
				MagicNode matched = findFirstMatchingDescriptionNode(m);
				if (matched != null) {
					// Now we should traverse it to accumulate output, etc
					return handleWinner(matched);
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
	protected TestableNode findFirstMatchingDescriptionNode(MagicNode mn) {
		TestableNode node = null;
		if( mn instanceof TestableNode) {
			node = (TestableNode)mn;
		}
		
		Tester tester = getTester(node);
		if( tester == null ) {
			return null;
		}

		// Tester found
		byte[] result = tester.getValue(node, contents);
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
				TestableNode matchedChild = findFirstMatchingDescriptionNode(child ); 
				if (matchedChild != null) {
					return matchedChild;
				}
			}
		}
		// we don't match
		return null;
	}

	private MagicResult handleWinner(MagicNode m) {
		MagicResult result = new MagicResult(m);
		result.appendOutput(filename + ":");
		fillResult(m, result);
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
	
	private void fillResult(MagicNode node2, MagicResult matchResult) {
		TestableNode node = null;
		if( node2 instanceof TestableNode) {
			node = (TestableNode)node2;
		}

		Tester tester = getTester(node);
		if( tester == null ) {
			// TODO log?
			return;
		}
		byte[] result = tester.getValue(node, contents);
		if (result != null && tester.matches(node, contents, result)) {
			matchResult.addMatchedMimeType(node.getLevel(), node.getMimeType());
			matchResult.addMatchingNode(node);
			if( node.hasOutput()) {
				String out = ((Magic)node).getOutput();
				if( out != null && !out.isEmpty()) {
					String toPrint = tester.formatString((Magic)node, out, result);
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
				fillResult(children[i], matchResult);
			}
		}
	}
	
	private String formatString(Magic m, String out, byte[] val) {
		if( StringUtils.isStringType(m.getType())) {
			String s = new String(val);
			return String.format(out, s);
		}
		ByteBuffer bb = ByteBuffer.wrap(val);
		switch(val.length) {
		case 1:
			return String.format(out, bb.get());
		case 2:
			return String.format(out,  bb.getShort());
		case 4: 
			return String.format(out,  bb.getInt());
		case 8: 
			return String.format(out, bb.getDouble());
		}
		return out;
	}
}
