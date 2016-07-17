import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;

public class MCMCOptionsPanel extends JPanel {
	public JTextField burnInTB;
	private SyntaxModel model;
	public JTextField thinIterTB;
	public JTextField nimpsTB;
	public JTextField randSeedTB;
	public JComboBox<String> noOfChains;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	/**
	 * Create the panel.
	 */
	public MCMCOptionsPanel() {
		setLayout(null);
		
		model = SyntaxModel.getInstance();
		burnInTB = new JTextField();
		burnInTB.setBounds(328, 68, 86, 20);
		add(burnInTB);
		burnInTB.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Burn In Iterations");
		lblNewLabel.setBounds(199, 71, 122, 14);
		add(lblNewLabel);
		
		JLabel lblThinningIterations = new JLabel("Thinning Iterations");
		lblThinningIterations.setBounds(199, 113, 111, 14);
		add(lblThinningIterations);
		
		thinIterTB = new JTextField();
		thinIterTB.setBounds(328, 110, 86, 20);
		add(thinIterTB);
		thinIterTB.setColumns(10);
		
		JLabel lblImputations = new JLabel("Imputations");
		lblImputations.setBounds(199, 158, 97, 14);
		add(lblImputations);
		
		nimpsTB = new JTextField();
		nimpsTB.setBounds(328, 155, 86, 20);
		add(nimpsTB);
		nimpsTB.setColumns(10);
		
		JLabel lblRandomNumberSeed = new JLabel("Random Number Seed");
		lblRandomNumberSeed.setBounds(199, 205, 122, 14);
		add(lblRandomNumberSeed);
		
		randSeedTB = new JTextField();
		randSeedTB.setBounds(328, 202, 86, 20);
		add(randSeedTB);
		randSeedTB.setColumns(10);
		
		JLabel lblNumberOfChains = new JLabel("Number of Chains");
		lblNumberOfChains.setBounds(199, 252, 111, 14);
		add(lblNumberOfChains);
		
		JLabel lblClusterMeans = new JLabel("Cluster Means");
		lblClusterMeans.setBounds(123, 329, 86, 14);
		add(lblClusterMeans);
		
		JRadioButton rdbtnInclude = new JRadioButton("Include");
		rdbtnInclude.setActionCommand("clmean\r\n");
		rdbtnInclude.setSelected(true);
		rdbtnInclude.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("CM", rdbtnInclude.getActionCommand());
			}
		});
		
		buttonGroup.add(rdbtnInclude);
		rdbtnInclude.setBounds(125, 350, 109, 23);
		add(rdbtnInclude);
		
		JRadioButton rdbtnExclude = new JRadioButton("Exclude");
		rdbtnExclude.setActionCommand("noclmean");
		rdbtnExclude.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("CM", rdbtnExclude.getActionCommand());
			}
		});
		buttonGroup.add(rdbtnExclude);
		rdbtnExclude.setBounds(125, 376, 109, 23);
		add(rdbtnExclude);
		
		JLabel lblLevelVariance = new JLabel("Level-1 Variance");
		lblLevelVariance.setBounds(288, 329, 86, 14);
		add(lblLevelVariance);
		
		JRadioButton rdbtnHomogenous = new JRadioButton("Homogenous");
		rdbtnHomogenous.setSelected(true);
		rdbtnHomogenous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("LV", rdbtnHomogenous.getActionCommand());
			}
		});
		rdbtnHomogenous.setActionCommand("hov");
		buttonGroup_1.add(rdbtnHomogenous);
		rdbtnHomogenous.setBounds(288, 350, 109, 23);
		add(rdbtnHomogenous);
		
		JRadioButton rdbtnHeterogenous = new JRadioButton("Heterogenous");
		rdbtnHeterogenous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.mappings.put("LV", rdbtnHeterogenous.getActionCommand());
			}
		});
		rdbtnHeterogenous.setActionCommand("hev");
		buttonGroup_1.add(rdbtnHeterogenous);
		rdbtnHeterogenous.setBounds(288, 376, 109, 23);
		add(rdbtnHeterogenous);
		
		JLabel lblVariancePrior = new JLabel("Variance Prior");
		lblVariancePrior.setBounds(433, 329, 79, 14);
		add(lblVariancePrior);
		
		JRadioButton rdbtnInverseWishart = new JRadioButton("Inverse Wishart 0");
		rdbtnInverseWishart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("VP", rdbtnHeterogenous.getActionCommand());
			}
		});
		rdbtnInverseWishart.setSelected(true);
		rdbtnInverseWishart.setActionCommand("iw0");
		buttonGroup_2.add(rdbtnInverseWishart);
		rdbtnInverseWishart.setBounds(433, 350, 122, 23);
		add(rdbtnInverseWishart);
		
		JRadioButton rdbtnInverseWishartId = new JRadioButton("Inverse Wishart ID");
		rdbtnInverseWishartId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("VP", rdbtnHeterogenous.getActionCommand());
			}
		});
		rdbtnInverseWishartId.setActionCommand("iw1");
		buttonGroup_2.add(rdbtnInverseWishartId);
		rdbtnInverseWishartId.setBounds(433, 376, 122, 23);
		add(rdbtnInverseWishartId);
		
		noOfChains = new JComboBox<String>();
		noOfChains.setModel(new DefaultComboBoxModel<String>(new String[] {"1", "2", "3", "4", "5", "6", "7"}));
		noOfChains.setBounds(328, 249, 86, 20);
		add(noOfChains);

	}
	
	public void initializeComponentsWithModel() {
		
	}
}
