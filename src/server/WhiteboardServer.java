package server;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

import remote.ClientInterface;
import remote.MessageInterface;
import remote.ServerInterface;

public class WhiteboardServer extends UnicastRemoteObject implements ServerInterface, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private ClientManager clientManager;
	
	protected WhiteboardServer() throws RemoteException {
		this.clientManager = new ClientManager(this);
	}
	
	// register a new client and ask for manager permission 
	public void register(ClientInterface client) throws RemoteException {
		// set first client manager
		if (this.clientManager.isEmpty()) {
			client.assignManager();
			System.out.println("manager assigned");
		}
		
		// check permission to join
		boolean permission = true;
		for (ClientInterface c: this.clientManager) {
			if (c.isManager()) {
				try {
					permission = c.askPermission(client.getName());
				}catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (!permission) {
			try {
				client.setPermission(permission);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		// indicate manager name
		if(client.isManager()) {
			client.setName("*" + client.getName()); 
		}
		
		this.clientManager.clientAdd(client);
		
		// update client set for all clients
		for (ClientInterface c : this.clientManager) {
			c.updateClientSet(this.clientManager.getClientSet());
		}
		
		
	}
	
	// getter of client set
	public Set<ClientInterface> getClientSet() throws RemoteException {
		return this.clientManager.getClientSet();
	}
	
	// broadcast message to all clients
	public void broadcast(MessageInterface msg) throws RemoteException {
		for (ClientInterface c : this.clientManager) {
			c.syncMessage(msg);
		}
	}
	
	// manager call, reset all clients whiteboared
	public void resetAllWhiteboard() throws RemoteException {
		for (ClientInterface c : this.clientManager) {
			c.resetWhiteboard();
		}
		
	}
	
	// client exit call and message to all clients
	public void clientExit(String clientName) throws RemoteException {
		for (ClientInterface c : this.clientManager) {
			if (c.getName().equals(clientName)) {
				this.clientManager.clientRemove(c);
				System.out.println(clientName + "has left.");
			}
		}
		
		// update to all clients
		for (ClientInterface c : this.clientManager) {
			c.updateClientSet(this.clientManager.getClientSet());
		}
		
	}
	
	// client kicked out call and message to all clients
	public void clientKicked(String clientName) throws RemoteException {
		for (ClientInterface c : this.clientManager) {
			if (c.getName().equals(clientName)) {
				this.clientManager.clientRemove(c);
				System.out.println(clientName + "has been kicked out.");
				c.closeUI();
			}
		}
		
		// update to all clients
		for (ClientInterface c : this.clientManager) {
			c.updateClientSet(this.clientManager.getClientSet()); 
		}
		
	}
	
	// manager exit and message to all clients
	public void managerExit() throws IOException, RemoteException {
		System.out.println("Manager closed whiteboard");
		for (ClientInterface c : this.clientManager) {
			this.clientManager.clientRemove(c);
			c.closeUI();
		}
		
	}
	
	// update all clients chat with text
	public void addChat(String text) throws RemoteException {
		for (ClientInterface c : this.clientManager) {
			try {
				c.addChat(text);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// manager open exist image
	public byte[] getImage() throws IOException, RemoteException {
		byte[] image = null;
		for (ClientInterface c : this.clientManager) {
			if (c.isManager()) {
				image = c.getImage();
			}
		}
		return image;
	}
	
	// manager open exist image and broadcast to all clients
	public void managerOpenImage(byte[] image) throws IOException, RemoteException {
		for (ClientInterface c : this.clientManager) {
			if (c.isManager() == false) {
				c.openImage(image);
			}
		}		
	}

}
