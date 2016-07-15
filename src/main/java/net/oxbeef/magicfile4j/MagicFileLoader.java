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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.oxbeef.magicfile4j.internal.model.Magic;
import net.oxbeef.magicfile4j.internal.model.MagicNode;
import net.oxbeef.magicfile4j.internal.model.NameNode;
import net.oxbeef.magicfile4j.internal.model.NodeFactoryUtil;
import net.oxbeef.magicfile4j.internal.model.TestableNode;
import net.oxbeef.magicfile4j.internal.model.UseNode;
import net.oxbeef.magicfile4j.internal.model.ext.Strength;
import net.oxbeef.magicfile4j.internal.offset.StringUtils;

public class MagicFileLoader {

	public MagicFileModel readMagicFile(InputStream io) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(io));
			return readMagicFile(br);
		} finally {
			if( br != null ) {
				try {
					br.close();
				} catch(IOException ioe) {
					// ignore
				}
			}
		}
	}

	public MagicFileModel readMagicFile(File f) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			return readMagicFile(br);
		} finally {
			if( br != null ) {
				try {
					br.close();
				} catch(IOException ioe) {
					// ignore
				}
			}
		}
	}
	private MagicFileModel readMagicFile(BufferedReader br) throws IOException {
		String line = null;
		MagicFileModel root = new MagicFileModel();
		TestableNode last = null;
		int linecount = 1;
		while ((line = br.readLine()) != null) {
			linecount++;
			if (line.isEmpty())
				continue;

			char c = line.charAt(0);
			if (c == '\0' || c == '#')
				continue;

			if (c == '!' && line.charAt(1) == ':') {
				handleBang(line, root, last);
				continue;
			}

			last = parse(line, root, last);
		}
		return root;
	}
	
	
	private void debug(String msg) {
		System.out.println(msg);
	}

	private TestableNode parse(String line, MagicFileModel root, TestableNode last) {
		int cont_level = 0;
		int lineLength = line.length();
		while (cont_level < lineLength && line.charAt(cont_level) == '>') {
			cont_level++;
		}
		
		String withoutLevels = line.substring(cont_level);
		MagicNode parent = null;

		if (cont_level != 0) {
			if (last == null) {
				debug("No current entry for continuation");
				return null;
			} 
			int continuationDiff = cont_level - last.getLevel();
			if( continuationDiff > 1 ) {
				debug(String.format("New continuation level %d is more than one larger than current level %d", cont_level, last.getLevel()));
				parent = last;
			} else if( continuationDiff == 0 ) {
				// A sister to 'last'
				parent = last.getParent();
			} else if( continuationDiff == 1 ) {
				parent = last;
			} else if( continuationDiff < 0 ) {
				// moving up continuation 
				parent = last.getParent();
				if( parent instanceof Magic ) {
					Magic p2 = ((Magic)parent);
					while(cont_level - p2.getLevel() > 1) {
						parent = p2.getParent();
					}
				}
			}
			// parent is now properly set
			
		} else {
			parent = root;
		}
		
		if( parent == null ) {
			debug("Unable to find a parent at the proper continuation level");
			return null;
		}
		// Top level 
		String offset = withoutLevels.split("\\s+")[0];
		String remainder = withoutLevels.substring(offset.length()).replaceAll("^\\s+", "");
		String type = remainder.split("\\s+")[0];
		remainder = remainder.substring(type.length()).replaceAll("^\\s+", "");
		
		return NodeFactoryUtil.createMagicNode(root, parent, cont_level, offset, type, remainder);
	}
	

	public void handleBang(String line, MagicFileModel root, TestableNode last) {
		if (line.startsWith("!:mime")) {
			parseMime(line, root, last);
		} else if (line.startsWith("!:ext")) {
			parseExt(line, root, last);
		} else if (line.startsWith("!:strength")) {
			parseStrength(line, root, last);
		} else if (line.startsWith("!:apple")) {
			parseApple(line, root, last);
		}
	}




	private void parseApple(String line, MagicFileModel root, TestableNode last) {
		// TODO Auto-generated method stub

	}

	private void parseStrength(String line, MagicFileModel root, TestableNode last) {
		// TODO Auto-generated method stub
		if( line.length() > 11 ) {
			line = line.substring(10).trim();
			if( !line.isEmpty()) {
				char c = line.charAt(0);
				String rem = line.substring(1).trim();
				Integer factor = null;
				try {
					factor = Integer.parseInt(rem);
				} catch(NumberFormatException nfe) {
					// log error
					return;
				}

				switch(c) {
				case '+':
				case '-':
				case '/':
				case '*':
					Strength s = new Strength(c, factor.intValue());
					last.getMagicBlockRoot().setStrength(s);
					break;
				default: 
					// LOG ERROR
					return;
				
				}
			}
		}

	}

	private void parseExt(String line, MagicFileModel root, TestableNode last) {
		// TODO Auto-generated method stub

	}

	private void parseMime(String line, MagicFileModel root, TestableNode last) {
		if( line.length() > 7) {
			String mimetype = line.substring(7).trim();
			if( mimetype != null && !mimetype.isEmpty())
				last.setProperty(Magic.PROP_MIME, mimetype);
		}
	}

}
