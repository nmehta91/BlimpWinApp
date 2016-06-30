import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JMenu;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.InputEvent;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow {

	private JFrame frame;
	private JTextArea syntaxEditor;
	private JScrollPane scrollBar;
	private JFileChooser selectedFile;
	private File currentFile = null;
	private ImportWindow importWin = null;
	private ModelMCOutput ModelMCOutputWindow = null;
	private SyntaxModel model;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Untitled");
		frame.setBounds(100, 100, 886, 537);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		model = SyntaxModel.getInstance();
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewSyntax = new JMenuItem("New Syntax");
		mntmNewSyntax.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newSyntax();
			}
		});
		mntmNewSyntax.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNewSyntax);
		
		JMenuItem mntmOpenFile = new JMenuItem("Open File...");
		// TODO : Move instantiation of selectedFile somewhere else
		selectedFile = new JFileChooser();
		mntmOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(syntaxEditor.getText().isEmpty()) {
					int openResult = selectedFile.showOpenDialog(null);
					if (openResult == selectedFile.APPROVE_OPTION) {
						openFile(selectedFile.getSelectedFile(), 0);
					}
				}
				else {
					if(currentFile == null){
						int saveResult = selectedFile.showSaveDialog(frame);
						
						if (saveResult == selectedFile.APPROVE_OPTION) {
							saveFile(selectedFile.getSelectedFile(), syntaxEditor.getText());
							
						}
					}
					else {
						saveFile(currentFile, syntaxEditor.getText());
					}
					int openResult = selectedFile.showOpenDialog(null);
					if (openResult == selectedFile.APPROVE_OPTION) {
						openFile(selectedFile.getSelectedFile(), 0);
					}
				}
		}
		});
		mntmOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpenFile);
		
		JMenuItem mntmSave = new JMenuItem("Save...");
		mntmSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int saveResult = selectedFile.showSaveDialog(frame);
				
				if (saveResult == selectedFile.APPROVE_OPTION) {
					saveFile(selectedFile.getSelectedFile(), syntaxEditor.getText());
					
				}
			}
		});
		mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSave);
		
		JMenuItem mntmSaveAs = new JMenuItem("Save As...");
		mntmSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int saveResult = selectedFile.showSaveDialog(frame);
				saveFile(selectedFile.getSelectedFile(), syntaxEditor.getText());
				
			}
		});
		mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mntmSaveAs);
		
		JMenuItem mntmImportDataset = new JMenuItem("Import Dataset");
		mntmImportDataset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Path importedFile = importSelectedFile();
				System.out.println(importedFile.toString());
				//model.mappings.put("data", importedFile.toString());
				model.dataSetPath = importedFile;
				importWin = new ImportWindow();
				importWin.showWindow();
				ModelMCOutputWindow = new ModelMCOutput(0);
			}
		});
		mntmImportDataset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
		mnFile.add(mntmImportDataset);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeWindow();
			}
		});
		
		JMenuItem mntmTest = new JMenuItem("Test");
		mntmTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		mnFile.add(mntmTest);
		mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFile.add(mntmClose);
		
		JMenu mnImpute = new JMenu("Impute");
		menuBar.add(mnImpute);
		
		JMenuItem mntmSpecifyModel = new JMenuItem("Specify Model");
		mntmSpecifyModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(ModelMCOutputWindow == null){
					ModelMCOutputWindow = new ModelMCOutput(0);
				}
				
				ModelMCOutputWindow.setVisible(true);
				
			}
		});
		mntmSpecifyModel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
		mnImpute.add(mntmSpecifyModel);
		
		JMenuItem mntmMcmcOptions = new JMenuItem("MCMC Options");
		mntmMcmcOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ModelMCOutputWindow = new ModelMCOutput(1);
				ModelMCOutputWindow.setVisible(true);
				
			}
		});
		mntmMcmcOptions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnImpute.add(mntmMcmcOptions);
		
		JMenuItem mntmOutputOptions = new JMenuItem("Output Options");
		mntmOutputOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ModelMCOutputWindow = new ModelMCOutput(2);
				ModelMCOutputWindow.setVisible(true);
			}
		});
		mntmOutputOptions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnImpute.add(mntmOutputOptions);
		
		syntaxEditor = new JTextArea(5, 30);
		syntaxEditor.setLineWrap(true);
		syntaxEditor.setWrapStyleWord(true);
		scrollBar = new JScrollPane(syntaxEditor);
		frame.getContentPane().add(scrollBar, BorderLayout.CENTER);

	}
	
	public void openFile(File file, int flag) {
		if (file.canRead()) {
			String filePath = file.getPath();
			String fileContents = "";
			int noOfLinesToRead;
			
			if(filePath.endsWith(".dat") || filePath.endsWith(".txt") || filePath.endsWith(".csv")) {
				try {
					model.importFileContents = new ArrayList<String>();
					Scanner scan = new Scanner(new FileInputStream(file));
					if(flag == 1) { // When a data import file is to be read, read only 10 lines
						noOfLinesToRead = 10;
						while(scan.hasNextLine() && noOfLinesToRead > 0){
							String line = scan.nextLine();
							fileContents += line;
							model.importFileContents.add(line);
							System.out.println("Added to fileContents: " + line);
							fileContents += "\n";
							noOfLinesToRead--;
						}
						model.importFileContentsInString = fileContents;
						System.out.println(fileContents);
					}
					else {	// When a syntax file is to be read, read the entire file and show in the syntax editor
					
					while(scan.hasNextLine()){
						fileContents += scan.nextLine();
						fileContents += "\n";
					}
					syntaxEditor.setText(fileContents);
					frame.setTitle(filePath);
					currentFile = file;
				  }
				} catch (FileNotFoundException e) {
					System.out.println("File Not Found Exception raised!");
				}
			} 
			else {
			JOptionPane.showMessageDialog(null, "That file type is not supported!\n Only .txt file type is supported!");
			}
		} 
		else {
		JOptionPane.showMessageDialog(null, "File could not be opened!");
		}
	}
	
	public void saveFile(File file, String contents) {
		BufferedWriter writer = null;
		String filePath = file.getPath();
		if(!filePath.endsWith(".txt")) {
			filePath += ".txt";
		}
		
		try {
			writer = new BufferedWriter(new FileWriter(filePath));
			writer.write(contents);
			writer.close();
			syntaxEditor.setText(contents);
			frame.setTitle(filePath);
			currentFile = file;
		} catch (Exception e) {
			System.out.println("Exception while trying to save the file!");
		}
	}
	
	
	/* @abstract: This function opens a new syntax whiteboard after saving the existing content on the textArea */
	public void newSyntax(){
		if(syntaxEditor.getText().isEmpty()){
			
		}
		else {
			if(currentFile == null){
				int saveResult = selectedFile.showSaveDialog(frame);
				
				if (saveResult == selectedFile.APPROVE_OPTION) {
					saveFile(selectedFile.getSelectedFile(), syntaxEditor.getText());
					
				}
			}
			else {
				saveFile(currentFile, syntaxEditor.getText());
			}
		}
		
		syntaxEditor.setText("");
		frame.setTitle("Untitled");
		
	}
	public void closeWindow() {
		WindowEvent close = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(close);
	}
	
	public Path importSelectedFile() {
		int openResult = selectedFile.showOpenDialog(null);
		if (openResult == JFileChooser.APPROVE_OPTION) {
			openFile(selectedFile.getSelectedFile(), 1);
		}
		return selectedFile.getSelectedFile().toPath();
	}
}
