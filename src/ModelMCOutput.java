import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JRadioButton;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Panel;
import javax.swing.ButtonGroup;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ModelMCOutput extends JFrame {

	private JPanel contentPane;
	public JTabbedPane tabbedPane;
	private SpecifyModelPanel SMPanel;
	private MCMCOptionsPanel MCMCPanel;
	private OutputOptionsPanel outputOptionsPanel;
	private SyntaxModel model;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public ModelMCOutput(int selectedTabIndex) {
		setTitle("Model Specification");
		initializeWindowComponents(selectedTabIndex);
		
	}
	
	public void initializeWindowComponents(int selectTabIndex) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 864, 656);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		model = SyntaxModel.getInstance();
		
		JButton btnNewButton = new JButton("Done");
		btnNewButton.setBounds(686, 572, 141, 23);
		contentPane.add(btnNewButton);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(5, 5, 822, 556);
		contentPane.add(tabbedPane);
		MCMCPanel = new MCMCOptionsPanel();
		outputOptionsPanel = new OutputOptionsPanel();
		
		SMPanel = new SpecifyModelPanel();
		
		tabbedPane.addTab("Specify Model", null, SMPanel, null);
		SMPanel.setLayout(null);
		tabbedPane.addTab("MCMC Options", null, MCMCPanel, null);
		tabbedPane.addTab("Output Options", null, outputOptionsPanel, null);
		tabbedPane.setSelectedIndex(selectTabIndex);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnCancel.setBounds(15, 572, 89, 23);
		contentPane.add(btnCancel);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		btnReset.setBounds(108, 572, 89, 23);
		contentPane.add(btnReset);
		
		btnNewButton.addActionListener(new doneActionListener(this));
	}
	
	public void selectTab(int index) {
		tabbedPane.setSelectedIndex(index);
	}
	
	class doneActionListener implements ActionListener{

	    private JFrame toBeClose;

	    public doneActionListener(JFrame toBeClose) {
	        this.toBeClose = toBeClose;
	    }

	    public void actionPerformed(ActionEvent e) {
	    	if(!MCMCPanel.burnInTB.getText().equals(""))
	    		model.mappings.put("BurnIn", MCMCPanel.burnInTB.getText());
	    	if(!MCMCPanel.thinIterTB.getText().equals(""))
	    		model.mappings.put("BurnIn", MCMCPanel.thinIterTB.getText());
	    	if(!MCMCPanel.nimpsTB.getText().equals(""))
	    		model.mappings.put("BurnIn", MCMCPanel.nimpsTB.getText());
	    	if(!MCMCPanel.randSeedTB.getText().equals(""))
	    		model.mappings.put("BurnIn", MCMCPanel.randSeedTB.getText());
	    	if(!MCMCPanel.noOfChains.getSelectedItem().toString().equals(""))
	    		model.mappings.put("BurnIn", MCMCPanel.noOfChains.getSelectedItem().toString());
	
	        toBeClose.setVisible(false);
	        toBeClose.dispose();
	    }
	}
	
	public void reset() {
		SMPanel.reset();
		MCMCPanel.reset();
		outputOptionsPanel.reset();
	}	
}
