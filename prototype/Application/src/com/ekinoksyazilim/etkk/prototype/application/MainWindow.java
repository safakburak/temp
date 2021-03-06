package com.ekinoksyazilim.etkk.prototype.application;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	private JButton btnGenerate;
	private JButton btnCalculate1;
	private JButton btnCalculate2;
	private JTextArea textArea;

	public MainWindow() {
		
		JPanel pnlButtons = new JPanel();
		getContentPane().add(pnlButtons, BorderLayout.WEST);
		GridBagLayout gbl_pnlButtons = new GridBagLayout();
		gbl_pnlButtons.columnWidths = new int[]{0, 0};
		gbl_pnlButtons.rowHeights = new int[]{0, 0, 0, 0};
		gbl_pnlButtons.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_pnlButtons.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlButtons.setLayout(gbl_pnlButtons);
		
		btnGenerate = new JButton(Messages.getString("MainWindow.btnGenerate.text")); //$NON-NLS-1$
		btnGenerate.setPreferredSize(new Dimension(120, 29));
		GridBagConstraints gbc_btnGenerate = new GridBagConstraints();
		gbc_btnGenerate.insets = new Insets(0, 0, 5, 0);
		gbc_btnGenerate.gridx = 0;
		gbc_btnGenerate.gridy = 0;
		pnlButtons.add(btnGenerate, gbc_btnGenerate);
		
		btnCalculate1 = new JButton(Messages.getString("MainWindow.btnCalculate1.text")); //$NON-NLS-1$
		btnCalculate1.setPreferredSize(new Dimension(120, 29));
		GridBagConstraints gbc_btnCalculate1 = new GridBagConstraints();
		gbc_btnCalculate1.insets = new Insets(0, 0, 5, 0);
		gbc_btnCalculate1.gridx = 0;
		gbc_btnCalculate1.gridy = 1;
		pnlButtons.add(btnCalculate1, gbc_btnCalculate1);
		
		btnCalculate2 = new JButton(Messages.getString("MainWindow.btnCalculate2.text")); //$NON-NLS-1$
		btnCalculate2.setPreferredSize(new Dimension(120, 29));
		GridBagConstraints gbc_btnCalculate2 = new GridBagConstraints();
		gbc_btnCalculate2.gridx = 0;
		gbc_btnCalculate2.gridy = 2;
		pnlButtons.add(btnCalculate2, gbc_btnCalculate2);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
	}

	public JButton getBtnCalculate1() {
		
		return btnCalculate1;
	}
	
	public JButton getBtnCalculate2() {
		
		return btnCalculate2;
	}
	
	public JButton getBtnGenerate() {
		
		return btnGenerate;
	}
	
	public JTextArea getTextArea() {
		
		return textArea;
	}
	
	public void setEnabled(boolean enabled) {
		
		btnGenerate.setEnabled(enabled);
		btnCalculate1.setEnabled(enabled);
		btnCalculate2.setEnabled(enabled);
		textArea.setEnabled(enabled);
	}
}
