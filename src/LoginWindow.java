import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class LoginWindow extends JFrame implements ActionListener {

	Connection con = null;
	Statement st = null;
	ResultSet rs = null;
	JLabel userLabel;
	JLabel passwordLabel;
	JButton loginButton, registerButton;
	JTextField userText;
	JPasswordField passwordText;

	public static void main(String[] args) {
		new LoginWindow().launchFrame();
	}

	public void launchFrame() {
		setTitle("Login");
		setLocation(400, 300);
		setSize(300, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		add(panel);
		placeComponents(panel);

		setVisible(true);
	}

	public void placeComponents(JPanel panel) {

		panel.setLayout(null);

		userLabel = new JLabel("User");
		userLabel.setBounds(10, 10, 80, 25);
		panel.add(userLabel);

		userText = new JTextField(20);
		userText.setBounds(100, 10, 160, 25);
		panel.add(userText);

		passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);
		panel.add(passwordLabel);

		passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 40, 160, 25);
		panel.add(passwordText);

		loginButton = new JButton("login");
		loginButton.setBounds(10, 80, 80, 25);
		panel.add(loginButton);

		registerButton = new JButton("register");
		registerButton.setBounds(180, 80, 80, 25);
		panel.add(registerButton);

		loginButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (userText.getText().length() == 0) // Checking for empty field
			JOptionPane.showMessageDialog(null, "Empty fields detected ! Please fill up all fields");
		else if (passwordText.getPassword().length == 0) // Checking for empty
															// field
			JOptionPane.showMessageDialog(null, "Empty fields detected ! Please fill up all fields");
		else {
			String username = userText.getText();
			char[] pass = passwordText.getPassword();
			String password = new String(pass);
			try {
				if (validate_login(username, password)) {
					// JOptionPane.showMessageDialog(null, "Login
					// sucessfully!");
					this.setVisible(false);
					new ChatClient().launchFrame(username);

				} else {
					JOptionPane.showMessageDialog(null,
							"Incorrect user name or password..Try Again with correct detail");
				}
			} catch (HeadlessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private static boolean validate_login(String username, String password) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(
				"jdbc:mysql://127.0.0.1:3306/chat?characterEncoding=UTF-8&useSSL=false", "megan", "menghan120");
		PreparedStatement ps = con.prepareStatement("select * from login where username=? and password=?");
		ps.setString(1, username);
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();
		if (rs.next())
			return true;
		else
			return false;
	}
}