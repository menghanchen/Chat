import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	boolean started = false;
	ServerSocket ss = null;
	
	List <Client> clients = new ArrayList<Client> ();
	
	public static void main(String[] args) {
		new ChatServer().start();
	}
	
	public void start() {
		DataInputStream dis = null;

		try {
			ss = new ServerSocket(8888);
		} catch (BindException e) {
			System.out.println("Port is being used...");
			System.out.println("Please close related program and re-run the server!");
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			started = true;
			while (started) {
				Socket s = ss.accept();
				Client c = new Client(s);
				System.out.println("a client connected");
				new Thread(c).start(); //a new thread
				clients.add(c); 
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	class Client implements Runnable {

		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean beConnected = false;

		public Client(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				beConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		public void send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				clients.remove(this);
				System.out.println("a client quits and be removed form list");
				//e.printStackTrace();
			}
		}

		public void run() {
			try {
				while (beConnected) {
					String str;
					str = dis.readUTF();
System.out.println(str);
					for(int i = 0; i<clients.size(); i++) {
						Client c = clients.get(i);
						c.send(str);
					}
				} 
			} catch (EOFException e) {
System.out.println("client closed!");
			} catch (IOException e) {
				e.printStackTrace();

			}
			
			finally {
				try {
					if (dis != null) dis.close();
					if (dos != null) dos.close();
					if (s != null) s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}	
			}

		}

	}
}