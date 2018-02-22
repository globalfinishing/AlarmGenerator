/*******************************************************************************

    Alarm Generator

    Copyright (C) 2018 Global Finishing Solutions, LLC.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.

*******************************************************************************/
package com.globalfinishing.alarmgenerator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Super simple GUI to make life easier
 *
 */
public class GUI {

    private final JFrame frame;
    private final JTextField txtInput;
    private final JTextField txtOutput;
    private final JTextArea textArea;

    public static void run() {
        // Schedule the GUI to be constructed on the event thread
        SwingUtilities.invokeLater(GUI::new);
    }

    /**
     * Make a new GUI
     */
    public GUI() {
        // Create GUI components
        frame = new JFrame(Util.getApplicationTitle());
        JLabel lblInput = new JLabel("Input File:");
        JLabel lblOutput = new JLabel("Output File:");
        txtInput = new JTextField();
        txtOutput = new JTextField();
        JButton btnInput = new JButton("...");
        JButton btnOutput = new JButton("...");
        textArea = new JTextArea(25, 80);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JButton btnGenerate = new JButton("Generate");

        // Setup events
        btnInput.addActionListener(this::onInputClk);
        btnOutput.addActionListener(this::onOutputClk);
        btnGenerate.addActionListener(this::onGenerateClk);
        Log.setListener((s) -> textArea.append(s));
        
        // Setup layout
        //
        // |----------------------------------------------------------------------------|
        // | [ lblInput ] | [ Text Field                              ] | [ btnInput  ] |
        // |----------------------------------------------------------------------------|
        // | [ lblOutput] | [ Text Field                              ] | [ btnOutput ] |
        // |----------------------------------------------------------------------------|
        // |----------------------------------------------------------------------------|
        // | [                                                                        ] |
        // | [                                                                        ] |
        // | [                                                                        ] |
        // | [                                                                        ] |
        // | [                            textArea/scrollPane                         ] |
        // | [                                                                        ] |
        // | [                                                                        ] |
        // | [                                                                        ] |
        // | [                                                                        ] |
        // |----------------------------------------------------------------------------|
        // |                                    [btnGenerate]                           |
        // |----------------------------------------------------------------------------|
        GroupLayout layout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lblInput)
                                .addComponent(lblOutput))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(txtInput)
                                .addComponent(txtOutput))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(btnInput)
                                .addComponent(btnOutput)))
                .addComponent(scrollPane)
                .addComponent(btnGenerate)
        );
        
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblInput)
                    .addComponent(txtInput)
                    .addComponent(btnInput))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOutput)
                    .addComponent(txtOutput)
                    .addComponent(btnOutput))
            .addComponent(scrollPane)
            .addComponent(btnGenerate)
        );
        
        // Finish up and show window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(512, 128));
        frame.pack();
        frame.setVisible(true);
        
    }
    
    /**
     * Handle the input file selector button
     * 
     * @param e
     *            Event
     */
    public void onInputClk(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("L5X", "L5X"));
        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            txtInput.setText(fc.getSelectedFile().getPath());
        }
    }

    /**
     * Handle the output file selector button
     * 
     * @param e
     *            Event
     */
    public void onOutputClk(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("XML", "xml"));
        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            txtOutput.setText(fc.getSelectedFile().getPath());
        }
    }
    
    /**
     * Generate the alarms
     * 
     * @param e
     */
    public void onGenerateClk(ActionEvent e) {
        textArea.setText(null);
        File inputFile = new File(txtInput.getText());
        File outputFile = new File(txtOutput.getText());

        if (!inputFile.canRead() || !inputFile.isFile()) {
            JOptionPane.showMessageDialog(frame, "Invalid Input File.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (outputFile.isDirectory()) {
            JOptionPane.showMessageDialog(frame, "Output cannot be a directory.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (outputFile.exists()) {
            int n = JOptionPane.showConfirmDialog(frame,
                    "The output file already exists.\n" + "Do you want to overwrite the file?", "Overwrite?",
                    JOptionPane.YES_NO_OPTION);
            if (n != JOptionPane.YES_OPTION) {
                return;
            }

        }

        try {
            Generator g = new Generator();
            g.addL5X("PLC1", inputFile.getPath());
            g.setXMLfile(outputFile.getPath());
            g.run();
        } catch (Exception ex) {
            Log.severe(Util.printStackTrace(ex));
            JOptionPane.showMessageDialog(frame, "Something bad happened.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(frame, "Generation Successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

}
