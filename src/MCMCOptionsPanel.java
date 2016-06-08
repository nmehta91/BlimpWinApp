import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class MCMCOptionsPanel extends JPanel {
	private JTextField textField;
	private SyntaxModel model;
	/**
	 * Create the panel.
	 */
	public MCMCOptionsPanel() {
		setLayout(null);
		
		model = SyntaxModel.getInstance();
		textField = new JTextField();
		textField.setBounds(279, 127, 86, 20);
		add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(285, 102, 46, 14);
		add(lblNewLabel);

	}
	
	public void initializeComponentsWithModel() {
		
	}
}
