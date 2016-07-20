/*
 * Copyright (c) 2016 Red Hat, Inc., Rob Stryker, and Contributors
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

public class SearchTest extends StringTest {
	
	protected String getMatchedOutput(char[] test, byte[] bytes, StringTestValue dataAtOffset) {
		String flags = dataAtOffset.flags;
		String[] arr = flags.split("/");
		int numSpots = -1;
		for( int i = 0; i < arr.length && numSpots < 0; i++ ) {
			if( allDigits(arr[i])) {
				numSpots = Integer.parseInt(arr[i]);
			}
		}
		if( numSpots < 0 ) {
			return null; // We can't search forever
		}
		String r = null;
		for( int i = 0; i < numSpots; i++ ) {
			r = super.getMatchedOutput(test, bytes, dataAtOffset);
			dataAtOffset.offset++;
			if( r != null )
				return r;
		}
		return null;
	}
	
	private boolean allDigits(String s) {
		if( s.isEmpty())
			return false;
		for( int i = 0; i < s.length(); i++ ) {
			if( !Character.isDigit(s.charAt(i)))
				return false;
		}
		return true;
	}

}