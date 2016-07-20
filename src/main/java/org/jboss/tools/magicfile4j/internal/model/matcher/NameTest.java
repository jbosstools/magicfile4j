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
package org.jboss.tools.magicfile4j.internal.model.matcher;

import org.jboss.tools.magicfile4j.internal.model.Magic;
import org.jboss.tools.magicfile4j.internal.model.TestableNode;

public class NameTest extends Tester {
	public boolean matches(TestableNode magic, byte[] bytearray) {
		return true;
	}
	public Object getValue(TestableNode magic, byte[] bytearray) {
		return new byte[0];
	}
	@Override
	public boolean matches(TestableNode magic, byte[] byteArray, Object dataAtOffset) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public String formatString(Magic m, String out, Object val) {
		return out;
	}
}