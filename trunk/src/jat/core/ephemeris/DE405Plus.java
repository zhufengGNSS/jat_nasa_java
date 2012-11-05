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

package jat.core.ephemeris;

import jat.core.astronomy.SolarSystemBodies;
import jat.core.ephemeris.DE405Body.body;
import jat.core.ephemeris.DE405Frame.frame;
import jat.core.spacetime.TimeAPL;
import jat.core.util.PathUtil;
import jat.core.util.jatMessages;
import jat.coreNOSA.cm.Constants;
import jat.coreNOSA.cm.cm;
import jat.coreNOSA.math.MatrixVector.data.VectorN;
import jat.coreNOSA.spacetime.Time;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/**
 * The DE405 Ephemeris data files from JPL are given in the ICRF frame. This
 * class allows to choose the frame for which position and velocity are
 * calculated (See DE405Frame.java)
 * 
 */
public class DE405Plus extends DE405APL implements FirstOrderDifferentialEquations {

	public frame ephFrame;
	jatMessages messages;
	VectorN[] posvelICRF, posvel;
	public boolean printSteps = false;
	SolarSystemBodies sb;
	public TimeAPL integrationStartTime;
	public ArrayList<Double> time = new ArrayList<Double>();
	public ArrayList<Double> xsol = new ArrayList<Double>();
	public ArrayList<Double> ysol = new ArrayList<Double>();
	public ArrayList<Double> zsol = new ArrayList<Double>();

	public TimeAPL getIntegrationStartTime() {
		return integrationStartTime;
	}

	public void setIntegrationStartTime(TimeAPL integrationStartTime) {
		this.integrationStartTime = integrationStartTime;
	}

	public DE405Plus() {
		super();
		ephFrame = frame.ICRF;
		posvelICRF = new VectorN[12];
		posvel = new VectorN[12];
		sb = new SolarSystemBodies();
	}

	public DE405Plus(PathUtil path, jatMessages messages) {
		this.path = path;
		DE405_path = path.DE405Path;
		if (messages != null)
			messages.addln("[DE405Plus] " + DE405_path);
		ephFrame = frame.ICRF;
		posvelICRF = new VectorN[12];
		posvel = new VectorN[12];
		sb = new SolarSystemBodies();
	}

	public DE405Plus(PathUtil path) {
		this.path = path;
		DE405_path = path.DE405Path;
		ephFrame = frame.ICRF;
		posvelICRF = new VectorN[12];
		posvel = new VectorN[12];
		sb = new SolarSystemBodies();
	}

	public void setFrame(frame ephFrame) {
		this.ephFrame = ephFrame;
	}

