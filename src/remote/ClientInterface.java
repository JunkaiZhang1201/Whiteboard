package remote;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface ClientInterface extends Remote{
	
	// assign manager to whiteboard
	public void assignManager() throws RemoteException;
	
	// check manager
	public boolean isManager() throws RemoteException;
	
	// ask manager for permission
	public boolean askPermission(String name) throws RemoteException;
	
	// set permission for client
	public void setPermission(boolean permission) throws RemoteException;
	
	// get permission for client
	public boolean getPermission() throws RemoteException;
	
	// set client name
	public void setName(String name) throws RemoteException;
	
	// get client name
	public String getName() throws RemoteException;
	
	// update client set
	public void updateClientSet(Set<ClientInterface> clientSet) throws RemoteException;
	
	// sync message for all clients
	public void syncMessage(MessageInterface msg) throws RemoteException;
	
	// reset whiteboard
	public void resetWhiteboard() throws RemoteException;

	public void closeUI() throws RemoteException;
	
	// add text to chat
	public void addChat(String text) throws RemoteException;
	
	// return image in byte
	public byte[] getImage() throws IOException;
	
	// open image by byte
	public void openImage(byte[] image) throws RemoteException;
	
	// UI creation and function deployment
	public void drawBoard(ServerInterface server) throws RemoteException;

	

}
