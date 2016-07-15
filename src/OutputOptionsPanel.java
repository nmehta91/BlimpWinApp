import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class OutputOptionsPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public JFileChooser selectedOutputDirectory;
	private SyntaxModel model;
	
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
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int openResult = selectedOutputDirectory.showSaveDialog(null);
				if(openResult == selectedOutputDirectory.APPROVE_OPTION) {
					model.outputFilePath = selectedOutputDirectory.getSelectedFile().toPath();
					System.out.println("OutPut Filepath: " + model.outputFilePath);
					outputFileNameLabel.setText(model.outputFilePath.toString());
				}
			}
		});
		btnBrowse.setBounds(336, 127, 89, 23);
		add(btnBrowse);
		
		

	}
}
