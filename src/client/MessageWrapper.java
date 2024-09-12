package client;

import java.awt.Color;
import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import remote.MessageInterface;

public class MessageWrapper extends UnicastRemoteObject implements MessageInterface{
	
	private static final long serialVersionUID = 1L;
	
	private String paintType;
	private Color paintColor;
	private Point paintPoint;
	private String clientName;
	private String text;
	private String state;
	private int stroke;

	protected MessageWrapper(String state, String clientName, String paintType, Color paintColor, Point paintPoint, String text, int strokeSize) throws RemoteException {
		this.state = state;
		this.clientName = clientName;
		this.paintType = paintType;
		this.paintColor = paintColor;
		this.paintPoint = paintPoint;
		this.text = text;
		this.stroke = strokeSize;
	}

	@Override
	public String getState() throws RemoteException {
		return this.state;
	}

	@Override
	public String getName() throws RemoteException {
		return this.clientName;
	}

	@Override
	public String getText() throws RemoteException {
		return this.text;
	}

	@Override
	public String getType() throws RemoteException {
		return this.paintType;
	}

	@Override
	public Color getColor() throws RemoteException {
		return this.paintColor;
	}

	@Override
	public Point getPoint() throws RemoteException {
		return this.paintPoint;
	}
	
	@Override
	public int getStroke() throws RemoteException {
		return this.stroke;
	}

}
