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
package org.orekit.propagation.events;

import org.apache.commons.math.geometry.Vector3D;
import org.orekit.errors.OrekitException;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.utils.PVCoordinates;

/** Finder for apside crossing events.
 * <p>This class finds apside crossing events (i.e. apogee or perigee crossing).</p>
 * <p>The default implementation behavior is to {@link
 * EventDetector#CONTINUE continue} propagation at apogee crossing
 * and to {@link EventDetector#STOP stop} propagation
 * at perigee crossing. This can be changed by overriding the
 * {@link #eventOccurred(SpacecraftState) eventOccurred} method in a
 * derived class.</p>
 * <p>Beware that apside detection will fail for almost circular orbits. If
 * for example an apside detector is used to trigger an {@link
 * org.orekit.forces.maneuvers.ImpulseManeuver ImpulseManeuver} and the maneuver
 * change the orbit shape to circular, then the detector may completely fail just
 * after the maneuver has been performed!</p>
 * @see org.orekit.propagation.Propagator#addEventDetector(EventDetector)
 * @author Luc Maisonobe
 * @version $Revision: 1752 $ $Date: 2008-06-24 16:57:58 +0200 (mar 24 jun 2008) $
 */
public class ApsideDetector extends AbstractDetector {

    /** Serializable UID. */
    private static final long serialVersionUID = 106899523973703627L;

    /** Build a new instance.
     * <p>The orbit is used only to set an upper bound for the
     * max check interval to period/3 and to set the convergence
     * threshold according to orbit size</p>
     * @param orbit initial orbit
     */
    public ApsideDetector(final Orbit orbit) {
        super(orbit.getKeplerianPeriod() / 3,
              1.0e-13 * Math.sqrt(orbit.getMu() * orbit.getA()));
    }

    /** Handle an apside crossing event and choose what to do next.
     * <p>The default implementation behavior is to {@link
     * EventDetector#CONTINUE continue} propagation at apogee
     * crossing and to {@link EventDetector#STOP stop} propagation
     * at perigee crossing. This can be changed by overriding the
     * {@link #eventOccurred(SpacecraftState) eventOccurred} method in a
     * derived class.</p>
     * @param s the current state information : date, kinematics, attitude
     * @return one of {@link #STOP}, {@link #RESET_STATE}, {@link #RESET_DERIVATIVES}
     * or {@link #CONTINUE}
     * @exception OrekitException if some specific error occurs
     */
    public int eventOccurred(final SpacecraftState s) throws OrekitException {
        final double r = s.getPVCoordinates().getPosition().getNorm();
        return (r > s.getA()) ? CONTINUE : STOP;
    }

    /** {@inheritDoc} */
    public double g(final SpacecraftState s) throws OrekitException {
        final PVCoordinates pv = s.getPVCoordinates();
        return Vector3D.dotProduct(pv.getPosition(), pv.getVelocity());
    }

}
