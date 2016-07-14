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
package net.oxbeef.magicfile4j.model.internal;

import java.util.ArrayList;
import java.util.Arrays;

public class UseNode extends TestableNode {

	private String name;
	public UseNode(MagicNode parent, int level, String offset, String type, String id) {
		super(parent, level, offset, type);
		if( id.charAt(0) == '^' ) {
			super.setInvertEndian(true);
			name = id.substring(1);
		} else {
			this.name = id;
		}
	}
	
	public String getName() {
		return name;
	}
	
	private ArrayList<MagicNode> allChildren = null;
	private NameNode namedNode = null;
	boolean notFound = false;
	public MagicNode[] getChildren() {
		if( allChildren == null ) {
			allChildren = new ArrayList<MagicNode>();
			allChildren.addAll(Arrays.asList(findDereferencedChildren()));
			allChildren.addAll(Arrays.asList(super.getChildren()));
		}
		return (MagicNode[]) allChildren.toArray(new MagicNode[allChildren.size()]);
	}

	private MagicNode[] findDereferencedChildren() {
		if( namedNode == null && !notFound) {
			namedNode = (NameNode)getModel().getNamedNode(name);
		}
		if( namedNode == null ) {
			notFound = true;
			return new MagicNode[0];
		}
		MagicNode[] namedChildren = namedNode.getChildren();
		// clone each
		ArrayList<MagicNode> cloned = new ArrayList<MagicNode>();
		for( int i = 0; i < namedChildren.length; i++ ) {
			cloned.add(namedChildren[i].clone(this));
		}
		return (MagicNode[]) cloned.toArray(new MagicNode[cloned.size()]);
	}
	

	public MagicNode clone(MagicNode newParent) {
		NameNode ret = new NameNode(newParent, getLevel(), getOffsetString(), getType(), name);
		cloneChildren(ret);
		return ret;
	}
	

	protected boolean canModifyChildrenOffset() {
		return true;
	}

	protected int getModifiedOffset(TestableNode child, String childOffsetString, int calculatedOffset, byte[] data) {
		if( child != this ) {
			if( isDirectOffset(childOffsetString)) {
				int useOffset = super.resolveOffset(data);
				return useOffset + calculatedOffset;
			}
		}
		return calculatedOffset;
	}

	private boolean isDirectOffset(String os) {
		return !os.startsWith("(") && !os.startsWith("&(");
	}
	
}
