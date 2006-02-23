/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2006 The JAT Project. All rights reserved.
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

package jat.demo.OptimalLambert;

import jat.alg.integrators.*;
import jat.cm.*;
import jat.matvec.data.VectorN;
import jat.plot.*;

/**
 * @author Tobias Berthold
 *
 */
public class Orbit implements Printable
{
	SinglePlot plot;
	TwoBody twobody;
	int plotnumber;
	double plotperiod;
	
	public Orbit(double mu, KeplerElements k, int plotnumber)
	{
		twobody = new TwoBody( mu, k);
		this.plotnumber=plotnumber;
		plotperiod=twobody.period();

	}

	public Orbit(double mu, VectorN r, VectorN v, int plotnumber)
	{

		twobody = new TwoBody(mu, r,v);
		this.plotnumber=plotnumber;
		plotperiod=twobody.period();

	}

	public void plot_orbit(SinglePlot plot)
	{
		this.plot=plot;	
		
		twobody.propagate(0., plotperiod, this, true);
		
	}

	public void print(double t, double[] y)
	{
		boolean first = true;
		if (t == 0.0)
			first = false;

		// print to the screen for warm fuzzy feeling
		//System.out.println(t + " " + y[0] + " " + y[1] + " " + first);

		// add data point to the plot
		plot.plot.addPoint(plotnumber, y[0], y[1], first);
	}

}
