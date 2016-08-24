import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
import java.awt.Toolkit;

import javax.swing.ButtonGroup;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.SpringLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import net.miginfocom.swing.MigLayout;
import java.awt.Color;
import javax.swing.border.LineBorder;

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
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		model = SyntaxModel.getInstance();
		
		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(114, 527, 82, 23);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		contentPane.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 764, 505);
		contentPane.add(tabbedPane);
		MCMCPanel = new MCMCOptionsPanel();
		outputOptionsPanel = new OutputOptionsPanel();
		
		SMPanel = new SpecifyModelPanel();
		
		tabbedPane.addTab("Specify Model", null, SMPanel, null);
		SMPanel.setLayout(null);
		tabbedPane.addTab("MCMC Options", null, MCMCPanel, null);
		tabbedPane.addTab("Output Options", null, outputOptionsPanel, null);
		tabbedPane.setSelectedIndex(selectTabIndex);
		
		JFrame frame = this;
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(10, 527, 92, 23);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WindowEvent close = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
				Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(close);
			}
		});
		contentPane.add(btnCancel);
		contentPane.add(btnReset);
		
		JButton btnNewButton = new JButton("Done");
		btnNewButton.setBounds(651, 527, 123, 23);
		contentPane.add(btnNewButton);
		
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
	    		model.mappings.put("ThinIterations", MCMCPanel.thinIterTB.getText());
	    	if(!MCMCPanel.nimpsTB.getText().equals(""))
	    		model.mappings.put("Nimps", MCMCPanel.nimpsTB.getText());
	    	if(!MCMCPanel.randSeedTB.getText().equals(""))
	    		model.mappings.put("RandomSeed", MCMCPanel.randSeedTB.getText());
	    	if(!MCMCPanel.noOfChains.getSelectedItem().toString().equals(""))
	    		model.mappings.put("Chains", MCMCPanel.noOfChains.getSelectedItem().toString());
	
	    	if(!IsComplete())
	    		return;
	    	
	        toBeClose.setVisible(false);
	        toBeClose.dispose();
	    }
	    
	    public boolean IsComplete() {
	    	String errorMsg = "";
	    	boolean isComplete = true;
	    	if(!SMPanel.isComplete()){
	    		errorMsg += "Imputation Model Variables Empty.\n";
	    		isComplete = false;
	    	}
	    	if(!MCMCPanel.isComplete()){
	    		errorMsg += "MCMC Options not entered.\n";
	    		isComplete = false;
	    	}
	    	if(!outputOptionsPanel.isComplete()){
	    		errorMsg += "Output Options not entered.\n";
	    		isComplete = false;
	    	}
	    	
	    	if(!isComplete)
	    		JOptionPane.showMessageDialog(contentPane.getParent(),
	    													errorMsg,
	    													"Error!",
	    													JOptionPane.ERROR_MESSAGE);
	    	return isComplete;
	    }
	}
	
	public void reset() {
		SMPanel.reset();
		MCMCPanel.reset();
		outputOptionsPanel.reset();
	}	
}
