import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import java.awt.Canvas;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import java.awt.Color;
import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.JEditorPane;
import java.awt.SystemColor;

public class AboutPage extends JFrame {

	private JPanel contentPane;
	/**
	 * Create the frame.
	 */
	public AboutPage() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 526, 502);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JFrame frame = this;
		ImageIcon img = new ImageIcon("blimplogo_32x32.png");
		System.out.println("width: "+ img.getIconWidth() + "height:" + img.getIconHeight());
		frame.setIconImage(img.getImage());
		
		RTFEditorKit rtf = new RTFEditorKit();
		
		ImageIcon icon = new ImageIcon("Resources\\Icons\\blimplogo_v3128x128.png");
		JLabel imageLabel = new JLabel("");
		imageLabel.setBounds(186, 11, 120, 124);
		imageLabel.setIcon(icon);
		contentPane.add(imageLabel);
		
		JLabel lblBlimpName = new JLabel("Blimp");
		lblBlimpName.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblBlimpName.setBounds(223, 146, 47, 22);
		contentPane.add(lblBlimpName);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 179, 490, 261);
		contentPane.add(scrollPane);
		
		JEditorPane editorPane = new JEditorPane();
		scrollPane.setViewportView(editorPane);
		editorPane.setBackground(SystemColor.menu);
		editorPane.setEditable(false);
		editorPane.setEditorKit(rtf);
		
		try {
			FileInputStream fi = new FileInputStream("Resources\\Credits.rtf");
			rtf.read(fi, editorPane.getDocument(), 0);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(contentPane,
					"Could not find Credits.rtf file!",
					"Error!",
					JOptionPane.ERROR_MESSAGE);
			WindowEvent close = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
			Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(close);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(contentPane,
					"Could not find Credits.rtf file!",
					"Error!",
					JOptionPane.ERROR_MESSAGE);
			WindowEvent close = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
			Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(close);
		} catch (BadLocationException e) {
			JOptionPane.showMessageDialog(contentPane,
					"Could not find Credits.rtf file!",
					"Error!",
					JOptionPane.ERROR_MESSAGE);
			WindowEvent close = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
			Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(close);
		}
	}
}
