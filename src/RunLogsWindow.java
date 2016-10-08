import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.omg.CORBA.portable.InputStream;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.JProgressBar;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class RunLogsWindow extends JFrame {

	private JPanel contentPane;
	public JTextArea logTextArea;
	private SwingWorker worker;
	private JProgressBar progressBar;
	private SyntaxModel model;
	private String pathToExe;
	/**
	 * Launch the application.
	 */
	/**
	 * Create the frame.
	 */
	public RunLogsWindow(String pathToExe) {
		setTitle("Output");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 786, 477);
		
		model = SyntaxModel.getInstance();
		this.pathToExe = pathToExe;
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JFrame frame = this;
		ImageIcon img = new ImageIcon("Resources\\blimplogo_32x32.png");
		System.out.println("width: "+ img.getIconWidth() + "height:" + img.getIconHeight());
		frame.setIconImage(img.getImage());
		
		logTextArea = new JTextArea();
		logTextArea.setEditable(false);
		logTextArea.setFont(new Font("Courier", Font.PLAIN, 14));
		
		JScrollPane scrollPane_1 = new JScrollPane(logTextArea);
		
		progressBar = new JProgressBar();
		
		JButton btnSaveOutput = new JButton("Save Output");
		btnSaveOutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser selectedFile = new JFileChooser();
				selectedFile.setSelectedFile(new File(""));
				int saveResult = selectedFile.showSaveDialog(contentPane);
				if (saveResult == selectedFile.APPROVE_OPTION) {
					BufferedWriter writer = null;
					File file = selectedFile.getSelectedFile();
					String filePath = file.getPath();
					if(!filePath.endsWith(".impOut")) {
						filePath += ".impOut";
					}
					try {
						writer = new BufferedWriter(new FileWriter(filePath));
						writer.write(logTextArea.getText());
						writer.close();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(contentPane,
								"Exception while trying to save the file!",
								"Error!",
								JOptionPane.ERROR_MESSAGE);
					}
					
				}
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
					.addGap(5))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addComponent(btnSaveOutput, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 469, Short.MAX_VALUE)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSaveOutput))
					.addGap(20))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void initiateExecution()
	{
		progressBar.setIndeterminate(true);
		progressBar.setVisible(true);
		executeEXE();
	}
	public void executeEXE()
	{
		Process process;
		try {  
			process = new ProcessBuilder("blimp.exe", model.syntaxFilePath).start();
			java.io.InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			
			
			if (worker!=null)
            {
                worker.cancel(true);
            }
			
			 worker = new SwingWorker()
	            {
	                @Override
	                protected Integer doInBackground()//Perform the required GUI update here.
	                {
	                    try
	                    {
	                    	do {
	                    		final String line = br.readLine();
	                    		
	                    		if(line == null)
	                    			break;
	                    		
	                    		logTextArea.append(line+"\n");
	             
	                    	}while(true);
	                    	
	                    }catch(Exception ex){}
	                    return 0;
	                } 
	                
	                @Override
	                protected void done() {
	                	progressBar.setIndeterminate(false);
	                }
	            };
	            worker.execute();//Schedules this SwingWorker for execution on a worker thread.
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	public String createTempDirectory() throws IOException {
		File tempFile =  File.createTempFile("temp-file", "tmp");
		tempFile.deleteOnExit();
		String tempDirectory = tempFile.getParent();
//		Runtime.getRuntime().exec("cmd /c start /B test.bat " + tempDirectory);
		return tempDirectory;
	}
}
