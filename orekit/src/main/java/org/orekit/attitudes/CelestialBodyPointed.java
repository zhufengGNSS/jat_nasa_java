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
package org.orekit.attitudes;

import org.apache.commons.math.geometry.Rotation;
import org.apache.commons.math.geometry.Vector3D;
import org.orekit.bodies.CelestialBody;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.Transform;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;


/**
 * This class handles a celestial body pointed attitude law.
 * <p>The celestial body pointed law is defined by two main elements:
 * <ul>
 *   <li>a celestial body towards which some satellite axis is exactly aimed</li>
 *   <li>a phasing reference defining the rotation around the pointing axis</li>
 * </ul>
 * </p>
 * <p>
 * The celestial body implicitly defines two of the three degrees of freedom
 * and the phasing reference defines the last degree of freedom. This definition
 * can be represented as first aligning exactly the satellite pointing axis to
 * the current direction of the celestial body, and then to find the rotation
 * around this axis such that the satellite phasing axis is in the half-plane
 * defined by a cut line on the pointing axis and containing the celestial
 * phasing reference.
 * </p>
 * <p>
 * In order for this definition to work, the user must ensure that the phasing
 * references are <strong>never</strong> aligned with the pointing references.
 * Since the pointed body moves as the date changes, this should be ensured
 * regardless of the date. A simple way to do this for Sun, Moon or any planet
 * pointing is to choose a phasing reference far from the ecliptic plane. Using
 * {@link org.apache.commons.math.geometry.Vector3D.PLUS_K Vector3D.PLUS_K},
 * the equatorial pole, is perfect in these cases.
 * </p>
 * <p>Instances of this class are guaranteed to be immutable.</p>
 * @author Luc Maisonobe
 * @version $Revision: 1740 $ $Date: 2008-06-24 13:45:14 +0200 (mar 24 jun 2008) $
 */
public class CelestialBodyPointed implements AttitudeLaw {

    /** Serializable UID. */
    private static final long serialVersionUID = 6222161082155807729L;

    /** Step size for estimating body motion (seconds). */
    private static final double STEP_SIZE = 0.1;

    /** Frame in which {@link #pointedBody} and {@link #phasingCel} are defined. */
    private final Frame celestialFrame;

    /** Celestial body to point at. */
    private final CelestialBody pointedBody;

    /** Phasing reference, in celestial frame. */
    private final Vector3D phasingCel;

    /** Satellite axis aiming at the pointed body, in satellite frame. */
    private final Vector3D pointingSat;

    /** Phasing reference, in satellite frame. */
    private final Vector3D phasingSat;

    /** Creates new instance.
     * @param celestialFrame frame in which <code>pointedBody</code>
     * and <code>phasingCel</code> are defined
     * @param pointedBody celestial body to point at
     * @param phasingCel phasing reference, in celestial frame
     * @param pointingSat satellite vector defining the pointing direction
     * @param phasingSat phasing reference, in satellite frame
     */
    public CelestialBodyPointed(final Frame celestialFrame,
                                final CelestialBody pointedBody,
                                final Vector3D phasingCel,
                                final Vector3D pointingSat,
                                final Vector3D phasingSat) {
        this.celestialFrame = celestialFrame;
        this.pointedBody    = pointedBody;
        this.phasingCel     = phasingCel;
        this.pointingSat    = pointingSat;
        this.phasingSat     = phasingSat;
    }

    /** {@inheritDoc} */
    public Attitude getState(final AbsoluteDate date,
                             final PVCoordinates pv, final Frame frame)
        throws OrekitException {

        // compute celestial references at the specified date
        final Vector3D body0     = pointedBody.getPosition(date, celestialFrame);
        final Vector3D sat0      = pv.getPosition();
        final Vector3D sat0Cel   = frame.getTransformTo(celestialFrame, date).transformPosition(sat0);
        final Vector3D pointing0 = body0.subtract(sat0Cel);
        final double r2 = Vector3D.dotProduct(pointing0, pointing0);

        // compute celestial references a few seconds after specified date
        final AbsoluteDate date1 = new AbsoluteDate(date, STEP_SIZE);
        final Vector3D body1     = pointedBody.getPosition(date1, celestialFrame);
        final Vector3D sat1      = pv.getPosition().add(STEP_SIZE, pv.getVelocity());
        final Vector3D sat1Cel   = frame.getTransformTo(celestialFrame, date1).transformPosition(sat1);
        final Vector3D pointing1 = body1.subtract(sat1Cel);

        // evaluate instant rotation axis by finite differences
        // note that despite we use forward difference and not centered differences,
        // the error in this estimation is O(h^2) for Sun since because Earth-Body
        // acceleration is in both case colinear to Earth-Body vector, so acceleration
        // contribution is nullified by the cross product.
        final Vector3D rotAxisCel =
            new Vector3D(1 / (r2 * STEP_SIZE), Vector3D.crossProduct(pointing0, pointing1));

        // compute transform from celestial frame to satellite frame
        final Rotation celToSatRotation =
            new Rotation(pointing0, phasingCel, pointingSat, phasingSat);
        final Vector3D celToSatSpin = celToSatRotation.applyTo(rotAxisCel);
        Transform transform = new Transform(celToSatRotation, celToSatSpin);

        if (frame != celestialFrame) {
            // prepend transform from specified frame to celestial frame
            transform = new Transform(frame.getTransformTo(celestialFrame, date), transform);
        }

        // build the attitude
        return new Attitude(frame, transform.getRotation(), transform.getRotationRate());

    }

}
