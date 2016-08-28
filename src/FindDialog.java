import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Document;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class FindDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextArea syntaxEditor;
	private int pos;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			FindDialog dialog = new FindDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public FindDialog(JTextArea syntaxEditor) {
		setTitle("Find");
		setBounds(100, 100, 408, 132);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		this.syntaxEditor = syntaxEditor;
		{
			textField = new JTextField();
			textField.setBounds(95, 20, 177, 20);
			contentPanel.add(textField);
			textField.setColumns(10);
		}
		{
			JButton okButton = new JButton("Find Next");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String find = textField.getText().toLowerCase();
					syntaxEditor.requestFocusInWindow();
					if(find != null && find.length() > 0) {
						Document document = syntaxEditor.getDocument();
						 int findLength = find.length();
						 
			                try {
			                    boolean found = false;
			                    if (pos + findLength > document.getLength()) {
			                        pos = 0;
			                    }
			                while (pos + findLength <= document.getLength()) {
			                    String match = document.getText(pos, findLength).toLowerCase();
			                    if (match.equals(find)) {
			                        found = true;
			                        break;
			                    }
			                    pos++;
			                }
			                if (found) {
			                    Rectangle viewRect = syntaxEditor.modelToView(pos);
			                    syntaxEditor.scrollRectToVisible(viewRect);
			                    syntaxEditor.setCaretPosition(pos + findLength);
			                    syntaxEditor.moveCaretPosition(pos);
			                    pos += findLength;
			                }
			            } catch (Exception exp) {
			                exp.printStackTrace();
			            }
					}
				}
			});
			okButton.setBounds(282, 20, 93, 23);
			contentPanel.add(okButton);
			okButton.setActionCommand("OK");
			getRootPane().setDefaultButton(okButton);
		}
		{
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			cancelButton.setBounds(282, 54, 93, 20);
			contentPanel.add(cancelButton);
			cancelButton.setActionCommand("Cancel");
		}
		
		JLabel lblNewLabel = new JLabel("Find What:");
		lblNewLabel.setBounds(21, 23, 75, 14);
		contentPanel.add(lblNewLabel);
	}
}