	public void computeDerivatives(double t, double[] yval, double[] yDot) {
		double mu_body;
		double xBody = 0, yBody = 0, zBody = 0; // x, y, z coordinates of body i
												// in frame
		double x, x2, y, y2, z, z2; // x, y, z distance from spacecraft to body
									// i and squares
		double r_sc_body, r_sc_body3; // distance from spacecraft to body i
		VectorN bodyPos, earthPos;
		TimeAPL EphTime;
		//

		// Derivatives
		EphTime = integrationStartTime.plus(t);
		// integrationStartTime.println();
		// EphTime.println();
		yDot[0] = yval[3];
		yDot[1] = yval[4];
		yDot[2] = yval[5];

		try {
			// contribution from the sun
			mu_body = 1E-0 * sb.Bodies[body.SUN.ordinal()].mu;
			bodyPos = get_planet_pos(body.SUN, EphTime);
			// bodyPos.print("sun pos");
			xBody = bodyPos.x[0];
			yBody = bodyPos.x[1];
			zBody = bodyPos.x[2];
			x = yval[0] - xBody;
			x2 = x * x;
			y = yval[1] - yBody;
			y2 = y * y;
			z = yval[2] - zBody;
			z2 = z * z;
			r_sc_body = Math.sqrt(x2 + y2 + z2);
			r_sc_body3 = r_sc_body * r_sc_body * r_sc_body;
			yDot[3] = -mu_body * x / r_sc_body3;
			yDot[4] = -mu_body * y / r_sc_body3;
			yDot[5] = -mu_body * z / r_sc_body3;

			// // contribution from the earth
			mu_body = 1E7 * sb.Bodies[body.EARTH.ordinal()].mu;
			bodyPos = get_planet_pos(body.EARTH, EphTime);
			//bodyPos.print("earth pos");
			xBody = bodyPos.x[0];
			yBody = bodyPos.x[1];
			zBody = bodyPos.x[2];
			x = yval[0] - xBody;
			x2 = x * x;
			y = yval[1] - yBody;
			y2 = y * y;
			z = yval[2] - zBody;
			z2 = z * z;
			r_sc_body = Math.sqrt(x2 + y2 + z2);
			r_sc_body3 = r_sc_body * r_sc_body * r_sc_body;
			yDot[3] += -mu_body * x / r_sc_body3;
			yDot[4] += -mu_body * y / r_sc_body3;
			yDot[5] += -mu_body * z / r_sc_body3;

			
			//yDot[3] += -1E-6;

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public int getDimension() {
		return 6;
	}

	public StepHandler stepHandler = new StepHandler() {
		public void init(double t0, double[] y0, double t) {
		}

		public void handleStep(StepInterpolator interpolator, boolean isLast) {
			double t = interpolator.getCurrentTime();
			double[] y = interpolator.getInterpolatedState();
			if (printSteps) {
				String nf = "%14.3f ";
				String format = nf + nf + nf + nf + nf;
				System.out.printf(format, t, y[0], y[1], y[2], energy(y));
				System.out.println();
			}
			time.add(t);
			xsol.add(y[0]);
			ysol.add(y[1]);
		}
	};

	public double energy(double yin[]) {
		return 2.1;
	}

	public void update_posvel_and_frame(Time t) throws IOException {

		update_planetary_ephemeris(t);

		// first get ICRF ephemeris
		double[] pv = new double[6];
		for (body q : EnumSet.allOf(body.class)) {
			int bodyNumber = q.ordinal();
			double daysec = 3600. * 24.;
			pv[0] = planet_r[bodyNumber][1];
			pv[1] = planet_r[bodyNumber][2];
			pv[2] = planet_r[bodyNumber][3];
			pv[3] = planet_rprime[bodyNumber][1] / daysec;
			pv[4] = planet_rprime[bodyNumber][2] / daysec;
			pv[5] = planet_rprime[bodyNumber][3] / daysec;

			posvelICRF[bodyNumber] = new VectorN(pv);
		}

		// Now transform posvel to desired reference frame
		for (body q : EnumSet.allOf(body.class)) {
			int bodyNumber = q.ordinal();
			VectorN in = posvelICRF[bodyNumber];
			switch (ephFrame) {
			case ICRF:
				posvel[bodyNumber] = in;
				break;
			case HEE:
				posvel[bodyNumber] = ecliptic_obliquity_rotate(in);
				break;
			case ECI:

				posvel[bodyNumber] = ICRF_to_ECI(in, t);
				break;
			default:
				posvel[bodyNumber] = in;
				break;
			}
		}

	}

	public VectorN get_planet_posvel(body bodyEnum, Time t) throws IOException {
		update_posvel_and_frame(t);
		return posvel[bodyEnum.ordinal()];
	}

	public VectorN get_planet_pos(body bodyEnum, Time t) throws IOException {

		update_posvel_and_frame(t);

		double[] pos = new double[3];
		int bodyNumber = bodyEnum.ordinal();
		pos[0] = posvel[bodyNumber].x[0];
		pos[1] = posvel[bodyNumber].x[1];
		pos[2] = posvel[bodyNumber].x[2];

		VectorN out = new VectorN(pos);

		return out;

	}

	public VectorN get_planet_vel(body bodyEnum, Time t) throws IOException {

		update_posvel_and_frame(t);

		double[] vel = new double[3];
		int bodyNumber = bodyEnum.ordinal();
		vel[0] = posvel[bodyNumber].x[3];
		vel[1] = posvel[bodyNumber].x[4];
		vel[2] = posvel[bodyNumber].x[5];

		VectorN out = new VectorN(vel);

		return out;

	}

	VectorN ecliptic_obliquity_rotate(VectorN rv) {
		VectorN returnval = new VectorN(6);
		double x, y, z, vx, vy, vz, eps, c, s;
		x = rv.get(0);
		y = rv.get(1);
		z = rv.get(2);
		eps = cm.Rad(Constants.eps);
		c = Math.cos(eps);
		s = Math.sin(eps);
		returnval.x[0] = x;
		returnval.x[1] = c * y + s * z;
		returnval.x[2] = -s * y + c * z;
		vx = rv.get(3);
		vy = rv.get(4);
		vz = rv.get(5);
		returnval.x[3] = vx;
		returnval.x[4] = c * vy + s * vz;
		returnval.x[5] = -s * vy + c * vz;

		return returnval;
	}

	private VectorN ICRF_to_ECI(VectorN in, Time mytime) throws IOException {

		double[] posvel = new double[6];
		int bodyNumber = DE405Body.body.EARTH.ordinal();
		posvel[0] = posvelICRF[bodyNumber].x[0];
		posvel[1] = posvelICRF[bodyNumber].x[1];
		posvel[2] = posvelICRF[bodyNumber].x[2];
		posvel[3] = posvelICRF[bodyNumber].x[3];
		posvel[4] = posvelICRF[bodyNumber].x[4];
		posvel[5] = posvelICRF[bodyNumber].x[5];

		VectorN earthPos = new VectorN(posvel);

		VectorN returnval = in.minus(earthPos);
		return returnval;
	}

}