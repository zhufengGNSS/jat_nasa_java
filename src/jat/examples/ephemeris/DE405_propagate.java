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

package jat.examples.ephemeris;

import jat.core.ephemeris.DE405Frame;
import jat.core.ephemeris.DE405Plus;
import jat.core.util.PathUtil;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;

public class DE405_propagate {
	static boolean print = false;

	void doExample() {
		double tf = 10000000.;
		double[] y = { 1e8, 0, 0, 0, 25, 0 }; // initial state

		PathUtil path = new PathUtil();

		DE405Plus Eph = new DE405Plus(path);
		Eph.setFrame(DE405Frame.frame.HEE);
		Eph.printSteps = true;
		FirstOrderIntegrator dp853 = new DormandPrince853Integrator(1.0e-8, tf / 10.0, 1.0e-10, 1.0e-10);
		dp853.addStepHandler(Eph.stepHandler);
		FirstOrderDifferentialEquations ode = Eph;

		dp853.integrate(ode, 0.0, y, tf, y); // now y contains final state
												// at
												// time tf
		if (print) {
			String nf = "%10.3f ";
			String format = nf + nf + nf + nf + nf;
			System.out.printf(format, tf, y[0], y[1], y[2], Eph.energy(y));
			System.out.println();
		}
	}

	public static void main(String[] args) {

		DE405_propagate ex = new DE405_propagate();
		ex.doExample();

	}

}
