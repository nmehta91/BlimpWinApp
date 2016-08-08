import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

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
		
		rdbtndat = new JRadioButton(".dat");
		rdbtndat.setBounds(358, 316, 143, 23);
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
		
		JLabel lblNewLabel = new JLabel("Save Imputations To File");
		lblNewLabel.setBounds(254, 135, 118, 14);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(398, 131, 103, 23);
		
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int openResult = selectedOutputDirectory.showSaveDialog(null);
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
		
		rdbtnSeparated = new JRadioButton("Separate Files");
		rdbtnSeparated.setBounds(168, 313, 101, 23);
		rdbtnSeparated.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputFileNameLabel.setText(model.outputFilePath + "*." + model.mappings.get("DT") );
				model.mappings.put("DF", "separate");
			}
		});
		
		rdbtnStacked = new JRadioButton("Stacked");
		rdbtnStacked.setBounds(168, 290, 101, 23);
		rdbtnStacked.setSelected(true);
		buttonGroup.add(rdbtnStacked);
		rdbtnStacked.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputFileNameLabel.setText(model.outputFilePath + "." + model.mappings.get("DT"));
				model.mappings.put("DF", "stacked");
			}
		});
		
		outputFileNameLabel = new JLabel("");
		outputFileNameLabel.setBounds(398, 159, 247, 99);
		
		JLabel lblDataFormat = new JLabel("Data Format");
		lblDataFormat.setBounds(168, 269, 72, 14);
		
		JLabel lblFileType = new JLabel("File Type");
		lblFileType.setBounds(362, 269, 65, 14);
		
		JLabel lblDiagnostics = new JLabel("Diagnostics");
		lblDiagnostics.setBounds(523, 269, 54, 14);
		
		rdbtnNoPsr = new JRadioButton("No PSR");
		rdbtnNoPsr.setBounds(523, 290, 126, 23);
		rdbtnNoPsr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("Diagnostics", "nopsr");
			}
		});
		
		rdbtncsv = new JRadioButton(".csv");
		rdbtncsv.setBounds(358, 290, 143, 23);
		rdbtncsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputFileNameLabel.setText(model.outputFilePath + ".csv");
				model.mappings.put("DT", "csv");
			}
		});
		rdbtncsv.setSelected(true);
		buttonGroup_1.add(rdbtncsv);
		rdbtnNoPsr.setSelected(true);
		buttonGroup_2.add(rdbtnNoPsr);
		buttonGroup.add(rdbtnSeparated);
		buttonGroup_1.add(rdbtndat);
		selectedOutputDirectory = new JFileChooser();
		selectedOutputDirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		rdbtnPsr = new JRadioButton("PSR");
		rdbtnPsr.setBounds(523, 313, 126, 23);
		rdbtnPsr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("Diagnostics", "psr");
			}
		});
		buttonGroup_2.add(rdbtnPsr);
		setLayout(null);
		add(lblDataFormat);
		add(lblFileType);
		add(lblDiagnostics);
		add(rdbtnStacked);
		add(rdbtncsv);
		add(rdbtnNoPsr);
		add(rdbtnSeparated);
		add(rdbtndat);
		add(rdbtnPsr);
		add(lblNewLabel);
		add(outputFileNameLabel);
		add(btnBrowse);
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
