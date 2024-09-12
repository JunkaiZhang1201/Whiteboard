package remote;

import java.awt.Color;
import java.awt.Point;
import java.awt.Stroke;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageInterface extends Remote{
	
	// getter for painting information
	public String getState() throws RemoteException;
	public String getName() throws RemoteException;
	public String getText() throws RemoteException;
	public String getType() throws RemoteException;
	public Color getColor() throws RemoteException;
	public Point getPoint() throws RemoteException;
	public int getStroke() throws RemoteException;
	

}
