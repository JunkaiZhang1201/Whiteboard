package client;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.rmi.RemoteException;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.swing.*;

import remote.ServerInterface;

public class Whiteboard extends JComponent{
	
	private static final long serialVersionUID = 1L;
	
	private String userName, paintType, text;
	private Boolean isManager;
	private Point startPt, endPt;
	private Color color;
	private BasicStroke stroke;
	private int strokeSize;
	
	private BufferedImage img, lastImg;
	private Graphics2D g;
	
	private ServerInterface server;
	
	public Whiteboard(String user, Boolean isManager, ServerInterface server) {
		this.userName = user;
		this.isManager = isManager;
		this.server = server;
		this.color = Color.black;
		this.paintType = "pen";
		this.text = "";
		this.stroke = new BasicStroke(1);
		this.strokeSize = 1;
		
		// allow real time image update
		setDoubleBuffered(false);
		
		// record and send mouse click to server
		addMouseListener((MouseListener) new MouseAdapter() { 
			public void mousePressed(MouseEvent e) {
				// get start point
				startPt = e.getPoint();
				saveWhiteboard();
				
				try {
					MessageWrapper msg = new MessageWrapper("start", userName, paintType, color, startPt, text, strokeSize);
					server.broadcast(msg);
				}catch(RemoteException re) {
					re.printStackTrace();
					JOptionPane.showMessageDialog(null, "Server offline.1");
				}
			}
		});
		
		// record and send shape to server, draw on local client
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				// get end point
				endPt = e.getPoint();

				Shape shape = null;
				if (g != null) {
					g.setPaint(color);
					g.setStroke(new BasicStroke(strokeSize));
					
					// set switch on different types
					switch(paintType) {
					
						// pen tool, draw line and broadcast to clients
						case "pen":
							shape = makeLine(shape,startPt,endPt);
							startPt = endPt;
							try {
								MessageWrapper msg = new MessageWrapper("draw", userName, paintType, color, endPt, "", strokeSize);
								server.broadcast(msg);
							}catch(RemoteException re) {
								re.printStackTrace();
								JOptionPane.showMessageDialog(null, "Server offline.");
							}
							break;
					
							// eraser tool
						case "eraser":
							shape = makeLine(shape,startPt,endPt);
							startPt = endPt;
							g.setPaint(Color.white);
							try {
								MessageWrapper msg = new MessageWrapper("draw", userName, paintType, Color.white, endPt, "", strokeSize);
								server.broadcast(msg);
							}catch(RemoteException re) {
								re.printStackTrace();
								JOptionPane.showMessageDialog(null, "Server offline.");
							}
							
							break;
					
							// line tool
						case "line":
							paintLastImg();
							shape = makeLine(shape, startPt, endPt);
							break;
					
							// rectangle tool
						case "rec":
							paintLastImg();
							shape = makeRec(shape, startPt, endPt);
							break;
					
							// circle tool
						case "circle":
							paintLastImg();
							shape = makeCircle(shape, startPt, endPt);
							break;
					
							// oval tool
						case "oval":
							paintLastImg();
							shape = makeOval(shape, startPt, endPt);
							break;
							
							// text tool
						case "text":
							paintLastImg();
							g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
							g.drawString("Enter text:", endPt.x, endPt.y);
							shape = makeText(shape, startPt);
							Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, new float[] {3}, 0);
							g.setStroke(dashed);
							break;
					
						default:
							JOptionPane.showMessageDialog(null, "Client paint type error!");
					}
					
					g.draw(shape);
					
					repaint();
					
					g.setColor(color);
					
					
				}
			}
		});
		
		// record and send shape to server, draw when released
		addMouseListener((MouseListener) new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				endPt = e.getPoint();
				Shape shape = null;
				text = "";
				
				if (g!=null) {
					switch (paintType) {
					
						case "line":
							shape = makeLine(shape, startPt, endPt);
							break;
						
						case "pen":
							shape = makeLine(shape, startPt, endPt);
							break;
						
						case "rec":
							shape = makeRec(shape, startPt, endPt);
							break;
						
						case "circle":
							shape = makeCircle(shape, startPt, endPt);
							break;
						
						case "oval":
							shape = makeOval(shape, startPt, endPt);
							break;
							
						case "text":
							text = JOptionPane.showInputDialog("Input text:");
							if (text == null) {
								text = "";
							}
						
							paintLastImg();
							g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
							g.drawString(text, endPt.x, endPt.y);
							g.setStroke(stroke);
							break;
					}
					
					if (! paintType.equals("text")) {
						try {
							g.draw(shape);
						}catch(NullPointerException ne) {
							// do nothing
						}
					}
					
					repaint();
					
					try {
						MessageWrapper msg = new MessageWrapper("end", userName, paintType, color, endPt, text, strokeSize);
						server.broadcast(msg);
					}catch(RemoteException re) {
						JOptionPane.showMessageDialog(null, "Server offline.");
					}
					
				}
			}
		});
	}


	// methods for painting shapes on whiteboard
	// synchronize when join server
	protected void paintComponent(Graphics graphics) {
		if (img == null) {
			// create a white image for manager
			if (isManager) {
				img = new BufferedImage(600,400,BufferedImage.TYPE_INT_RGB);
				g = (Graphics2D) img.getGraphics();
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setPaint(color);
				reset();
			}else {
				try {
					byte[] rawImg = server.getImage();
					img = ImageIO.read(new ByteArrayInputStream(rawImg));
					g = (Graphics2D) img.getGraphics();
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g.setPaint(color);
				}catch(IOException ioe) {
					JOptionPane.showMessageDialog(null, "Unable to receive image.");
				}
			}
		}
		graphics.drawImage(img, 0,0,null);
	}
	
	public Color getCurrColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public String getCurrPaintType() {
		return paintType;
	}
	
	public Stroke getStroke() {
		return stroke;
	}
	
	public int getStrokeSize() {
		return strokeSize;
	}
	
	public Graphics2D getGraphic() {
		return g;
	}
	
	public BufferedImage getWhiteboard() {
		saveWhiteboard();
		return lastImg;
	}
	
	// reset whiteboard
	public void reset() {
		g.setPaint(Color.white);
		g.fillRect(0, 0, 600, 400);
		g.setPaint(color);
		repaint();
	}
	
	// save image
	public void saveWhiteboard() {
		ColorModel cm = img.getColorModel();
		WritableRaster raster = img.copyData(null);
		lastImg = new BufferedImage(cm, raster, false, null);
	}
	
	// draw last image
	public void paintLastImg() {
		drawImage(lastImg);		
	}


	public void drawImage(BufferedImage img) {
		g.drawImage(img, null, 0, 0);
		
	}
	
	//setter for paint type
	public void setPaintType(String type) {
		paintType = type;
	}
	
	// draw line
	public Shape makeLine(Shape shape, Point start, Point end) {
		shape = new Line2D.Double(start.x, start.y, end.x, end.y);
		return shape;
	}
	
	// draw rectangle
	public Shape makeRec(Shape shape, Point start, Point end) {
		int x = Math.min(start.x, end.x);
		int y = Math.min(start.y, end.y);
		int width = Math.abs(start.x - end.x);
		int height = Math.abs(start.y - end.y);
		shape = new Rectangle2D.Double(x,y,width,height);
		return shape;
	}
	
	// draw Circle
	public Shape makeCircle(Shape shape, Point start, Point end) {
		int x = Math.min(start.x, end.x);
		int y = Math.min(start.y, end.y);
		int width = Math.abs(start.x - end.x);
		int height = Math.abs(start.y - end.y);
		shape = new Ellipse2D.Double(x,y,Math.max(width, height),Math.max(width, height));
		return shape;
	}
	
	// draw Oval
	public Shape makeOval(Shape shape, Point start, Point end) {
		int x = Math.min(start.x, end.x);
		int y = Math.min(start.y, end.y);
		int width = Math.abs(start.x - end.x);
		int height = Math.abs(start.y - end.y);
		shape = new Ellipse2D.Double(x,y,width,height);
		return shape;
	}
	
	public Shape makeText(Shape shape, Point start) {
		int x = start.x - 5;
		int y = start.y - 20;
		int width = 130;
		int height = 25;
		shape = new RoundRectangle2D.Double(x,y,width,height,15,15);
		return shape;
	}


	public void setStrokeSize(int strokeSize) {
		this.strokeSize = strokeSize;
		
	}
}
