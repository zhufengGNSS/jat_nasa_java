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

package jat.core.cm;

import java.util.ArrayList;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 * @author Tobias Berthold
 * 
 *         Central Restricted Three Body Problem
 */
public class CRTBP implements FirstOrderDifferentialEquations {
	public static double mu; // CRTBP parameter
	public boolean printSteps = false;
	public ArrayList<Double> time = new ArrayList<Double>();
	public ArrayList<Double> xsol = new ArrayList<Double>();
	public ArrayList<Double> ysol = new ArrayList<Double>();
	public ArrayList<Double> zsol = new ArrayList<Double>();
	public double[][] zerovel2D;
	double[] yStart;
	public double C;
	public Vector3D LibPoints[];

	public CRTBP(double mu) {
		this.mu = mu;
		LibPoints = new Vector3D[5];
	}

	public void computeDerivatives(double t, double[] y, double[] yDot) {
		double xc, yc, zc, xdot, ydot, zdot;
		double r1, r2;
		double r1cubed, r2cubed;
		double fac1, fac2;

		xc = y[0];
		yc = y[1];
		zc = y[2];
		xdot = y[3];
		ydot = y[4];
		zdot = y[5];

		r1 = Math.sqrt((xc + mu) * ((xc + mu)) + yc * yc + zc * zc);
		r2 = Math.sqrt((xc - 1 + mu) * (xc - 1 + mu) + yc * yc + zc * zc);
		r1cubed = r1 * r1 * r1;
		r2cubed = r2 * r2 * r2;
		fac1 = -(1 - mu) / r1cubed;
		fac2 = -mu / r2cubed;

		// Derivatives
		yDot[0] = y[3];
		yDot[1] = y[4];
		yDot[2] = y[5];
		yDot[3] = fac1 * (xc + mu) + fac2 * (xc - 1 + mu) + 2 * ydot + xc;
		yDot[4] = fac1 * (yc) + fac2 * (yc) - 2 * xdot + yc;
		yDot[5] = fac1 * (zc) + fac2 * (zc);
	}

	public StepHandler stepHandler = new StepHandler() {
		public void init(double t0, double[] y0, double t) {
			C = JacobiIntegral(y0);
		}

		public void handleStep(StepInterpolator interpolator, boolean isLast) {
			double t = interpolator.getCurrentTime();
			double[] y = interpolator.getInterpolatedState();
			// System.out.println(t + " " + y[0] + " " + y[1] + " " + y[2] + " "
			// + JacobiIntegral(y));
			if (printSteps) {
				System.out.printf("%9.6f %9.6f %9.6f %9.6f %9.6f", t, y[0], y[1], y[2], JacobiIntegral(y));
				System.out.println();
			}
			time.add(t);
			xsol.add(y[0]);
			ysol.add(y[1]);
		}
	};

	public int getDimension() {
		return 6;
	}

	public double JacobiIntegral(double yin[]) {
		double x = yin[0];
		double y = yin[1];
		double z = yin[2];
		double xdot = yin[3];
		double ydot = yin[4];
		double zdot = yin[5];
		double x2 = x * x;
		double y2 = y * y;
		double z2 = z * z;
		double xdot2 = xdot * xdot;
		double ydot2 = ydot * ydot;
		double zdot2 = zdot * zdot;

		double r1 = Math.sqrt((x + mu) * (x + mu) + y2 + z2);
		double r2 = Math.sqrt((x - 1 + mu) * (x - 1 + mu) + y2 + z2);

		double C = x2 + y2 + 2.0 * (1. - mu) / r1 + 2.0 * mu / r2 - xdot2 - ydot2 - zdot2;
		return C;
	}

	private static class L123Func implements UnivariateFunction {
		public double value(double x) {

			double xpmu = x + mu;
			double omu = 1 - mu;
			double xmomu = x - omu;
			double denominator1 = Math.abs(xpmu * xpmu * xpmu);

			double y = x - xpmu * omu / Math.abs(xpmu * xpmu * xpmu) - mu * xmomu / Math.abs(xmomu * xmomu * xmomu);

			return y;
		}
	}

	public void findLibrationPoints() {

		BisectionSolver bs = new BisectionSolver();

		UnivariateFunction Lfunction = new L123Func();
		double L1 = bs.solve(100, Lfunction, 0, 1.);
		LibPoints[0] = new Vector3D(L1, 0, 0);
		double L2 = bs.solve(100, Lfunction, 1, 2.);
		LibPoints[1] = new Vector3D(L2, 0, 0);
		LibPoints[0] = new Vector3D(L1, 0, 0);
		double L3 = bs.solve(100, Lfunction, -2.0, 0);
		LibPoints[2] = new Vector3D(L3, 0, 0);
		double y45 = Math.sqrt(3.) / 2;
		LibPoints[3] = new Vector3D(.5 - mu, y45, 0);
		LibPoints[4] = new Vector3D(.5 - mu, -y45, 0);

		System.out.println("L1: " + L1);
		System.out.println("L2: " + L2);
		System.out.println("L3: " + L3);
		System.out.println("L4= (" + LibPoints[3].getX() + "," + LibPoints[3].getY() + ")");
		System.out.println("L5= (" + LibPoints[4].getX() + "," + LibPoints[4].getY() + ")");

	}

	private static class JacobiFixedx implements UnivariateFunction {
		double x, C;
		double z = 0;

		public JacobiFixedx() {
			this.x = .2;
			this.C = 3.0;
		}

		public JacobiFixedx(double x, double C) {
			this.x = x;
			this.C = C;
		}

		public void setx(double x) {
			this.x = x;
		}

		public void setC(double C) {
			this.C = C;
		}

		public double value(double y) {

			double J;

			double x2 = x * x;
			double y2 = y * y;
			double z2 = z * z;

			double r1 = Math.sqrt((x + mu) * (x + mu) + y2 + z2);
			double r2 = Math.sqrt((x - 1 + mu) * (x - 1 + mu) + y2 + z2);

			J = -C + x2 + y2 + 2.0 * (1. - mu) / r1 + 2.0 * mu / r2;
			return J;
		}
	}

	public void findZeroVelocity() {
		zerovel2D = new double[20][2];
		JacobiFixedx JF = new JacobiFixedx();
		BisectionSolver bs = new BisectionSolver();
		JF.setC(3.2);
		double x=-.5;
		for (int i=0;i<16;i++) {
			//for (double x = -.5; x < .9; x += .1) {
			x+=.1;
			JF.setx(x);
			double y = bs.solve(100, JF, 0, 2);
			System.out.println("x y " + x + " " + y);
			zerovel2D[i][0]=x;
			zerovel2D[i][1]=y;
		
		}
		// double x=0.3;

	}

}
