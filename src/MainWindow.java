import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JMenu;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Desktop;

import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.awt.event.InputEvent;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import say.swing.JFontChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainWindow {

	private JFrame frame;
	private JTextArea syntaxEditor;
	private JScrollPane scrollBar;
	private JFileChooser selectedFile;
	private File currentFile = null;
	private ImportWindow importWindow;
	private ModelMCOutput ModelMCOutputWindow = null;
	private SyntaxModel model;
	private RunLogsWindow runWindow = null;
	private int mostRecentHashCode;
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
				
				// If before opening new file, syntax editor is non-empty and needs to be saved
				if(syntaxEditor.getText().isEmpty()) {
					int openResult = selectedFile.showOpenDialog(null);
					if (openResult == selectedFile.APPROVE_OPTION) {
						openFile(selectedFile.getSelectedFile(), 0);
					}
				}
				else {
					// If syntax editor is non-empty, save the existing text on disk
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
				if(currentFile == null){
					int saveResult = selectedFile.showSaveDialog(frame);
					if (saveResult == selectedFile.APPROVE_OPTION) {
						saveFile(selectedFile.getSelectedFile(), syntaxEditor.getText());
						
					}
				} else {
					saveFile(currentFile, syntaxEditor.getText());
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
				SyntaxModel.clearModel();
				Path importedFile = importSelectedFile();

				if(importedFile == null){
					System.out.println("There was an error in importing the file.");
				} else {
					model.dataSetPath = importedFile;
					importWindow = new ImportWindow(frame);
					importWindow.setVisible(true);
					importWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosed(WindowEvent e){
                       writeImportSyntax();
                    }
					});
					ModelMCOutputWindow = new ModelMCOutput(0);
				}
				
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
		mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mnFile.add(mntmClose);
		
		JMenu mnFormat = new JMenu("Format");
		menuBar.add(mnFormat);
		
		JMenuItem mntmFont = new JMenuItem("Font...");
		mntmFont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFontChooser fontChooser = new JFontChooser();
				int result = fontChooser.showDialog(null);
				if (result == JFontChooser.OK_OPTION)
				{
					Font font = fontChooser.getSelectedFont(); 
					syntaxEditor.setFont(font);
				}
			}
		});
		mnFormat.add(mntmFont);
		
		JMenu mnImpute = new JMenu("Impute");
		menuBar.add(mnImpute);
		
		JMenuItem mntmSpecifyModel = new JMenuItem("Specify Model");
		mntmSpecifyModel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(ModelMCOutputWindow == null){
					ModelMCOutputWindow = new ModelMCOutput(0);
				}
				ModelMCOutputWindow.selectTab(0);
				ModelMCOutputWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosed(WindowEvent e){
                        writeModelMCMCOutputSyntax();
                    }
					});
				
				ModelMCOutputWindow.setVisible(true);
				
			}
		});
		mntmSpecifyModel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
		mnImpute.add(mntmSpecifyModel);
		
		JMenuItem mntmMcmcOptions = new JMenuItem("MCMC Options");
		mntmMcmcOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(ModelMCOutputWindow == null) {
					ModelMCOutputWindow = new ModelMCOutput(1);
				}
				ModelMCOutputWindow.selectTab(1);
				ModelMCOutputWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosed(WindowEvent e){
                        // .. get some information from the child before disposing 
                        System.out.println("Window closed."); // does not terminate when passing frame as parent
                        writeModelMCMCOutputSyntax();
                    }
					});
				
				ModelMCOutputWindow.setVisible(true);
				
			}
		});
		mntmMcmcOptions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnImpute.add(mntmMcmcOptions);
		
		JMenuItem mntmOutputOptions = new JMenuItem("Output Options");
		mntmOutputOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ModelMCOutputWindow = new ModelMCOutput(2);
				ModelMCOutputWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosed(WindowEvent e){
                        writeModelMCMCOutputSyntax();
                    }
					});
				
				ModelMCOutputWindow.setVisible(true);
			}
		});
		mntmOutputOptions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnImpute.add(mntmOutputOptions);
		
		JMenuItem mntmRun = new JMenuItem("Run");
		mntmRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(currentFile == null){
					int saveResult = selectedFile.showSaveDialog(frame);
					if (saveResult == selectedFile.APPROVE_OPTION) {
						saveFile(selectedFile.getSelectedFile(), syntaxEditor.getText());
					}
				} else {
					System.out.println("Most recent hashcode: " + mostRecentHashCode);
					System.out.println("Syntax Editor hashcode: "+ syntaxEditor.getText().hashCode());
					if(mostRecentHashCode != syntaxEditor.getText().hashCode()){
						saveFile(currentFile, syntaxEditor.getText());
					}
				}
					
				runWindow = new RunLogsWindow();
				runWindow.setVisible(true);
				runWindow.initiateExecution();
			}
		});
		mntmRun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mnImpute.add(mntmRun);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmBlimpUserManual = new JMenuItem("Blimp User Manual");
		mntmBlimpUserManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (Desktop.isDesktopSupported()) {
		            try {
		                File myFile = new File("Resources\\UserGuide.pdf");
		                Desktop.getDesktop().open(myFile);
		            } catch (IOException ex) {
		                // no application registered for PDFs
		            	JOptionPane.showMessageDialog(frame,
								"No application found to open User Manual (.pdf). Please install and then try again.",
								"Error!",
								JOptionPane.ERROR_MESSAGE);
		            }
		        }
			}
		});
		mntmBlimpUserManual.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
		mnHelp.add(mntmBlimpUserManual);
		
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
					JOptionPane.showMessageDialog(frame,
							"FileNotFoundException raised!",
							"Error!",
							JOptionPane.ERROR_MESSAGE);
				}
			} 
			else {
				JOptionPane.showMessageDialog(frame,
						"That file type is not supported!\n Only .txt file type is supported!",
						"Error!",
						JOptionPane.ERROR_MESSAGE);
			}
		} 
		else {
			JOptionPane.showMessageDialog(frame,
					"File could not be opened!",
					"Error!",
					JOptionPane.ERROR_MESSAGE);
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
			mostRecentHashCode = syntaxEditor.getText().hashCode();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frame,
					"Exception while trying to save the file!",
					"Error!",
					JOptionPane.ERROR_MESSAGE);
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
		
		syntaxEditor.setText("DATA:\n\nVARIABLES:\n\nORDINAL:\n\nNOMINAL:\n\nMODEL:\n\nNIMPS:\n\nTHIN:\n\nBURN:\n\nSEED:\n\nCHAINS:\n\nOUTFILE:\n\nOPTIONS:\n\n");
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
			return selectedFile.getSelectedFile().toPath();
		} else {
			return null;
		}
		
	}
	
	public void writeImportSyntax() {
		syntaxEditor.setText("");
		String line = "";
		if(model.dataSetPath != null) {
			line = "DATA: " + model.dataSetPath.toAbsolutePath() + ";";
			syntaxEditor.append(line);
		}
		if(model.variables.size() != 0){
			line = "\n\nVARIABLES: ";
			int i;
			String ordinalVariables = "\n\nORDINAL: ";
			String nominalVariables = "\n\nNOMINAL: ";
			for(i = 0; i < model.variables.size() - 1; i++){
				Variable var = model.variables.get(i);
				line = line + var.name + " ";
				
				if(var.type == "Ordinal") 
					ordinalVariables = ordinalVariables + var.name + " ";
				if(var.type == "Nominal")
					nominalVariables = nominalVariables + var.name + " "; 
			}
			line = line + model.variables.get(i).name + ";";
			if(model.variables.get(i).type == "Ordinal") 
				ordinalVariables = ordinalVariables + model.variables.get(i).name + ";";
			if(model.variables.get(i).type == "Nominal")
				nominalVariables = nominalVariables + model.variables.get(i).name + ";"; 
			
			syntaxEditor.append(line);
			syntaxEditor.append(ordinalVariables);
			syntaxEditor.append(nominalVariables);
			
		}
		
		line = "\n\nMISSING: ";
		if(model.mappings.containsKey("MVC")){
			line += model.mappings.get("MVC");
			line += ";";
		}
		syntaxEditor.append(line);
	}
	
	public void writeModelMCMCOutputSyntax() {
		syntaxEditor.setText("");
		writeImportSyntax();
		String line;
		if(model.modelVariables.size() > 0) {
			line = "\n\nMODEL: ";
			int i;
			for(i = 0; i < model.identifierVariables.size(); i++) {
				String name = model.identifierVariables.get(i).name;
				String truncatedVariable = name.substring(0, name.lastIndexOf("("));
				line = line + truncatedVariable + " ";
			}
			line += "~ ";
			for(i = 0; i < model.modelVariables.size()-1; i++) {
				String name = model.modelVariables.get(i).name;
				if(name.lastIndexOf("(") != -1) {
					String truncatedVariable = name.substring(0, name.lastIndexOf("("));
					line = line + truncatedVariable + " ";
				} else {
					line = line + name + " ";
				}
			}
			
			String name = model.modelVariables.get(i).name;
			if(name.lastIndexOf("(") != -1) {
				String truncatedVariable = name.substring(0, name.lastIndexOf("("));
				line = line + truncatedVariable + " ";
			} else {
				line = line + name + " ";
			}
			
			syntaxEditor.append(line);
		}
		
		System.out.println("Model Mapping size = " + model.mappings.size());
		
		for(Entry<String, String> entry: model.mappings.entrySet()) {
			System.out.println("Key:" + entry.getKey() + " Value:" + entry.getValue());
		}
		
		if(model.mappings.size() == 11) {
			line = "\n\nNIMPS: " + model.mappings.get("Nimps");
			line = line + "\n\nTHIN: " + model.mappings.get("ThinIterations");
			line = line + "\n\nBURN: " + model.mappings.get("BurnIn");
			line = line + "\n\nSEED: " + model.mappings.get("RandomSeed");
			line = line + "\n\nCHAINS: " + model.mappings.get("Chains");
			
			if(model.mappings.get("DF") == "separate") {
				line = line + "\n\nOUTFILE: " + model.outputFilePath + "*." + model.mappings.get("DT");
			} else {
				line = line + "\n\nOUTFILE: " + model.outputFilePath + "." + model.mappings.get("DT");
			}
			
			syntaxEditor.append(line);
			
			line = "\n\nOPTIONS:";
			line += " " + model.mappings.get("DF") ;
			line += " " + model.mappings.get("Diagnostics");
			line += " " + model.mappings.get("DT");
			line += " " + model.mappings.get("CM");
			line += " " + model.mappings.get("VP");
			line += " " + model.mappings.get("LV");
			syntaxEditor.append(line);
		}
	}

}
