import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class MCMCOptionsPanel extends JPanel {
	public JTextField burnInTB;
	private SyntaxModel model;
	public JTextField thinIterTB;
	public JTextField nimpsTB;
	public JTextField randSeedTB;
	public JComboBox<String> noOfChains;
	private JRadioButton rdbtnInclude;
	private JRadioButton rdbtnExclude;
	private JRadioButton rdbtnHomogenous;
	private JRadioButton rdbtnHeterogenous;
	private JRadioButton rdbtnInverseWishart;
	private JRadioButton rdbtnInverseWishartId;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	/**
	 * Create the panel.
	 */
	public MCMCOptionsPanel() {
		
		model = SyntaxModel.getInstance();
		initializeModel();
		initializeWithModel();
		burnInTB = new JTextField();
		burnInTB.setBounds(328, 34, 86, 20);
		burnInTB.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Burn In Iterations");
		lblNewLabel.setBounds(199, 37, 122, 14);
		
		JLabel lblThinningIterations = new JLabel("Thinning Iterations");
		lblThinningIterations.setBounds(199, 79, 111, 14);
		
		thinIterTB = new JTextField();
		thinIterTB.setBounds(328, 76, 86, 20);
		thinIterTB.setColumns(10);
		
		JLabel lblImputations = new JLabel("Imputations");
		lblImputations.setBounds(199, 115, 97, 14);
		
		nimpsTB = new JTextField();
		nimpsTB.setBounds(328, 115, 86, 20);
		nimpsTB.setColumns(10);
		
		JLabel lblRandomNumberSeed = new JLabel("Random Number Seed");
		lblRandomNumberSeed.setBounds(199, 161, 122, 20);
		
		randSeedTB = new JTextField();
		randSeedTB.setBounds(328, 161, 86, 20);
		randSeedTB.setColumns(10);
		
		JLabel lblNumberOfChains = new JLabel("Number of Chains");
		lblNumberOfChains.setBounds(199, 202, 111, 14);
		
		JLabel lblClusterMeans = new JLabel("Cluster Means");
		lblClusterMeans.setBounds(148, 285, 86, 14);
		
		rdbtnInclude = new JRadioButton("Include");
		rdbtnInclude.setBounds(148, 308, 109, 23);
		rdbtnInclude.setSelected(true);
		rdbtnInclude.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("CM", "clmean");
			}
		});
		
		buttonGroup.add(rdbtnInclude);
		
		rdbtnExclude = new JRadioButton("Exclude");
		rdbtnExclude.setBounds(148, 334, 109, 23);
		rdbtnExclude.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("CM", "noclmean");
			}
		});
		buttonGroup.add(rdbtnExclude);
		
		JLabel lblLevelVariance = new JLabel("Level-1 Variance");
		lblLevelVariance.setBounds(328, 285, 99, 14);
		
		rdbtnHomogenous = new JRadioButton("Homogenous");
		rdbtnHomogenous.setBounds(328, 301, 109, 23);
		rdbtnHomogenous.setSelected(true);
		rdbtnHomogenous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("LV", "hov");
			}
		});
		buttonGroup_1.add(rdbtnHomogenous);
		
		rdbtnHeterogenous = new JRadioButton("Heterogenous");
		rdbtnHeterogenous.setBounds(328, 334, 109, 23);
		rdbtnHeterogenous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.mappings.put("LV", "hev");
			}
		});
		buttonGroup_1.add(rdbtnHeterogenous);
		
		JLabel lblVariancePrior = new JLabel("Variance Prior");
		lblVariancePrior.setBounds(497, 285, 79, 14);
		
		rdbtnInverseWishart = new JRadioButton("Inverse Wishart 0");
		rdbtnInverseWishart.setBounds(495, 308, 131, 23);
		rdbtnInverseWishart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("VP", "iw0");
			}
		});
		rdbtnInverseWishart.setSelected(true);
		buttonGroup_2.add(rdbtnInverseWishart);
		
		rdbtnInverseWishartId = new JRadioButton("Inverse Wishart ID");
		rdbtnInverseWishartId.setBounds(495, 334, 131, 23);
		rdbtnInverseWishartId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("VP", "iw1");
			}
		});
		buttonGroup_2.add(rdbtnInverseWishartId);
		
		noOfChains = new JComboBox<String>();
		noOfChains.setBounds(328, 199, 86, 20);
		noOfChains.setModel(new DefaultComboBoxModel<String>(new String[] {"1", "2", "3", "4", "5", "6", "7"}));
		setLayout(null);
		add(lblNewLabel);
		add(burnInTB);
		add(lblThinningIterations);
		add(thinIterTB);
		add(lblImputations);
		add(nimpsTB);
		add(lblRandomNumberSeed);
		add(randSeedTB);
		add(lblNumberOfChains);
		add(noOfChains);
		add(rdbtnExclude);
		add(rdbtnHeterogenous);
		add(rdbtnInverseWishartId);
		add(lblClusterMeans);
		add(rdbtnInclude);
		add(rdbtnHomogenous);
		add(rdbtnInverseWishart);
		add(lblLevelVariance);
		add(lblVariancePrior);
		
		System.out.println("Initializing mcmc..");

	}
	
	public void initializeModel() {
		model.mappings.put("CM", "clmean");
		model.mappings.put("LV", "hov");
		model.mappings.put("VP", "iw0");
	}
	
	public void initializeWithModel() {
		if(model.mappings.get("BurnIn") != null){
//			burnInTB.setText(model.mappings.get("BurnIn"));
		}
			
	}
	
	public void reset() {
		burnInTB.setText("");
		thinIterTB.setText("");
		nimpsTB.setText("");
		randSeedTB.setText("");
	    noOfChains.setSelectedIndex(0);
	    rdbtnInclude.setSelected(true);
	    rdbtnHomogenous.setSelected(true);
	    rdbtnInverseWishart.setSelected(true);
	}
}
