import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class OutputOptionsPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public JFileChooser selectedOutputDirectory;
	private SyntaxModel model;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	
	public OutputOptionsPanel() {
		setLayout(null);
		model = SyntaxModel.getInstance();
		
		JLabel lblNewLabel = new JLabel("Save Imputations To File");
		lblNewLabel.setBounds(208, 131, 118, 14);
		add(lblNewLabel);
		
		JLabel outputFileNameLabel = new JLabel("");
		outputFileNameLabel.setBounds(346, 161, 247, 104);
		add(outputFileNameLabel); 
		
		JButton btnBrowse = new JButton("Browse");
		selectedOutputDirectory = new JFileChooser();
		selectedOutputDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		btnBrowse.setBounds(336, 127, 89, 23);
		add(btnBrowse);
		
		JLabel lblDataFormat = new JLabel("Data Format");
		lblDataFormat.setBounds(102, 288, 77, 14);
		add(lblDataFormat);
		
		JLabel lblFileType = new JLabel("File Type");
		lblFileType.setBounds(286, 288, 56, 14);
		add(lblFileType);
		
		JLabel lblDiagnostics = new JLabel("Diagnostics");
		lblDiagnostics.setBounds(467, 288, 77, 14);
		add(lblDiagnostics);
		
		JRadioButton rdbtnStacked = new JRadioButton("Stacked");
		rdbtnStacked.setActionCommand("stacked");
		rdbtnStacked.setSelected(true);
		buttonGroup.add(rdbtnStacked);
		rdbtnStacked.setBounds(102, 309, 109, 23);
		rdbtnStacked.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("DF", rdbtnStacked.getActionCommand());
			}
		});
		add(rdbtnStacked);
		
		JRadioButton rdbtnSeparated = new JRadioButton("Separate Files");
		rdbtnSeparated.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("DF", rdbtnSeparated.getActionCommand());
			}
		});
		rdbtnSeparated.setActionCommand("seperate");
		buttonGroup.add(rdbtnSeparated);
		rdbtnSeparated.setBounds(102, 335, 109, 23);
		add(rdbtnSeparated);
		
		JRadioButton rdbtncsv = new JRadioButton(".csv");
		rdbtncsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indexOfExtension = model.outputFilePath.lastIndexOf(".");
				String newFileName = model.outputFilePath.substring(0, indexOfExtension);
				model.outputFilePath = newFileName + ".csv";
				outputFileNameLabel.setText(model.outputFilePath);
			}
		});
		rdbtncsv.setSelected(true);
		buttonGroup_1.add(rdbtncsv);
		rdbtncsv.setBounds(286, 309, 109, 23);
		add(rdbtncsv);
		
		JRadioButton rdbtndat = new JRadioButton(".dat");
		rdbtndat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indexOfExtension = model.outputFilePath.lastIndexOf(".");
				String newFileName = model.outputFilePath.substring(0, indexOfExtension);
				model.outputFilePath = newFileName + ".dat";
				outputFileNameLabel.setText(model.outputFilePath.toString());
			}
		});
		buttonGroup_1.add(rdbtndat);
		rdbtndat.setBounds(286, 335, 109, 23);
		add(rdbtndat);
		
		JRadioButton rdbtnNoPsr = new JRadioButton("No PSR");
		rdbtnNoPsr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("Diagnostics", rdbtnNoPsr.getActionCommand());
			}
		});
		rdbtnNoPsr.setActionCommand("nopsr");
		rdbtnNoPsr.setSelected(true);
		buttonGroup_2.add(rdbtnNoPsr);
		rdbtnNoPsr.setBounds(467, 309, 109, 23);
		add(rdbtnNoPsr);
		
		JRadioButton rdbtnPsr = new JRadioButton("PSR");
		rdbtnPsr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("Diagnostics", rdbtnPsr.getActionCommand());
			}
		});
		rdbtnPsr.setActionCommand("psr");
		buttonGroup_2.add(rdbtnPsr);
		rdbtnPsr.setBounds(467, 335, 109, 23);
		add(rdbtnPsr);
		
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int openResult = selectedOutputDirectory.showSaveDialog(null);
				if(openResult == selectedOutputDirectory.APPROVE_OPTION) {
					model.outputFilePath = selectedOutputDirectory.getSelectedFile().getAbsolutePath().toString();
					System.out.println("OutPut Filepath: " + model.outputFilePath);
					if(rdbtncsv.isSelected()) {
						model.outputFilePath = model.outputFilePath + ".csv";
						outputFileNameLabel.setText(model.outputFilePath);
					} else {
						model.outputFilePath = model.outputFilePath + ".dat";
						outputFileNameLabel.setText(model.outputFilePath.toString() + ".dat");
					}	
				}
			}
		});
		

	}
}
