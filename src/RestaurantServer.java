import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.NumberFormat;
import java.util.Scanner;

public class RestaurantServer {
	private int port;

	public RestaurantServer(int port) {
		this.port = port;
	}

	public void runForever() {
		try (ServerSocket serverSocket = new ServerSocket(this.port)) {
			System.out.println("listening on port " + this.port);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("connection from " + clientSocket.getInetAddress().getHostAddress());
				Thread client = new Thread(new ClientTask(clientSocket));
				client.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		RestaurantServer server = new RestaurantServer(port);
		server.runForever();
	}

}

class ClientTask implements Runnable {
	private Socket socket;

	public ClientTask(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {

			File Log = new File("Finallog.txt");
			if (!Log.exists()) {
				Log.createNewFile();
			}
			FileWriter fw = new FileWriter(Log.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Client ip: " + socket.getInetAddress().getHostAddress());

			DataInputStream input = new DataInputStream(socket.getInputStream());
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			while (true) {
				double quantityBurger = input.readDouble();
				double quantityFries = input.readDouble();
				double quantityShake = input.readDouble();

				if (quantityBurger + quantityFries + quantityShake < 0) {
					break;
				}

				Order order = new Order(quantityBurger, quantityFries, quantityShake);
				String newline = System.lineSeparator();
				NumberFormat formatter = NumberFormat.getCurrencyInstance();
				String report = "OrderId: " + order.getOrderID() + newline + "Total: "
						+ formatter.format(order.getTotal()) + newline;
				output.writeUTF(report);
				output.flush();

				bw.newLine();
				bw.write(report);
				bw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
