package jat.core.astronomy;

import jat.core.ephemeris.DE405APL;

public class SolarSystemBodies {

	public static Body[] Bodies;

	public class Body {
		DE405APL.body body;
		public double radius; // km
		public double mass; // kg

		public Body(jat.core.ephemeris.DE405APL.body body, double radius, double mass, double orbitalPeriod) {
			this.body = body;
			this.radius = radius;
			this.mass = mass;
			this.orbitalPeriod = orbitalPeriod;
		}

		public double orbitalPeriod; // days

	}

	public SolarSystemBodies() {

		Bodies = new SolarSystemBodies.Body[10];

		Bodies[1] = new Body(DE405APL.body.MERCURY, 2439.7, 330.2e21, 88);
		Bodies[2] = new Body(DE405APL.body.VENUS, 6051.8, 4868.5e21, 225);
		Bodies[3] = new Body(DE405APL.body.EARTH_MOON_BARY, 4880., 5972.2E21, 365);
		Bodies[4] = new Body(DE405APL.body.MARS, 3396., 641.85e21, 687);
		Bodies[5] = new Body(DE405APL.body.JUPITER, 69911, 1898600.0e21, 4332.59);
		Bodies[6] = new Body(DE405APL.body.SATURN, 54364, 568460.0e21, 10759.22);

	}

}