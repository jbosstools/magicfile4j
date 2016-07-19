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
package net.oxbeef.magicfile4j.internal.model.matcher;

import net.oxbeef.magicfile4j.internal.model.Magic;
import net.oxbeef.magicfile4j.internal.model.TestableNode;

public abstract class Tester {
	/**
	 * Get the text / values for this given magic test, or null if impossible
	 * 
	 * @param magic
	 * @param bytearray
	 * @return
	 */
	public abstract Object getValue(TestableNode magic, byte[] bytearray);

	
	/**
	 * Format the output string with the found bytes
	 * @param m
	 * @param out
	 * @param val
	 * @return
	 */
	public abstract String formatString(Magic m, String out, Object val);

	
	/**
	 * 
	 * Return whether this matcher agrees that the given magic entry
	 * matches for the byte array
	 * 
	 * @param magic
	 * @param byteArray
	 * @param dataAtOffset
	 * @return
	 */
	public abstract boolean matches(TestableNode magic, byte[] byteArray, Object dataAtOffset);
	
	
	
}