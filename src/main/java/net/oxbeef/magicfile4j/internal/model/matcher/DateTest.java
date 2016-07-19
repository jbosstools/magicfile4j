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

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import net.oxbeef.magicfile4j.internal.endian.Endian;
import net.oxbeef.magicfile4j.internal.model.Magic;
import net.oxbeef.magicfile4j.internal.model.TestableNode;

public class DateTest extends NumericTest {
	
	public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
	
	private TimeZone zone;
	public DateTest(int size, Endian endian, TimeZone zone) {
		super(size, endian);
		this.zone = zone;
	}

	protected boolean isSigned(TestableNode magic) {
		return false;
	}
	
	@Override
	public String formatString(Magic m, String out, byte[] val) {
		ByteBuffer bb = ByteBuffer.wrap(val);
		long l = -1;
		switch(size) {
		case 4:
			l = bb.getInt();
			break;
		case 8:
			l = bb.getLong();
			break;
		}
		Date date = new Date(l);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		if( zone != null )
			format.setTimeZone(zone);
		String asStr = format.format(date);
		return String.format(out, asStr);
	}
}