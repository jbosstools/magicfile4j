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

import java.util.ArrayList;

import net.oxbeef.magicfile4j.internal.model.Magic;
import net.oxbeef.magicfile4j.internal.model.MagicNode;

public class MagicResult {
	private MagicNode topMatchingNode;
	private ArrayList<MagicNode> allMatching;
	StringBuffer fullDescription;
	public MagicResult(MagicNode m) {
		topMatchingNode = m;
		allMatching = new ArrayList<MagicNode>();
		fullDescription = new StringBuffer();
	}
	public void addMatchingNode(MagicNode node) {
		allMatching.add(node);
	}
	
	public void appendOutput(String output) {
		if( output != null && !output.isEmpty()) {
			fullDescription.append(output);
		}
	}
	
	public String getOutput() {
		return fullDescription.toString();
	}
	
	private int mimeLevel = -1;
	private String mime = null;
	public void addMatchedMimeType(int level, String mime) {
		if( level > mimeLevel ) {
			mimeLevel = level;
			this.mime = mime;
		}
	}
	
	public String getMimeType() {
		return mime;
	}
}
