/* Copyright 2002-2008 CS Communication & Systèmes
 * Licensed to CS Communication & Systèmes (CS) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * CS licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.orekit.frames;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.orekit.errors.OrekitException;
import org.orekit.frames.series.BodiesElements;
import org.orekit.frames.series.Development;
import org.orekit.time.AbsoluteDate;

/** Intermediate Reference Frame 2000 : true equinox and equator of date.
 * <p> It considers precession and nutation effects and not the earth rotation. Its parent
 * frame is the J2000 frame. <p>
 * @version $Revision: 1726 $ $Date: 2008-06-20 11:18:17 +0200 (ven 20 jun 2008) $
 */
class IRF2000Frame extends Frame {

    /** Serializable UID. */
    private static final long serialVersionUID = 2781008917378714616L;

    /** 2&pi;. */
    private static final double TWO_PI = 2.0 * Math.PI;

    /** Radians per arcsecond. */
    private static final double RADIANS_PER_ARC_SECOND = TWO_PI / 1296000;

    /** Julian century per second. */
    private static final double JULIAN_CENTURY_PER_SECOND = 1.0 / (36525.0 * 86400.0);

    // CHECKSTYLE: stop JavadocVariable check

    // lunisolar nutation elements
    private static final double F10 = Math.toRadians(134.96340251);
    private static final double F11 = 1717915923.217800  * RADIANS_PER_ARC_SECOND;
    private static final double F12 =         31.879200  * RADIANS_PER_ARC_SECOND;
    private static final double F13 =          0.051635  * RADIANS_PER_ARC_SECOND;
    private static final double F14 =         -0.0002447 * RADIANS_PER_ARC_SECOND;

    private static final double F20 = Math.toRadians(357.52910918);
    private static final double F21 = 129596581.048100   * RADIANS_PER_ARC_SECOND;
    private static final double F22 =        -0.553200   * RADIANS_PER_ARC_SECOND;
    private static final double F23 =         0.000136   * RADIANS_PER_ARC_SECOND;
    private static final double F24 =        -0.00001149 * RADIANS_PER_ARC_SECOND;

    private static final double F30 = Math.toRadians(93.27209062);
    private static final double F31 = 1739527262.847800   * RADIANS_PER_ARC_SECOND;
    private static final double F32 =        -12.751200   * RADIANS_PER_ARC_SECOND;
    private static final double F33 =         -0.001037   * RADIANS_PER_ARC_SECOND;
    private static final double F34 =          0.00000417 * RADIANS_PER_ARC_SECOND;

    private static final double F40 = Math.toRadians(297.85019547);
    private static final double F41 = 1602961601.209000   * RADIANS_PER_ARC_SECOND;
    private static final double F42 =         -6.370600   * RADIANS_PER_ARC_SECOND;
    private static final double F43 =          0.006593   * RADIANS_PER_ARC_SECOND;
    private static final double F44 =         -0.00003169 * RADIANS_PER_ARC_SECOND;

    private static final double F50 = Math.toRadians(125.04455501);
    private static final double F51 = -6962890.543100   * RADIANS_PER_ARC_SECOND;
    private static final double F52 =        7.472200   * RADIANS_PER_ARC_SECOND;
    private static final double F53 =        0.007702   * RADIANS_PER_ARC_SECOND;
    private static final double F54 =       -0.00005939 * RADIANS_PER_ARC_SECOND;

    // planetary nutation elements
    private static final double F60 = 4.402608842;
    private static final double F61 = 2608.7903141574;

    private static final double F70 = 3.176146697;
    private static final double F71 = 1021.3285546211;

    private static final double F80 = 1.753470314;
    private static final double F81 = 628.3075849991;

    private static final double F90 = 6.203480913;
    private static final double F91 = 334.0612426700;

    private static final double F100 = 0.599546497;
    private static final double F101 = 52.9690962641;

    private static final double F110 = 0.874016757;
    private static final double F111 = 21.3299104960;

    private static final double F120 = 5.481293872;
    private static final double F121 = 7.4781598567;

