package remote;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface ServerInterface extends Remote{
	// register new client
	public void register(ClientInterface client) throws RemoteException;
	
	// clientList getter
	public Set<ClientInterface> getClientSet() throws RemoteException;
	
	// broadcast to all clients in clientList
	public void broadcast(MessageInterface msg) throws RemoteException;
	
	// reset all whiteboard
	public void resetAllWhiteboard() throws RemoteException;
	
	// client exit
	public void clientExit(String clientName) throws RemoteException;
	
	// client kicked out
	public void clientKicked(String clientName) throws RemoteException;
	
	// manager exit
	public void managerExit() throws IOException, RemoteException;
	
	// add chat to chatroom
	public void addChat(String text) throws RemoteException;
	
	// send image to client
	public byte[] getImage() throws IOException, RemoteException;
	
	// manager open new image
	public void managerOpenImage(byte[] image) throws IOException, RemoteException;
	
	
}
