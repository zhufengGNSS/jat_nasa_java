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

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import com.sun.j3d.utils.geometry.*;

/** 
 * @author Tobias Berthold
 */
public class Cylinder3D extends ABody3D
{
	public static float size_factor = 1.0f;
	float radius, height;	
	static Color3f EmissiveColor = Colors.darkgray;

	public Cylinder3D(float radius, float height, Color3f DiffuseColor)
	{
		this.radius = radius;
		this.height = height;
		addChild(new Cylinder(radius, height, createMatAppear(DiffuseColor, Colors.white, EmissiveColor,10.0f)));
	}

	public Cylinder3D(float radius, Color3f DiffuseColor, Color3f EmissiveColor)
	{
		this.radius = radius;
		addChild(new Sphere(radius, Sphere.GENERATE_NORMALS, 60, createMatAppear(DiffuseColor, Colors.white, EmissiveColor,10.0f)));
	}

	public Cylinder3D(float radius, Color3f DiffuseColor, Color3f EmissiveColor, double x, double y, double z)
	{
		this.radius = radius;
		addChild(new Sphere(radius, Sphere.GENERATE_NORMALS, 60, createMatAppear(DiffuseColor, Colors.white, EmissiveColor,10.0f)));
		set_position(x, y, z);
	}

	public Cylinder3D(float radius, Color3f DiffuseColor, float x, float y, float z)
	{
		this.radius = radius;
		addChild(new Sphere(radius, Sphere.GENERATE_NORMALS, 60, createMatAppear(DiffuseColor, Colors.white, EmissiveColor,10.0f)));
		set_position(x, y, z);
	}

	public Cylinder3D(float radius, Color3f DiffuseColor, double x, double y, double z)
	{
		this.radius = radius;
		addChild(new Sphere(radius, Sphere.GENERATE_NORMALS, 60, createMatAppear(DiffuseColor, Colors.white, EmissiveColor,10.0f)));
		set_position(x, y, z);
	}

	protected static Appearance createMatAppear(
		Color3f DiffuseColor,
		Color3f SpecularColor,
		Color3f EmissiveColor,
		float shine)
	{
		Appearance appear = new Appearance();
		Material material = new Material();
		material.setDiffuseColor(DiffuseColor);
		material.setSpecularColor(SpecularColor);
		material.setShininess(shine);
		material.setEmissiveColor(EmissiveColor);
		appear.setMaterial(material);

		//For Transparency
		TransparencyAttributes ta = new TransparencyAttributes();
		ta.setTransparency(0.5f);
		ta.setTransparencyMode(TransparencyAttributes.BLENDED);
		appear.setTransparencyAttributes(ta);

		return appear;
	}
}
