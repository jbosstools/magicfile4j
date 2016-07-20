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
package org.jboss.tools.magicfile4j.internal.endian;

import java.nio.ByteOrder;

public enum Endian {
	/** big endian, also called network byte order (motorola 68k) */
	BIG(new BigEndianReader()),
	/** little endian (x86) */
	LITTLE(new LittleEndianReader()),
	/** old PDP11 byte order */
	MIDDLE(new MiddleEndianReader()),
	/** uses the byte order of the current system */
	NATIVE(ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? BIG.getConverter() : LITTLE.getConverter()),
	// end
	;

	private EndianReader converter;

	private Endian(EndianReader converter) {
		this.converter = converter;
	}

	/**
	 * Returns the converter associated with this endian-type.
	 */
	public EndianReader getConverter() {
		return converter;
	}
}
