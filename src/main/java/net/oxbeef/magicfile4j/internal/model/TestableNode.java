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

import java.util.HashMap;

import net.oxbeef.magicfile4j.internal.model.ext.Strength;
import net.oxbeef.magicfile4j.internal.offset.OffsetResolver;

public abstract class TestableNode extends MagicNode {
	public static final String PROP_MIME = "mime.property";
	public static final String PROP_STRENGTH = "strength.property";
	
	protected HashMap<String, Object> properties;  // used for extensions 
	protected int level;
	private String offsetString;
	private String type;
	private boolean invertEndian = false;
	public TestableNode(MagicNode parent, int level, String offsetString, String type) {
		super(parent);
		this.offsetString = offsetString;
		this.type = type;
		this.level = level;
		invertEndian = false;
	}
	
	public boolean shouldInvertEndian() {
		if( parent instanceof TestableNode ) {
			boolean parentFlip = ((TestableNode)parent).shouldInvertEndian();
			if( invertEndian ) {
				return !parentFlip;
			} else {
				return parentFlip;
			}
		}
		return invertEndian;
	}
	
	public void setInvertEndian(boolean val) {
		invertEndian = val;
	}
	
	public int getLevel() {
		return level;
	}
	public String getOffsetString() {
		return offsetString;
	}

	protected boolean canModifyChildrenOffset() {
		return false;
	}
	
	protected boolean anyAncestorCanModifyChildrenOffset() {
		if( parent instanceof TestableNode) {
			return canModifyChildrenOffset() || ((TestableNode)parent).canModifyChildrenOffset();
		}
		return canModifyChildrenOffset();
	}
	
	protected int getModifiedOffset(TestableNode child, String childOffsetString, int calculatedOffset, byte[] data) {
		if( anyAncestorCanModifyChildrenOffset() && parent instanceof TestableNode) {
			return ((TestableNode)parent).getModifiedOffset(child, childOffsetString, calculatedOffset, data);
		}
		return calculatedOffset;
	}
	
	public int resolveOffset(byte[] data) {
		int calculated = new OffsetResolver().getResolutionIndex(offsetString, this, data);
		return getModifiedOffset(this, getOffsetString(), calculated, data);
	}
	
	public String getType() {
		return type;
	}

	public boolean hasOutput() {
		return false;
	}

	public Object getProperty(String key) {
		if( properties != null )
			return properties.get(key);
		return null;
	}

	public void setProperty(String key, Object obj) {
		if( properties == null ) {
			this.properties = new HashMap<String, Object>();
		}
		properties.put(key, obj);
	}
	
	public TestableNode getMagicBlockRoot() {
		if( parent instanceof MagicFileModel ) 
			return this;
		if( parent != null && parent instanceof Magic) {
			return ((TestableNode)parent).getMagicBlockRoot();
		}
		return null;
	}
	
	public String getMimeType() {
		String thisNode = (String)getProperty(PROP_MIME);
		if( thisNode == null ) {
			if( parent instanceof Magic) {
				return ((TestableNode)parent).getMimeType();
			}
		}
		return thisNode;
	}
	
	public void setStrength(Strength s) {
		TestableNode blockRoot = getMagicBlockRoot();
		if( blockRoot != this ) {
			// TODO error, strength can only be set on root of the set
			blockRoot.setStrength(s);
		} else {
			// I am the block root
			setProperty(PROP_STRENGTH, s);
		}
	}
	
	public int getStrength(int defaultStrength) {
		TestableNode blockRoot = getMagicBlockRoot();
		if( blockRoot != this ) {
			return getMagicBlockRoot().getStrength(defaultStrength);
		}
		Object s = getProperty(PROP_STRENGTH);
		if( s instanceof Strength) {
			return ((Strength)s).getStrength(defaultStrength);
		}
		return defaultStrength;
	}
}
