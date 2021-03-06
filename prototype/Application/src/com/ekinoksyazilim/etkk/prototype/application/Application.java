package com.ekinoksyazilim.etkk.prototype.application;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.osgi.framework.BundleContext;

import com.ekinoksyazilim.etkk.prototype.configuration.Configuration;
import com.ekinoksyazilim.etkk.prototype.dummy.DummyWork;
import com.ekinoksyazilim.etkk.prototype.tools.ResourceBundleReplacer;
import com.ekinoksyazilim.etkk.prototype.top.ITop;

public class Application {

	private ITop top;
	
	private Integer[] inputs;
	
	private MainWindow mainWindow;
	
	private Configuration configuration;
	
	public void activate(BundleContext bc) {

		ResourceBundleReplacer.replace(bc);
		
		mainWindow = new MainWindow();
		mainWindow.setSize(500, 500);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
		
		mainWindow.getBtnGenerate().addActionListener((e) -> generate());
		mainWindow.getBtnCalculate1().addActionListener((e) -> calculate1());
		mainWindow.getBtnCalculate2().addActionListener((e) -> calculate2());
		
	}
	
	public void setTop(ITop top) {
		
		this.top = top;
	}
	
	private void generate() {
		
		mainWindow.setEnabled(false);
		
		CompletableFuture.runAsync(() -> {
			
			System.out.println("Generating at thread: " + Thread.currentThread().getId());
			
			DummyWork.doDummy();
			
			int inputCount = (int) (Math.random() * 1000);
			
			this.inputs = new Integer[inputCount];
			
			for(int i = 0; i < inputCount; i++) {
				
				inputs[i] = (int) (Math.random() * 100);
			}

			SwingUtilities.invokeLater(() -> {
				
				mainWindow.getTextArea().setText("");
				
				Arrays.asList(inputs).forEach(input -> mainWindow.getTextArea().append(input + "\n"));
				
				mainWindow.setEnabled(true);
				
			});
		});
	}
	
	private void calculate1() {
		
		mainWindow.setEnabled(false);
		
		top.sum(inputs).thenAccept(this::onCalculateComplete);
	}
	
	private void calculate2() {
		
		mainWindow.setEnabled(false);
		
		top.sum(inputs, result -> onCalculateComplete(result));
	}
	
	private void onCalculateComplete(Integer result) {
		
		if(result == null) {
			
			JOptionPane.showMessageDialog(mainWindow, "Calculation FAILED!");
			
		} else {
			
			JOptionPane.showMessageDialog(mainWindow, "Calculation completed: " + result);
		}
		
		SwingUtilities.invokeLater(() -> {
			
			mainWindow.setEnabled(true);
			
		});
	}
	
	public void setConfiguration(Configuration configuration) {
		
		this.configuration = configuration;
	}
}
