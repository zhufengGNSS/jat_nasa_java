/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2002 National Aeronautics and Space Administration and the Center for Space Research (CSR),
 * The University of Texas at Austin. All rights reserved.
 *
 * This file is part of JAT. JAT is free software; you can
 * redistribute it and/or modify it under the terms of the
 * NASA Open Source Agreement
 * 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * NASA Open Source Agreement for more details.
 *
 * You should have received a copy of the NASA Open Source Agreement
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package jat.core.math;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import jat.core.math.matvec.data.VectorN;

/**
 * Transformations between different coordinate systems
 * 
 * @author Tobias Berthold
 * 
 */
public class CoordTransform {
	/**
	 * Convert spherical coordinates to Cartesian coordinates
	 * 
	 * @param r
	 *            radius
	 * @param theta
	 *            angle between z-axis and point in radians
	 * @param phi
	 *            angle between x-axis and projection of point onto x-y plane in
	 *            radians
	 * @return VectorN
	 */
	public static VectorN Spherical_to_Cartesian_rad(double r, double theta, double phi) {
		VectorN out = new VectorN(3);
		out.set(0, r * Math.sin(theta) * Math.cos(phi));
		out.set(1, r * Math.sin(theta) * Math.sin(phi));
		out.set(2, r * Math.cos(theta));
		return out;
	}

	public static VectorN Spherical_to_Cartesian_deg(double r, double theta, double phi) {
		return Spherical_to_Cartesian_rad(r, MathUtils.DEG2RAD * theta, MathUtils.DEG2RAD * phi);
	}

	public static Vector3f Cartesian_to_Spherical(Vector3f coord) {
		// r, theta, phi
		if (coord.x == 0)
			;
		// coord.x = Math.e.FLT_EPSILON;
		Vector3f out = new Vector3f();

		out.x = (float) Math.sqrt((coord.x * coord.x) + (coord.y * coord.y) + (coord.z * coord.z));
		out.y = (float) Math.acos(coord.y / coord.x);
		out.z = (float) Math.atan(coord.z / coord.x);
		if (coord.x < 0)
			out.y += Math.PI;

		return out;
	}

	public static Vector3f Spherical_to_Cartesian(Vector3f coord) {
		Vector3f out = new Vector3f();
		double r = coord.x;
		double theta = coord.y;
		double phi = coord.z;
		out.x = (float) (r * Math.sin(theta) * Math.cos(phi));
		out.y = (float) (r * Math.sin(theta) * Math.sin(phi));
		out.z = (float) (r * Math.cos(theta));
		return out;
	}
}
