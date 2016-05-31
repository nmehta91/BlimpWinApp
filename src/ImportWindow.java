import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class ImportWindow extends JFrame {

	private JPanel contentPane;
	private JTextField MV_Code;
	private JComboBox DelimiterComboBox;
	private SyntaxModel model;
	/**
	 * Launch the application.
	 */
	public static void showWindow() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImportWindow frame = new ImportWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ImportWindow() {
		model = SyntaxModel.getInstance();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 682, 459);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel dataPanel = new JPanel();
		tabbedPane.addTab("Data", null, dataPanel, null);
		dataPanel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Delimiter");
		lblNewLabel.setBounds(28, 25, 41, 14);
		dataPanel.add(lblNewLabel);
		
		JLabel lblMissingValueCode = new JLabel("Missing Value Code");
		lblMissingValueCode.setBounds(28, 65, 91, 14);
		dataPanel.add(lblMissingValueCode);
		
		MV_Code = new JTextField();
		MV_Code.setBounds(131, 65, 86, 20);
		dataPanel.add(MV_Code);
		MV_Code.setColumns(10);
		
		JTextArea rawDataView = new JTextArea();
		rawDataView.setWrapStyleWord(true);
		rawDataView.setLineWrap(true);
		rawDataView.setBounds(285, 11, 356, 165);
		dataPanel.add(rawDataView);
		
		DelimiterComboBox = new JComboBox();
		DelimiterComboBox.setModel(new DefaultComboBoxModel(new String[] {"Comma", "Space"}));
		DelimiterComboBox.setBounds(131, 22, 86, 20);
		dataPanel.add(DelimiterComboBox);
		
		JButton btnImport = new JButton("Import");
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String delimiter;
				if ((String) DelimiterComboBox.getSelectedItem() == "Comma") {
					delimiter = "\\s+";
				} else {
					delimiter = ",";
				}
				System.out.println(DelimiterComboBox.getSelectedItem());
				parseData(delimiter);
			}
		});
		btnImport.setBounds(80, 120, 89, 23);
		dataPanel.add(btnImport);
		
		JPanel variablePanel = new JPanel();
		tabbedPane.addTab("Variable", null, variablePanel, null);
		variablePanel.setLayout(null);
	}
	
	public void parseData(String delimiter) {
		
		ArrayList<String[]> parsedFile = new ArrayList<String[]>();
		
		for(String str : model.importFileContents){
			String[] line = str.split(delimiter);
			parsedFile.add(line);
			System.out.println(parsedFile.get(0)[2]);
		}
		
	}
}
