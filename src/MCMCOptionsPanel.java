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
import javax.swing.Box;

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
		burnInTB.setBounds(378, 34, 86, 20);
		burnInTB.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Burn In Iterations");
		lblNewLabel.setBounds(199, 29, 122, 31);
		
		JLabel lblThinningIterations = new JLabel("Thinning Iterations");
		lblThinningIterations.setBounds(199, 74, 122, 25);
		
		thinIterTB = new JTextField();
		thinIterTB.setBounds(378, 76, 86, 20);
		thinIterTB.setColumns(10);
		
		JLabel lblImputations = new JLabel("Imputations");
		lblImputations.setBounds(199, 107, 111, 30);
		
		nimpsTB = new JTextField();
		nimpsTB.setBounds(378, 112, 86, 20);
		nimpsTB.setColumns(10);
		
		JLabel lblRandomNumberSeed = new JLabel("Random Number Seed");
		lblRandomNumberSeed.setBounds(199, 156, 169, 30);
		
		randSeedTB = new JTextField();
		randSeedTB.setBounds(378, 161, 86, 20);
		randSeedTB.setColumns(10);
		
		JLabel lblNumberOfChains = new JLabel("Number of Chains");
		lblNumberOfChains.setBounds(199, 202, 111, 26);
		
		JLabel lblClusterMeans = new JLabel("Cluster Means");
		lblClusterMeans.setBounds(116, 284, 131, 26);
		
		JLabel lblLevelVariance = new JLabel("Level-1 Variance");
		lblLevelVariance.setBounds(313, 284, 131, 26);
		
		JLabel lblVariancePrior = new JLabel("Variance Prior");
		lblVariancePrior.setBounds(514, 284, 131, 26);
		
		noOfChains = new JComboBox<String>();
		noOfChains.setBounds(378, 205, 86, 20);
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
		add(lblClusterMeans);
		add(lblLevelVariance);
		add(lblVariancePrior);
		
		Box verticalBox = Box.createVerticalBox();
		verticalBox.setBounds(116, 315, 131, 81);
		add(verticalBox);
		
		rdbtnInclude = new JRadioButton("Include");
		verticalBox.add(rdbtnInclude);
		rdbtnInclude.setSelected(true);
		rdbtnInclude.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("CM", "clmean");
			}
		});
		
		buttonGroup.add(rdbtnInclude);
		
		rdbtnExclude = new JRadioButton("Exclude");
		verticalBox.add(rdbtnExclude);
		rdbtnExclude.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("CM", "noclmean");
			}
		});
		buttonGroup.add(rdbtnExclude);
		
		Box verticalBox_1 = Box.createVerticalBox();
		verticalBox_1.setBounds(313, 315, 131, 81);
		add(verticalBox_1);
		
		rdbtnHomogenous = new JRadioButton("Homogenous");
		verticalBox_1.add(rdbtnHomogenous);
		rdbtnHomogenous.setSelected(true);
		rdbtnHomogenous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("LV", "hov");
			}
		});
		buttonGroup_1.add(rdbtnHomogenous);
		
		rdbtnHeterogenous = new JRadioButton("Heterogenous");
		verticalBox_1.add(rdbtnHeterogenous);
		rdbtnHeterogenous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.mappings.put("LV", "hev");
			}
		});
		buttonGroup_1.add(rdbtnHeterogenous);
		
		Box verticalBox_2 = Box.createVerticalBox();
		verticalBox_2.setBounds(514, 315, 201, 81);
		add(verticalBox_2);
		
		rdbtnInverseWishart = new JRadioButton("Prior1 (IW with SSCP = 0)");
		verticalBox_2.add(rdbtnInverseWishart);
		rdbtnInverseWishart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("VP", "iw0");
			}
		});
		buttonGroup_2.add(rdbtnInverseWishart);
		
		rdbtnInverseWishartId = new JRadioButton("Prior2 (IW with SSCP = ID)");
		rdbtnInverseWishartId.setSelected(true);
		verticalBox_2.add(rdbtnInverseWishartId);
		rdbtnInverseWishartId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.mappings.put("VP", "iw1");
			}
		});
		buttonGroup_2.add(rdbtnInverseWishartId);
		
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
	
	public boolean isComplete() {
		boolean isComplete = true;
		if(burnInTB.getText().equals("") || thinIterTB.getText().equals("") 
		   || randSeedTB.getText().equals("") || nimpsTB.getText().equals("")){
			isComplete = false;
		}
		return isComplete;
	}
}
