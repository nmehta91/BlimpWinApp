import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.omg.CORBA.portable.InputStream;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class RunLogsWindow extends JFrame {

	private JPanel contentPane;
	private JTextArea logTextArea;
	private SwingWorker worker;
	/**
	 * Launch the application.
	 */
	/**
	 * Create the frame.
	 */
	public RunLogsWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 596, 354);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		logTextArea = new JTextArea();
		
		JScrollPane scrollPane_1 = new JScrollPane(logTextArea);

		contentPane.add(scrollPane_1, BorderLayout.CENTER);
	}
	
	public void initiateExecution()
	{
		executeEXE();
	}
	public void executeEXE()
	{
		Process process;
		try {
			process = new ProcessBuilder("C:\\Users\\Blimp\\Desktop\\sample.exe").start();
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
	                    		Thread.sleep(500);
	                    	}while(true);
	                    	
	                    }catch(Exception ex){}
	                    return 0;
	                }       
	            };
	            worker.execute();//Schedules this SwingWorker for execution on a worker thread.
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}

}
