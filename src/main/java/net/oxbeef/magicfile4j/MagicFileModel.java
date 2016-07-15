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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import net.oxbeef.magicfile4j.internal.model.MagicNode;
import net.oxbeef.magicfile4j.internal.model.NameNode;
import net.oxbeef.magicfile4j.internal.model.TestableNode;

public class MagicFileModel extends MagicNode {
	private static final int DEFAULT_DEFAULT_STRENGTH = 70;
	private int defaultStrength;
	private HashMap<String, MagicNode> namedNodes;
	
	public MagicFileModel() {
		super(null);
		namedNodes = new HashMap<String, MagicNode>();
		defaultStrength = DEFAULT_DEFAULT_STRENGTH;
	}
	
	public void addNamedNode(NameNode nn) {
		namedNodes.put(nn.getName(), nn);
	}
	
	public MagicNode getNamedNode(String name) {
		return namedNodes.get(name);
	}
	
	public void setDefaultStrength(int strength) {
		this.defaultStrength = strength;
	}
	
	public int getDefaultStrength() {
		return defaultStrength;
	}
	
	public TestableNode[] getSortedChildren() {
		ArrayList<TestableNode> ret = new ArrayList<TestableNode>();
		Iterator<MagicNode> it = super.children.iterator();
		while(it.hasNext()) {
			MagicNode mn = it.next();
			if( mn instanceof TestableNode) {
				ret.add(((TestableNode)mn));
			}
		}
		ret.sort(new Comparator<TestableNode>() {
			public int compare(TestableNode o1, TestableNode o2) {
				return o2.getStrength(70) - o1.getStrength(70);
			}
		});
		return (TestableNode[]) ret.toArray(new TestableNode[ret.size()]);
	}
}