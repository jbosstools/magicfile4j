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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

import net.oxbeef.magicfile4j.internal.model.Magic;
import net.oxbeef.magicfile4j.internal.offset.StringUtils;

public class Main {

	public static void main(String[] args) throws IOException {
		new Main().run();
	}

	public Main() {
	}

	public void run() throws IOException {
//		File f = new File("/home/rob/code/magic/test/magic_img");
//
//		Path path = Paths.get("/home/rob/Desktop/pics/wall-e.png");
//		byte[] data = Files.readAllBytes(path);
//
//		MagicFileModel mr = new MagicFileLoader().readMagicFile(f);
//		MagicResult result = new MagicRunner("Test", data).runMatcher(mr);
//		System.out.println(result.getOutput());
//		System.out.println(result.getMimeType());
	}
}