    private static final double F130 = 5.311886287;
    private static final double F131 = 3.8133035638;

    private static final double F141 = 0.024381750;
    private static final double F142 = 0.00000538691;

    // CHECKSTYLE: resume JavadocVariable check

    /** IERS conventions (2003) resources base directory. */
    private static final String IERS_2003_BASE = "/META-INF/IERS-conventions-2003/";

    /** Resources for IERS table 5.2a from IERS conventions (2003), model A. */
    private static final String X_MODEL_2000A    = IERS_2003_BASE + "tab5.2a.txt";

    /** Resources for IERS table 5.2a from IERS conventions (2003), model B. */
    private static final String X_MODEL_2000B    = IERS_2003_BASE + "tab5.2a.reduced.txt";

    /** Resources for IERS table 5.2b from IERS conventions (2003), model A. */
    private static final String Y_MODEL_2000A    = IERS_2003_BASE + "tab5.2b.txt";

    /** Resources for IERS table 5.2b from IERS conventions (2003), model B. */
    private static final String Y_MODEL_2000B    = IERS_2003_BASE + "tab5.2b.reduced.txt";

    /** Resources for IERS table 5.2c from IERS conventions (2003), model A. */
    private static final String S_XY2_MODEL_2000A = IERS_2003_BASE + "tab5.2c.txt";

    /** Resources for IERS table 5.2c from IERS conventions (2003), model B. */
    private static final String S_XY2_MODEL2000B = IERS_2003_BASE + "tab5.2c.reduced.txt";

    /** Indicator for complete or reduced precession-nutation model. */
    private final boolean useIAU2000B;

    /** Pole position (X). */
    private final Development xDevelopment;

    /** Pole position (Y). */
    private final Development yDevelopment;

    /** Pole position (S + XY/2). */
    private final Development sxy2Development;

    /** Cached date to avoid useless computation. */
    private AbsoluteDate cachedDate;

    /** Build the IRF2000 frame singleton.
     * <p>If the <code>useIAU2000B</code> boolean parameter is true (which is the
     * recommended value) the reduced IAU2000B precession-nutation model will be
     * used, otherwise the complete IAU2000A precession-nutation model will be used.
     * The IAU2000B is recommended for most applications since it is <strong>far
     * less</strong> computation intensive than the IAU2000A model and its accuracy
     * is only slightly degraded (1 milliarcsecond instead of 0.2 milliarcsecond).</p>
     * @param date the date.
     * @param useIAU2000B if true (recommended value), the IAU2000B model will be used
     * @param name name of the frame
     * @exception OrekitException if the nutation model data embedded in the
     * library cannot be read.
     * @see Frame
     */
    protected IRF2000Frame(final AbsoluteDate date, final boolean useIAU2000B, final String name)
        throws OrekitException {

        super(getJ2000(), null , name);

        this.useIAU2000B = useIAU2000B;

        // nutation models are in micro arcseconds
        final Class<IRF2000Frame> c = IRF2000Frame.class;
        final String xModel = useIAU2000B ? X_MODEL_2000B : X_MODEL_2000A;
        xDevelopment =
            new Development(c.getResourceAsStream(xModel), RADIANS_PER_ARC_SECOND * 1.0e-6, xModel);
        final String yModel = useIAU2000B ? Y_MODEL_2000B : Y_MODEL_2000A;
        yDevelopment =
            new Development(c.getResourceAsStream(yModel), RADIANS_PER_ARC_SECOND * 1.0e-6, yModel);
        final String sxy2Model = useIAU2000B ? S_XY2_MODEL2000B : S_XY2_MODEL_2000A;
        sxy2Development =
            new Development(c.getResourceAsStream(sxy2Model), RADIANS_PER_ARC_SECOND * 1.0e-6, sxy2Model);

        // everything is in place, we can now synchronize the frame
        updateFrame(date);
    }

