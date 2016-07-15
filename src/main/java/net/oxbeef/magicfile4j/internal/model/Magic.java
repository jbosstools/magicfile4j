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

public class Magic extends TestableNode {
	
	private String test;
	private String output;
	
	public Magic(MagicNode parent, int level, String offset, String type, String test, String output) {
		super(parent, level, offset, type);
		this.test = test;
		this.output = output;
	}
	
	public String getTest() {
		return test == null ? "x" : test;
	}

	public boolean hasOutput() {
		return output != null && !output.isEmpty();
	}

	public String getOutput() {
		return output;
	}
	
	public MagicNode clone(MagicNode newParent) {
		Magic ret = new Magic(newParent, getLevel(), getOffsetString(), getType(), test, output);
		cloneChildren(ret);
		return ret;
	}
}