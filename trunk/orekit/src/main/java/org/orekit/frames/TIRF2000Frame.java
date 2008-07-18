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
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TTScale;
import org.orekit.time.UTCScale;

/** Terrestrial Intermediate Reference Frame 2000.
 * <p> The pole motion is not considered : Pseudo Earth Fixed Frame. It handles
 * the earth rotation angle, its parent frame is the {@link IRF2000Frame}</p>
 * @version $Revision: 1726 $ $Date: 2008-06-20 11:18:17 +0200 (ven 20 jun 2008) $
 */
class TIRF2000Frame extends Frame {

    /** Serializable UID. */
    private static final long serialVersionUID = 5098003225016267874L;

    /** 2&pi;. */
    private static final double TWO_PI = 2.0 * Math.PI;

    /** Constant term of Capitaine's Earth Rotation Angle model. */
    private static final double ERA_0 = TWO_PI * 0.7790572732640;

    /** Offset between J2000.0 epoch and Java epoch in seconds. */
    private static final double J2000_MINUS_JAVA =
        AbsoluteDate.J2000_EPOCH.minus(AbsoluteDate.JAVA_EPOCH);

    /** Rate term of Capitaine's Earth Rotation Angle model.
     * (radians per day, main part) */
    private static final double ERA_1A = TWO_PI;

    /** Rate term of Capitaine's Earth Rotation Angle model.
     * (radians per day, fractional part) */
    private static final double ERA_1B = ERA_1A * 0.00273781191135448;

    /** Cached date to avoid useless calculus. */
    private AbsoluteDate cachedDate;

    /** Earth Rotation Angle, in radians. */
    private double era;

    /** Constructor for the singleton.
     * @param parent the IRF2000
     * @param date the current date
     * @param name the string reprensentation
     * @exception OrekitException if nutation cannot be computed
     */
    protected TIRF2000Frame(final Frame parent, final AbsoluteDate date, final String name)
        throws OrekitException {
        super(parent, null, name);
        // everything is in place, we can now synchronize the frame
        updateFrame(date);
    }

    /** Update the frame to the given date.
     * <p>The update considers the earth rotation from IERS data.</p>
     * @param date new value of the date
     * @exception OrekitException if the nutation model data embedded in the
     * library cannot be read
     */
    protected void updateFrame(final AbsoluteDate date) throws OrekitException {

        if ((cachedDate == null) || !cachedDate.equals(date)) {

            //    offset from J2000 epoch in julian centuries
            final double tts = date.minus(AbsoluteDate.J2000_EPOCH);

            // compute Earth Rotation Angle using Nicole Capitaine model (2000)
            final double dtu1 = EarthOrientationHistory.getInstance().getUT1MinusUTC(date);
            final double taiMinusTt  = TTScale.getInstance().offsetToTAI(tts + J2000_MINUS_JAVA);
            final double utcMinusTai = UTCScale.getInstance().offsetFromTAI(tts + taiMinusTt + J2000_MINUS_JAVA);
            final double tu = (tts + taiMinusTt + utcMinusTai + dtu1) / 86400.0;
            era  = ERA_0 + ERA_1A * tu + ERA_1B * tu;
            era -= TWO_PI * Math.floor((era + Math.PI) / TWO_PI);

            // simple rotation around the Celestial Intermediate Pole
            final Rotation rRot = new Rotation(Vector3D.PLUS_K, era);

            final Rotation combined = rRot.revert();

            // set up the transform from parent GCRS (J2000) to ITRF
            final Vector3D rotationRate = new Vector3D((ERA_1A + ERA_1B) / 86400, Vector3D.PLUS_K);
            setTransform(new Transform(combined , rotationRate));
            cachedDate = date;
        }
    }

    /** Get the Earth Rotation Angle at the current date.
     * @param  date the date
     * @return Earth Rotation Angle at the current date in radians
     * @exception OrekitException if nutation model cannot be computed
     */
    public double getEarthRotationAngle(final AbsoluteDate date) throws OrekitException {
        updateFrame(date);
        return era;
    }

}
