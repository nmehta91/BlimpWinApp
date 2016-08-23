import java.awt.BorderLayout;
import java.awt.EventQueue;
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

public class RunLogsWindow extends JFrame {

	private JPanel contentPane;
	private JTextArea logTextArea;
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
		setResizable(false);
		setTitle("Output");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 791, 490);
		
		model = SyntaxModel.getInstance();
		this.pathToExe = pathToExe;
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		logTextArea = new JTextArea();
		
		JScrollPane scrollPane_1 = new JScrollPane(logTextArea);
		scrollPane_1.setBounds(5, 5, 760, 410);

		contentPane.add(scrollPane_1, BorderLayout.CENTER);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(602, 426, 163, 14);
		contentPane.add(progressBar);
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
			
			process = new ProcessBuilder(createTempDirectory() + "\\Blimp\\blimp.exe", model.syntaxFilePath).start();
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
		Runtime.getRuntime().exec("cmd /c start /B test.bat " + tempDirectory);
		return tempDirectory;
	}
}
