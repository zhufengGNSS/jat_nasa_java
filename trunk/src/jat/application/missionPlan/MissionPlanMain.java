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

package jat.application.missionPlan;

import jat.core.util.messageConsole.MessageConsole;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class MissionPlanMain extends JApplet {

	private static final long serialVersionUID = 1L;
	// public MissionPlanGUI mpGUI;
	// public MissionPlanPlot mpPlot;
	// public MissionPlanParameters mpParam;
	static int appletwidth = 900; // Width of Applet
	static int appletheight = 700;
	static com.tornadolabs.j3dtree.Java3dTree m_Java3dTree = null;
	private static boolean Java3dTree_debug = false;
	Container level1_Pane;
	JFrame sFrame;
	List<Flight> flightList = new ArrayList<Flight>();

	public void init() {

		getContentPane().setLayout(new BorderLayout(0, 0));

		// Create a text pane.
		JTextPane textPane = new JTextPane();
		JScrollPane paneScrollPane = new JScrollPane(textPane);
		paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(250, 155));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));

		// JTextPane txtpnTest = new JTextPane();
		// txtpnTest.setText("test");
		getContentPane().add(paneScrollPane, BorderLayout.CENTER);

		// MessageFrame mf = new MessageFrame();
		// mf.setVisible(true);

		// MessageConsole mc = new MessageConsole(mf.textArea);
		MessageConsole mc = new MessageConsole(textPane);

		JScrollPane scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.SOUTH);
		mc.redirectOut();
		mc.redirectErr(Color.RED, null);
		mc.setMessageLines(100);

		System.out.println("JAT missionPlanMain started");
		System.out.println("redirected output");

		boolean later = false;
		if (later) {
			// mpGUI = new MissionPlanGUI(this);
			// mpPlot = new MissionPlanPlot(this);
			// mpParam = new MissionPlanParameters();
			level1_Pane = getContentPane();
			// level1_Pane.add(mpGUI, BorderLayout.WEST);
			// level1_Pane.add(mpPlot, BorderLayout.CENTER);
		}
	}

	public void start() {
		System.out.println("Init complete");
		System.out.println("Attempting to create MissionPlanApplet");

		// 1. Create the frame.
		JFrame frame = new JFrame("FrameDemo");
		frame.setSize(600, 400);
		// 2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 3. Create components and put them in the frame.
		// ...create emptyLabel...
		missionPlanApplet mpApplet = new missionPlanApplet();
		mpApplet.init();
		frame.getContentPane().add(mpApplet, BorderLayout.CENTER);

		// 4. Size the frame.
		frame.pack();

		// 5. Show it.
		frame.setVisible(true);

		// mpApplet.setVisible(true);

		System.out.println("");
	}

	void loadTheApplet() {
	}

	/**
	 * Used when run as an application
	 */
	public static void main(String[] args) {
		// MissionPlanMain mApplet = new MissionPlanMain();
		// mApplet.init();
		//
		// mApplet.sFrame = new JFrame();
		// mApplet.sFrame.setTitle("JAT Solar System Mission Planner");
		// mApplet.sFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//
		// // When running this file as a stand-alone app, add the applet to
		// // the frame.
		// mApplet.sFrame.getContentPane().add(mApplet, BorderLayout.CENTER);
		// mApplet.sFrame.setSize(appletwidth, appletheight);
		// mApplet.sFrame.setVisible(true);
		//
		// // sApplet.ssp.mouseZoom.setupCallback(sApplet.ssE);
		// mApplet.mpPlot.requestFocusInWindow();
		//
		// mApplet.mpGUI.mpE.timer.start();

		// if (Java3dTree_debug) {
		// m_Java3dTree = new com.tornadolabs.j3dtree.Java3dTree();
		// m_Java3dTree.recursiveApplyCapability(mApplet.mpPlot.jatScene);
		// m_Java3dTree.updateNodes(mApplet.mpPlot.universe);
		// }
	}// End of main()

}