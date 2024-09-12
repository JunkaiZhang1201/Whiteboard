package server;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import remote.ClientInterface;

public class ClientManager implements Iterable<ClientInterface>{
	
	// manage client by client set
	private Set<ClientInterface> clientSet;
	
	// constructor
	public ClientManager(WhiteboardServer server) {
		this.clientSet = Collections.newSetFromMap(new ConcurrentHashMap<ClientInterface, Boolean>());
	}
	
	// client set getter
	public Set<ClientInterface> getClientSet(){
		return this.clientSet;
	}
	
	// add new client
	public void clientAdd(ClientInterface client) {
		this.clientSet.add(client);
	}
	
	// remove client
	public void clientRemove(ClientInterface client) {
		this.clientSet.remove(client);
	}
	
	// check set empty
	public boolean isEmpty() {
		return this.clientSet.size() == 0;
	}
	
	@Override
	public Iterator<ClientInterface> iterator() {
		return clientSet.iterator();
	}
}
