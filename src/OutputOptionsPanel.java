import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Box;

public class OutputOptionsPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public JFileChooser selectedOutputDirectory;
	private SyntaxModel model;
	public JLabel outputFileNameLabel;
	private JRadioButton rdbtnStacked;
	private JRadioButton rdbtnSeparated;
	private JRadioButton rdbtncsv;
	private JRadioButton rdbtndat;
	private JRadioButton rdbtnNoPsr;
	private JRadioButton rdbtnPsr;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	
	public OutputOptionsPanel() {
		model = SyntaxModel.getInstance();
		initializeModel();
		
		JLabel lblNewLabel = new JLabel("Save Imputations To File");
		lblNewLabel.setBounds(227, 125, 171, 32);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(399, 130, 91, 27);
		
		JPanel outputOptionsPanel = this;
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int openResult = selectedOutputDirectory.showSaveDialog(outputOptionsPanel);
				if(openResult == selectedOutputDirectory.APPROVE_OPTION) {
					model.outputFilePath = selectedOutputDirectory.getSelectedFile().getAbsolutePath().toString();
					System.out.println("OutPut Filepath: " + model.outputFilePath);
					if(rdbtncsv.isSelected()) {
						outputFileNameLabel.setText(model.outputFilePath + ".csv");
					} else {
						outputFileNameLabel.setText(model.outputFilePath + ".dat");
					}	
				}
			}
		});
		
		outputFileNameLabel = new JLabel("");
		outputFileNameLabel.setBounds(399, 161, 258, 99);
		
		JLabel lblDataFormat = new JLabel("Data Format");
		lblDataFormat.setBounds(113, 271, 128, 23);
		
		JLabel lblFileType = new JLabel("File Type");
		lblFileType.setBounds(321, 271, 135, 23);
		
		JLabel lblDiagnostics = new JLabel("Diagnostics");
		lblDiagnostics.setBounds(524, 271, 133, 23);
		selectedOutputDirectory = new JFileChooser();
		
		setLayout(null);
		add(lblDataFormat);
		add(lblFileType);
		add(lblDiagnostics);
		add(lblNewLabel);
		add(outputFileNameLabel);
		add(btnBrowse);
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBounds(113, 296, 133, 81);
		add(verticalBox);
		
		rdbtnStacked = new JRadioButton("Stacked");
		verticalBox.add(rdbtnStacked);
		rdbtnStacked.setSelected(true);
		buttonGroup.add(rdbtnStacked);
		
		rdbtnSeparated = new JRadioButton("Separate Files");
		verticalBox.add(rdbtnSeparated);
		rdbtnSeparated.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputFileNameLabel.setText(model.outputFilePath + "*." + model.mappings.get("DT") );
				model.mappings.put("DF", "separate");
			}
		});
		buttonGroup.add(rdbtnSeparated);
		
		Box verticalBox_1 = Box.createVerticalBox();
		verticalBox_1.setBounds(322, 296, 134, 81);
		add(verticalBox_1);
		
		rdbtncsv = new JRadioButton(".csv");
		verticalBox_1.add(rdbtncsv);
		rdbtncsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputFileNameLabel.setText(model.outputFilePath + ".csv");
				model.mappings.put("DT", "csv");
			}
		});
		rdbtncsv.setSelected(true);
		buttonGroup_1.add(rdbtncsv);
		
		rdbtndat = new JRadioButton(".dat");
		verticalBox_1.add(rdbtndat);
		rdbtndat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(model.outputFilePath != null) {
//					int indexOfExtension = model.outputFilePath.lastIndexOf(".");
//					String newFileName = model.outputFilePath.substring(0, indexOfExtension);
//					model.outputFilePath = newFileName + ".dat";
					outputFileNameLabel.setText(model.outputFilePath + ".dat");
				}
				model.mappings.put("DT", "dat");
			}
		});
		buttonGroup_1.add(rdbtndat);
		
		Box verticalBox_2 = Box.createVerticalBox();
		verticalBox_2.setBounds(524, 296, 133, 81);
		add(verticalBox_2);
		
		rdbtnNoPsr = new JRadioButton("No PSR");
		verticalBox_2.add(rdbtnNoPsr);
		rdbtnNoPsr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("Diagnostics", "nopsr");
			}
		});
		rdbtnNoPsr.setSelected(true);
		buttonGroup_2.add(rdbtnNoPsr);
		
		rdbtnPsr = new JRadioButton("PSR");
		verticalBox_2.add(rdbtnPsr);
		rdbtnPsr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("Diagnostics", "psr");
			}
		});
		buttonGroup_2.add(rdbtnPsr);
		rdbtnStacked.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputFileNameLabel.setText(model.outputFilePath + "." + model.mappings.get("DT"));
				model.mappings.put("DF", "stacked");
			}
		});
	}
	
	public void initializeModel() {
		model.mappings.put("DF", "stacked");
		model.mappings.put("DT", "csv");
		model.mappings.put("Diagnostics", "nopsr");
	}
	
	public void reset() {
		outputFileNameLabel.setText("");
		rdbtnStacked.setSelected(true);
		rdbtncsv.setSelected(true);
		rdbtnNoPsr.setSelected(true);
	}
	
	public boolean isComplete() {
		boolean isComplete = true;
		if(model.outputFilePath == null){
			isComplete = false;
		}
		return isComplete;
	}
}
