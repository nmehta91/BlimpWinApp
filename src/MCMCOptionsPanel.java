import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;

public class MCMCOptionsPanel extends JPanel {
	private JTextField textField;
	private SyntaxModel model;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	/**
	 * Create the panel.
	 */
	public MCMCOptionsPanel() {
		setLayout(null);
		
		model = SyntaxModel.getInstance();
		textField = new JTextField();
		textField.setBounds(328, 68, 86, 20);
		add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Burn In Iterations");
		lblNewLabel.setBounds(199, 71, 122, 14);
		add(lblNewLabel);
		
		JLabel lblThinningIterations = new JLabel("Thinning Iterations");
		lblThinningIterations.setBounds(199, 113, 111, 14);
		add(lblThinningIterations);
		
		textField_1 = new JTextField();
		textField_1.setBounds(328, 110, 86, 20);
		add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblImputations = new JLabel("Imputations");
		lblImputations.setBounds(199, 158, 97, 14);
		add(lblImputations);
		
		textField_2 = new JTextField();
		textField_2.setBounds(328, 155, 86, 20);
		add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblRandomNumberSeed = new JLabel("Random Number Seed");
		lblRandomNumberSeed.setBounds(199, 205, 122, 14);
		add(lblRandomNumberSeed);
		
		textField_3 = new JTextField();
		textField_3.setBounds(328, 202, 86, 20);
		add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblNumberOfChains = new JLabel("Number of Chains");
		lblNumberOfChains.setBounds(199, 252, 111, 14);
		add(lblNumberOfChains);
		
		JLabel lblClusterMeans = new JLabel("Cluster Means");
		lblClusterMeans.setBounds(123, 329, 86, 14);
		add(lblClusterMeans);
		
		JRadioButton rdbtnInclude = new JRadioButton("Include");
		buttonGroup.add(rdbtnInclude);
		rdbtnInclude.setBounds(125, 350, 109, 23);
		add(rdbtnInclude);
		
		JRadioButton rdbtnExclude = new JRadioButton("Exclude");
		buttonGroup.add(rdbtnExclude);
		rdbtnExclude.setBounds(125, 376, 109, 23);
		add(rdbtnExclude);
		
		JLabel lblLevelVariance = new JLabel("Level-1 Variance");
		lblLevelVariance.setBounds(288, 329, 86, 14);
		add(lblLevelVariance);
		
		JRadioButton rdbtnHomogenous = new JRadioButton("Homogenous");
		buttonGroup_1.add(rdbtnHomogenous);
		rdbtnHomogenous.setBounds(288, 350, 109, 23);
		add(rdbtnHomogenous);
		
		JRadioButton rdbtnHeterogenous = new JRadioButton("Heterogenous");
		buttonGroup_1.add(rdbtnHeterogenous);
		rdbtnHeterogenous.setBounds(288, 376, 109, 23);
		add(rdbtnHeterogenous);
		
		JLabel lblVariancePrior = new JLabel("Variance Prior");
		lblVariancePrior.setBounds(433, 329, 79, 14);
		add(lblVariancePrior);
		
		JRadioButton rdbtnInverseWishart = new JRadioButton("Inverse Wishart 0");
		buttonGroup_2.add(rdbtnInverseWishart);
		rdbtnInverseWishart.setBounds(433, 350, 122, 23);
		add(rdbtnInverseWishart);
		
		JRadioButton rdbtnInverseWishartId = new JRadioButton("Inverse Wishart ID");
		buttonGroup_2.add(rdbtnInverseWishartId);
		rdbtnInverseWishartId.setBounds(433, 376, 122, 23);
		add(rdbtnInverseWishartId);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(328, 249, 86, 20);
		add(comboBox);

	}
	
	public void initializeComponentsWithModel() {
		
	}
}
