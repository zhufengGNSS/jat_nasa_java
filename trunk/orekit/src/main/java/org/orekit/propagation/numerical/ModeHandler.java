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
package org.orekit.propagation.numerical;

import org.orekit.attitudes.AttitudeLaw;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;

/** Common interface for all propagator mode handlers initialization.
 * @author Luc Maisonobe
 * @version $Revision: 1728 $ $Date: 2008-06-23 09:52:11 +0200 (lun 23 jun 2008) $
 */
public interface ModeHandler {

    /** Initialize the mode handler.
     * @param reference reference date
     * @param frame reference frame
     * @param mu central body attraction coefficient
     * @param attitudeLaw attitude law
     */
    void initialize(AbsoluteDate reference, Frame frame, double mu, AttitudeLaw attitudeLaw);

}
