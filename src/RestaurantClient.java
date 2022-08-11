import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.NumberFormat;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class RestaurantClient {
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private JTextArea responseText;
	private JTextField quantity;
	private JTextField quantity1;
	private JTextField quantity2;
	private Socket socket;
	private String host;
	private int port;
	private boolean connected = false;
	NumberFormat formatter = NumberFormat.getCurrencyInstance();
	
	public RestaurantClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void runForever() {
		JFrame frame = new JFrame();
		frame.setSize(250,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		GridLayout layout = new GridLayout(4, 2);
		panel.setLayout(layout);

		quantity = new JTextField();
		panel.add(new JLabel("Burger " + formatter.format(5.00)));
		panel.add(quantity);

		quantity1 = new JTextField("",5);
		panel.add(new JLabel("Fries " +  formatter.format(2.00)));
		panel.add(quantity1);

		quantity2 = new JTextField("",5);
		panel.add(new JLabel("Shake " + formatter.format(3.50)));
		panel.add(quantity2);
		panel.add(new JLabel(""));
		JButton submit = new JButton("Send Order");

		submit.addActionListener(e -> sendToServer());

		panel.add(submit);

		responseText = new JTextArea(20, 60);

		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(BorderLayout.NORTH, panel);
		frame.getContentPane().add(BorderLayout.CENTER, responseText);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				try {
					if (connected) {
						// notify server we're done chatting
						outputStream.writeDouble(-1);
						outputStream.writeDouble(-1);
						// clean up the resources
						outputStream.close();
						inputStream.close();
						socket.close();
					}
				} catch (IOException e1) {
				}
			};
		});
		frame.setVisible(true);
	}

	private void sendToServer() {
		try {
			if (!connected) {
				socket = new Socket(host, port);
				inputStream = new DataInputStream(socket.getInputStream());
				outputStream = new DataOutputStream(socket.getOutputStream());
				connected = true;
			}

			outputStream.writeDouble(Double.parseDouble(quantity.getText().trim()));

			outputStream.writeDouble(Double.parseDouble(quantity1.getText().trim()));
			
			outputStream.writeDouble(Double.parseDouble(quantity2.getText().trim()));

			outputStream.flush();
			String report = inputStream.readUTF();
			responseText.append(report + "\n");
		} catch (Exception e) {
			responseText.append(e.getMessage());
		}
	}

	// click

	public static void main(String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		RestaurantClient client = new RestaurantClient(host, port);
		client.runForever();
	}
}