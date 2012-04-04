/* JAT: Java Astrodynamics Toolkit
 * 
  Copyright 2012 Tobias Berthold

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

// loads resources (any file - image, html, etc.)
// works in Eclipse, in a jar file, and (hopefully) applet on web page!! 

package jat.application.AttitudeSimulator;

import java.awt.image.*;
import java.io.*;
import java.net.URL;

import javax.imageio.*;
import javax.swing.*;

public class ResourceLoader {
	private JLabel label = new JLabel();

	public ResourceLoader() {
	}

	public InputStream loadResource(String name) {
		return getClass().getResourceAsStream(name);
	}

	public URL loadURL(String relative_path) {
		return getClass().getResource(relative_path);
	}

	public BufferedImage loadImage(String name) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(loadResource(name));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return img;
	}

}

