import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;

public class SpecifyModelPanel extends JPanel {
	private JTable table;
	private JTable table_1;
	private JTable table_2;
	private SyntaxModel model;
	/**
	 * Create the panel.
	 */
	public SpecifyModelPanel() {
		setLayout(null);
		
		model = SyntaxModel.getInstance();
		
		table = new JTable();
		table.setBounds(25, 44, 210, 194);
		add(table);
		
		table_1 = new JTable();
		table_1.setBounds(442, 44, 183, 149);
		add(table_1);
		
		table_2 = new JTable();
		table_2.setBounds(442, 226, 183, 132);
		add(table_2);
		
		JButton button = new JButton("-->");
		button.setBounds(293, 81, 89, 23);
		add(button);
		
		JButton button_1 = new JButton("<--");
		button_1.setBounds(293, 134, 89, 23);
		add(button_1);
		
		JLabel lblVariables = new JLabel("Variables");
		lblVariables.setBounds(25, 30, 76, 14);
		add(lblVariables);
		
		JLabel lblModelVariables = new JLabel("Model Variables");
		lblModelVariables.setBounds(442, 30, 144, 14);
		add(lblModelVariables);
		
		JLabel lblImputationVariables = new JLabel("Imputation Variables");
		lblImputationVariables.setBounds(442, 212, 121, 14);
		add(lblImputationVariables);

	}
	
	public void initializeComponentsWithModel() {
		
	}
}