    /** Update the frame to the given date.
     * <p>The update considers the nutation and precession effects from IERS data.</p>
     * @param date new value of the date
     * @exception OrekitException if the nutation model data embedded in the
     * library cannot be read
     */
    protected void updateFrame(final AbsoluteDate date) throws OrekitException {

        if (cachedDate == null || cachedDate != date) {
            //    offset from J2000 epoch in julian centuries
            final double tts = date.minus(AbsoluteDate.J2000_EPOCH);
            final double ttc =  tts * JULIAN_CENTURY_PER_SECOND;

            // luni-solar and planetary elements
            final BodiesElements elements = computeBodiesElements(ttc);


            // precession and nutation effect (pole motion in celestial frame)
            final Rotation qRot = precessionNutationEffect(ttc, elements);

            // combined effects
            final Rotation combined = qRot.revert();

            // set up the transform from parent GCRS (J2000) to ITRF
            setTransform(new Transform(combined , Vector3D.ZERO));
            cachedDate = date;
        }
    }

    /** Compute the nutation elements.
     * @param tt offset from J2000.0 epoch in julian centuries
     * @return luni-solar and planetary elements
     */
    private BodiesElements computeBodiesElements(final double tt) {
        if (useIAU2000B) {
            return new BodiesElements(F11 * tt + F10, // mean anomaly of the Moon
                                      F21 * tt + F20, // mean anomaly of the Sun
                                      F31 * tt + F30, // L - &Omega; where L is the mean longitude of the Moon
                                      F41 * tt + F40, // mean elongation of the Moon from the Sun
                                      F51 * tt + F50, // mean longitude of the ascending node of the Moon
                                      Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN,
                                      Double.NaN, Double.NaN, Double.NaN, Double.NaN);
        }
        return new BodiesElements((((F14 * tt + F13) * tt + F12) * tt + F11) * tt + F10, // mean anomaly of the Moon
                                  (((F24 * tt + F23) * tt + F22) * tt + F21) * tt + F20, // mean anomaly of the Sun
                                  (((F34 * tt + F33) * tt + F32) * tt + F31) * tt + F30, // L - &Omega; where L is the mean longitude of the Moon
                                  (((F44 * tt + F43) * tt + F42) * tt + F41) * tt + F40, // mean elongation of the Moon from the Sun
                                  (((F54 * tt + F53) * tt + F52) * tt + F51) * tt + F50, // mean longitude of the ascending node of the Moon
                                  F61  * tt +  F60, // mean Mercury longitude
                                  F71  * tt +  F70, // mean Venus longitude
                                  F81  * tt +  F80, // mean Earth longitude
                                  F91  * tt +  F90, // mean Mars longitude
                                  F101 * tt + F100, // mean Jupiter longitude
                                  F111 * tt + F110, // mean Saturn longitude
                                  F121 * tt + F120, // mean Uranus longitude
                                  F131 * tt + F130, // mean Neptune longitude
                                  (F142 * tt + F141) * tt); // general accumulated precession in longitude
    }

    /** Compute precession and nutation effects.
     * @param t offset from J2000.0 epoch in julian centuries
     * @param elements luni-solar and planetary elements for the current date
     * @return precession and nutation rotation
     */
    public Rotation precessionNutationEffect(final double t, final BodiesElements elements) {

        // pole position
        final double x =    xDevelopment.value(t, elements);
        final double y =    yDevelopment.value(t, elements);
        final double s = sxy2Development.value(t, elements) - x * y / 2;

        final double x2 = x * x;
        final double y2 = y * y;
        final double r2 = x2 + y2;
        final double e = Math.atan2(y, x);
        final double d = Math.acos(Math.sqrt(1 - r2));
        final Rotation rpS = new Rotation(Vector3D.PLUS_K, -s);
        final Rotation rpE = new Rotation(Vector3D.PLUS_K, -e);
        final Rotation rmD = new Rotation(Vector3D.PLUS_J, +d);

        // combine the 4 rotations (rpE is used twice)
        // IERS conventions (2003), section 5.3, equation 6
        return rpE.applyInverseTo(rmD.applyTo(rpE.applyTo(rpS)));

    }

}
