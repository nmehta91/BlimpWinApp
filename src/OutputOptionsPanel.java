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
		rdbtndat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(model.outputFilePath != null) {
					int indexOfExtension = model.outputFilePath.lastIndexOf(".");
					String newFileName = model.outputFilePath.substring(0, indexOfExtension);
					model.outputFilePath = newFileName + ".dat";
					outputFileNameLabel.setText(model.outputFilePath.toString());
				}
				model.mappings.put("DT", "dat");
			}
		});
		
		JLabel lblNewLabel = new JLabel("Save Imputations To File");
		
		JButton btnBrowse = new JButton("Browse");
		
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
		
		rdbtnSeparated = new JRadioButton("Separate Files");
		rdbtnSeparated.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("DF", "seperate");
			}
		});
		
		rdbtnStacked = new JRadioButton("Stacked");
		rdbtnStacked.setSelected(true);
		buttonGroup.add(rdbtnStacked);
		rdbtnStacked.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("DF", "stacked");
			}
		});
		
		outputFileNameLabel = new JLabel("");
		
		JLabel lblDataFormat = new JLabel("Data Format");
		
		JLabel lblFileType = new JLabel("File Type");
		
		JLabel lblDiagnostics = new JLabel("Diagnostics");
		
		rdbtnNoPsr = new JRadioButton("No PSR");
		rdbtnNoPsr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("Diagnostics", "nopsr");
			}
		});
		
		rdbtncsv = new JRadioButton(".csv");
		rdbtncsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indexOfExtension = model.outputFilePath.lastIndexOf(".");
				String newFileName = model.outputFilePath.substring(0, indexOfExtension);
				model.outputFilePath = newFileName + ".csv";
				outputFileNameLabel.setText(model.outputFilePath);
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
		rdbtnPsr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("Diagnostics", "psr");
			}
		});
		buttonGroup_2.add(rdbtnPsr);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(168)
					.addComponent(lblDataFormat, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
					.addGap(122)
					.addComponent(lblFileType, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
					.addGap(96)
					.addComponent(lblDiagnostics))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(168)
					.addComponent(rdbtnStacked, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addGap(89)
					.addComponent(rdbtncsv, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
					.addGap(22)
					.addComponent(rdbtnNoPsr, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(168)
					.addComponent(rdbtnSeparated, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addGap(89)
					.addComponent(rdbtndat, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
					.addGap(22)
					.addComponent(rdbtnPsr, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(254)
					.addComponent(lblNewLabel)
					.addGap(26)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(outputFileNameLabel, GroupLayout.PREFERRED_SIZE, 247, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBrowse, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
					.addGap(47))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(131)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(4)
							.addComponent(lblNewLabel))
						.addComponent(btnBrowse))
					.addGap(5)
					.addComponent(outputFileNameLabel, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDataFormat)
						.addComponent(lblFileType)
						.addComponent(lblDiagnostics))
					.addGap(7)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnStacked)
						.addComponent(rdbtncsv)
						.addComponent(rdbtnNoPsr))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnSeparated)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(3)
							.addComponent(rdbtndat))
						.addComponent(rdbtnPsr)))
		);
		setLayout(groupLayout);
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
}
