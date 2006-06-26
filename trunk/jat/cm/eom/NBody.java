/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2002, 2003 The JAT Project. All rights reserved.
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

package jat.cm.eom;
import jat.alg.integrators.*;

/**
 * <P>
 * The N-Body Class provides the force model of an n-body gravity simulation.
 * The reference frame is sun-inertial, the planet positions are taken from DE405 Ephemeris data.
 *
 * @author Tobias Berthold
 * @version 1.0
 */

public class NBody implements Derivatives
{


    /** Construct an N-Body
    */
    public NBody()
    {
    }

    /** Compute the derivatives for numerical integration of N-body equations
     * of motion.
     * @return double [] containing the derivatives.
     * @param t Time (not used).
     * @param y State vector.
     */

    public double[] derivs(double t, double[] y)
    {
        double out[] = new double[6];
        return out;
    }

}