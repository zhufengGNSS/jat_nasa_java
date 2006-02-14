/* JAT: Java Astrodynamics Toolkit
 *
 * Copyright (c) 2002 The JAT Project and the Center for Space Research (CSR),
 * The University of Texas at Austin. All rights reserved.
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
 /*
 *  File        :   ephemeris.java
 *  Author      :   Tobias Berthold
 *  Date        :   10-9-2002
 *  Change      :
 *  Description :   main program, JAT ephemeris demo
 */

package jat.demo.ephemeris;

import jat.cm.*;
import jat.util.*;
import jat.eph.*;

/**
 * @author Tobias Berthold
 * @version 1.0
 */
public class ephemeris
{
    public static void main (String argv[])
    {
        double jd=cm.juliandate(2002, 2, 17, 12, 0, 0);
		String fs = FileUtil.file_separator();
		DE405 my_eph = new DE405(FileUtil.getClassFilePath("jat.eph","DE405")+fs+"DE405data"+fs);
        double[] rv=my_eph.get_planet_posvel(DE405.MARS, jd);

        System.out.println("The position of Mars on 10-17-2002 at 12:00pm is ");
        System.out.println("x= "+rv[0]+" km");
        System.out.println("y= "+rv[1]+" km");
        System.out.println("z= "+rv[2]+" km");


    }
}
