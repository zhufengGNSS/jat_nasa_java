/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2002 The JAT Project and the Center for Space Research (CSR),
 * The University of Texas at Austin. All rights reserved.
 *
 * This file is part of JAT. JAT is free software; you can
 * redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package jat.vr;

import jat.cm.*;
import java.applet.Applet;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.geometry.*;

/** Planet class
 * @author Tobias Berthold
 */
public class Star3D extends Body3D
{
	float radius;
	String Texturefilename;
	Appearance app;
	Color3f Starcolor; // Star color if texture not found

	public Star3D(Applet myapplet, float scale)
	{
		super(myapplet);
		this.scale = scale;
		Texturefilename = images_path + "sun.jpg";
		radius = (float) cm.sun_radius;
		Starcolor = Colors.blue;

		if (Texturefilename == null)
		{
			app = createMatAppear_star(Colors.blue, Colors.white, 10.0f);
		}
		else
		{
			TextureLoader tex = new TextureLoader(Texturefilename, myapplet);
			TextureAttributes ta = new TextureAttributes();
			ta.setTextureMode(TextureAttributes.MODULATE);
			app = createMatAppear_star(Colors.white, Colors.white, 10.0f);
			app.setTextureAttributes(ta);
			app.setTexture(tex.getTexture());
		}

		addChild(new Sphere(scale * radius, Sphere.GENERATE_NORMALS | Sphere.GENERATE_TEXTURE_COORDS, 60, app));

	}

	static Appearance createMatAppear_star(Color3f dColor, Color3f sColor, float shine)
	{
		Appearance appear = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(dColor);
		material.setSpecularColor(sColor);
		material.setShininess(shine);
		material.setEmissiveColor(1.f, 1.f, 1.f);
		appear.setMaterial(material);
		return appear;
	}

}
