import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.*;
import java.io.*;

public class ChatClient extends Frame {

	Socket s = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	private boolean bConnected = false;

	TextField tfTxt = new TextField();
	TextArea taContent = new TextArea();
	Thread tRecv = new Thread(new RecvThread());
/*
	public static void main(String[] args) {

		new ChatClient().launchFrame();

	}
	*/

	public void launchFrame(String s) {
		setTitle(s);
		setLocation(400, 300);
		this.setSize(300, 300);
		add(tfTxt, BorderLayout.SOUTH);
		add(taContent, BorderLayout.NORTH);
		pack();

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				// f.setVisible(false);
				disconnect();
				System.exit(0);
			}
		});

		tfTxt.addActionListener(new TFListener());
		setVisible(true);

		connect();
		
		tRecv.start();
	}

	public void connect() {
		try {
			s = new Socket("127.0.0.1", 8888);
System.out.println("connectd");

			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			bConnected = true;

		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void disconnect() {
		/*
		try {
			bConnected = false;
			tRecv.join();  //make sure the thread is end
		} catch (InterruptedException e) {
				e.printStackTrace();
		} finally {
			try {
				dos.close();
			    dis.close();
			    s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		*/
		try {
			dos.close();
		    dis.close();
		    s.close();
	    } catch (IOException e) {
		    e.printStackTrace();
	    }
		
			

	}

	private class TFListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String str = tfTxt.getText().trim();
			//taContent.setText(str);
			tfTxt.setText("");

			try {
				// System.out.println(s);

				dos.writeUTF(str);
				dos.flush();
				// dos.close();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private class RecvThread implements Runnable {
		public void run() {
			try {
				while (bConnected) {
					String str = dis.readUTF();
					taContent.setText(taContent.getText() + str + '\n');
				}
			} catch (SocketException e) {
		    	System.out.println("exit");
		    } catch (EOFException e) {
		    	System.out.println("exit");
		    } catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
