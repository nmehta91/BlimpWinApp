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
		
		JButton btnBrowse = new JButton("Browse");
		
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
		
		JLabel lblDataFormat = new JLabel("Data Format");
		
		JLabel lblFileType = new JLabel("File Type");
		
		JLabel lblDiagnostics = new JLabel("Diagnostics");
		selectedOutputDirectory = new JFileChooser();
		
		Box verticalBox = Box.createVerticalBox();
		
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
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(110)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(114)
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnBrowse, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 164, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(286)
							.addComponent(outputFileNameLabel, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblDataFormat, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
							.addGap(80)
							.addComponent(lblFileType, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
							.addGap(68)
							.addComponent(lblDiagnostics, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(verticalBox, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
							.addGap(76)
							.addComponent(verticalBox_1, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
							.addGap(68)
							.addComponent(verticalBox_2, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)))
					.addGap(110))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(126)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 5, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnBrowse, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)))
					.addGap(4)
					.addComponent(outputFileNameLabel, GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDataFormat, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
						.addComponent(lblFileType, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
						.addComponent(lblDiagnostics, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
					.addGap(2)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(verticalBox, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
						.addComponent(verticalBox_1, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
						.addComponent(verticalBox_2, GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
					.addGap(127))
		);
		setLayout(groupLayout);
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
