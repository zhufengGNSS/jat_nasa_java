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

package jat.application.missionPlanRunLocal;

import jat.application.porkChopPlot.PorkChopPlot_main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class AddFlightDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 6771932082871532590L;
	JButton okButton;
	JButton cancelButton;
	MissionPlanMain ssmain;
	PorkChopPlot_main p;
	public boolean OK_pressed;

	public AddFlightDialog(MissionPlanMain ssmain) {
		super(ssmain.sFrame, true); // modal
		setTitle("Optimal Launch Date Finder");
		this.ssmain = ssmain;
		setBounds(50, 50, 900, 700);
		getContentPane().setLayout(new BorderLayout());
		p = new PorkChopPlot_main();
		p.init();
		p.setVisible(true);
		getContentPane().add(p);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		if (okButton == e.getSource()) {
			OK_pressed=true;
			setVisible(false);
		} else if (cancelButton == e.getSource()) {
			OK_pressed=false;
			setVisible(false);
		}
	}

}