/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package gp404;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A plot of one or more sequences.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.08.19
 */
public class SequencePlot {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a plot for one sequence.
   * @param l1 a sequence label.
   * @param s1 a sequence.
   */
  public SequencePlot(String l1, Sequence s1) {
    this(new String[]{l1},
         new Sequence[]{s1});
  }

  /**
   * Constructs a plot for multiple sequences.
   * @param al array of sequence labels.
   * @param as array of sequences.
   */
  public SequencePlot(String[] al, Sequence[] as) {
    makeFrame(al,as);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private PlotFrame _frame;
  private PlotPanel _panel;

  private void makeFrame(final String[] al, final Sequence[] as) {
    Check.argument(al.length==as.length,"al.length==as.length");
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        int ns = as.length;
        _panel = new PlotPanel(ns,1);
        _panel.setHLabel("time");
        for (int is=0; is<ns; ++is) {
          Sequence s = as[is];
          String l = al[is];
          _panel.setVLabel(is,l);
          _panel.addSequence(is,0,s.getSampling(),s.getValues());
        }
        _frame = new PlotFrame(_panel);
        addButtons();
        _frame.setSize(950,250*as.length);
        _frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
        _frame.setVisible(true);
      }
    });
  }

  private void addButtons() {
    Action saveToPngAction = new AbstractAction("Save to PNG") {
      private static final long serialVersionUID = 1L;
      public void actionPerformed(ActionEvent event) {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        fc.showSaveDialog(_frame);
        File file = fc.getSelectedFile();
        if (file!=null) {
          String filename = file.getAbsolutePath();
          _frame.paintToPng(300,6,filename);
        }
      }
    };
    JButton saveToPngButton = new JButton(saveToPngAction);
    JToolBar toolBar = new JToolBar();
    toolBar.add(saveToPngButton);
    _frame.add(toolBar,BorderLayout.NORTH);
  }
}