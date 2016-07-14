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

import net.oxbeef.magicfile4j.MagicFileModel;
import net.oxbeef.magicfile4j.offset.util.StringUtils;

public class NodeFactoryUtil {

	public static TestableNode createMagicNode(MagicFileModel root, MagicNode parent, int cont_level, 
			String offset, String type, String remainder) {
		String comparison, description;
		comparison = description = null;
		try {
			if( StringUtils.MAGIC_NAME.equals(type)) {
				// named types have only 3 fields.   Offset, type, and an identifier
				NameNode working = new NameNode(parent, cont_level, offset, type, remainder);
				parent.addChild(working);
				// Register this with the model
				root.addNamedNode(working);
				return working;
			}
			if( StringUtils.MAGIC_USE.equals(type)) {
				UseNode use = new UseNode(parent, cont_level, offset, type, remainder);
				parent.addChild(use);
				return use;
			}
			
			if (StringUtils.isStringType(type)) {
				int space = StringUtils.findFirstUnescapedSpace(remainder);
				comparison = remainder.substring(0, space);
				description = comparison.length() >= remainder.length() ? null : remainder.substring(space).trim();
			} else { // if( isFloatType(type) || isDoubleType(type) || isLongType(type)) {
				comparison = remainder.trim().split("\\s+")[0];
				remainder = remainder.trim().substring(comparison.length());
				if( "x".equals(comparison)) {
					comparison = null; // use the default
				}
				description = remainder.trim();
			}
		} catch(StringIndexOutOfBoundsException aioobe) {
			aioobe.printStackTrace();
		}
		
		Magic working = new Magic(parent, cont_level, offset, type, comparison, description);
		parent.addChild(working);
		return working;
	}
	
}
