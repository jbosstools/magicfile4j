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
import java.util.Iterator;

import net.oxbeef.magicfile4j.MagicFileModel;

public class MagicNode {
	protected ArrayList<MagicNode> children;
	protected MagicNode parent;
	
	public MagicNode(MagicNode parent) {
		this.parent = parent;
		children = new ArrayList<MagicNode>();
	}

	public void addChild(MagicNode m) {
		children.add(m);
	}

	public MagicNode getParent() {
		return parent;
	}
	
	public MagicNode[] getChildren() {
		return (MagicNode[]) children.toArray(new MagicNode[children.size()]);
	}
	
	public MagicFileModel getModel() {
		if( this instanceof MagicFileModel ) {
			return (MagicFileModel)this;
		}
		if( parent != null ) {
			return parent.getModel();
		}
		return null;
	}
	
	
	public MagicNode clone(MagicNode newParent) {
		MagicNode ret = new MagicNode(newParent);
		cloneChildren(ret);
		return ret;
	}
	protected void cloneChildren(MagicNode newParent) {
		Iterator<MagicNode> kids = children.iterator();
		while(kids.hasNext()) {
			newParent.addChild(kids.next().clone(newParent));
		}
	}
}