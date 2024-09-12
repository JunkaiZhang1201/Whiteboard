package server;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;

import remote.ServerInterface;

public class Server {
	
	public static void main(String[] args) {
		try {
			ServerInterface server = new WhiteboardServer();
			// create registry and name
			Registry registry = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
			
			registry.bind("WhiteboardServer", server);
			System.out.println("Whiteboard server ready");
			JOptionPane.showMessageDialog(null, "Whiteboard server ready...");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
