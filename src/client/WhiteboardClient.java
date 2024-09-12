package client;

import java.awt.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import remote.ClientInterface;
import remote.MessageInterface;
import remote.ServerInterface;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.awt.event.ActionEvent;

public class WhiteboardClient extends UnicastRemoteObject implements ClientInterface{

	static ServerInterface server;
	private static final long serialVersionUID = 1L;
	private Boolean isManager;
	private Boolean permission;
	
	// UI
	private JFrame frame;
	private JButton penBtn, lineBtn, circleBtn, ovalBtn, recBtn, eraserBtn, textBtn;
	private JButton resetBtn, saveBtn, saveAsBtn, openBtn;
	private JButton colorBtn, sendBtn;
	private JSlider sizeSlider;
	private int strokeSize;
	private JScrollPane chatArea;
	private JScrollPane userArea;
	private JTextArea showColor;
	private JList<String> users;
	private JList<String> chats;
	private ArrayList<JButton> tools;
	private Whiteboard whiteboardUI;
	
	private DefaultListModel<String> userList;
	private DefaultListModel<String> chatList;
	
	private String clientName;
	private String imageName;
	private String imagePath;
	private Hashtable<String, Point> startPts = new Hashtable<String, Point>();
	private JTextField enterChat;

	
	
	

	protected WhiteboardClient() throws RemoteException {
		userList = new DefaultListModel<>();
		isManager = false;
		permission = true;
		chatList = new DefaultListModel<>();
		tools = new ArrayList<>();
		strokeSize = 1;
		
		
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	// UI creation and function deployment
	public void drawBoard(ServerInterface server) throws RemoteException{
		frame = new JFrame("Collabrate Whiteboard User:" + clientName);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(WhiteboardClient.class.getResource("/icon/huaban.png")));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		Container content = frame.getContentPane();
		
		whiteboardUI = new Whiteboard(clientName, isManager, server);
		
		// button border
		LineBorder selected = new LineBorder(Color.black, 2);
		LineBorder notSelected = new LineBorder(new Color(238,238,238), 2);
		
		// pen button for draw line
		penBtn = new JButton();
		penBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				whiteboardUI.setPaintType("pen");
				for (JButton btn : tools) {
					if (btn == penBtn) {
						btn.setBorder(selected);
					}else {
						btn.setBorder(notSelected);
					}
				}
			}
		});
		penBtn.setIcon(new javax.swing.ImageIcon(WhiteboardClient.class.getResource("/icon/huabi_huaban1.png")));
		
		// straight line button
		lineBtn = new JButton();
		lineBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				whiteboardUI.setPaintType("line");
				for (JButton btn : tools) {
					if (btn == lineBtn) {
						btn.setBorder(selected);
					}else {
						btn.setBorder(notSelected);
					}
				}
			}
		});
		lineBtn.setIcon(new javax.swing.ImageIcon(WhiteboardClient.class.getResource("/icon/zhixian_huaban1.png")));

		// circle button
		circleBtn = new JButton();
		circleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				whiteboardUI.setPaintType("circle");
				for (JButton btn : tools) {
					if (btn == circleBtn) {
						btn.setBorder(selected);
					}else {
						btn.setBorder(notSelected);
					}
				}
			}
		});
		circleBtn.setIcon(new javax.swing.ImageIcon(WhiteboardClient.class.getResource("/icon/yuanquan_huaban1.png")));
		
		// oval button
		ovalBtn = new JButton();
		ovalBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				whiteboardUI.setPaintType("oval");
				for (JButton btn : tools) {
					if (btn == ovalBtn) {
						btn.setBorder(selected);
					}else {
						btn.setBorder(notSelected);
					}
				}
			}
		});
		ovalBtn.setIcon(new javax.swing.ImageIcon(WhiteboardClient.class.getResource("/icon/tuoyuan.png")));

		// rectangle button
		recBtn = new JButton();
		recBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				whiteboardUI.setPaintType("rec");
				for (JButton btn : tools) {
					if (btn == recBtn) {
						btn.setBorder(selected);
					}else {
						btn.setBorder(notSelected);
					}
				}
			}
		});
		recBtn.setIcon(new javax.swing.ImageIcon(WhiteboardClient.class.getResource("/icon/juxinggongju_huaban1.png")));
		
		// eraser button
		eraserBtn = new JButton();
		eraserBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("erazer");
				whiteboardUI.setPaintType("eraser");
				for (JButton btn : tools) {
					if (btn == eraserBtn) {
						btn.setBorder(selected);
					}else {
						btn.setBorder(notSelected);
					}
				}
			}
		});
		eraserBtn.setIcon(new javax.swing.ImageIcon(WhiteboardClient.class.getResource("/icon/xiangpi_huaban1.png")));
		
		// text input button
		textBtn = new JButton();
		textBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("text");
				whiteboardUI.setPaintType("text");
				for (JButton btn : tools) {
					if (btn == textBtn) {
						btn.setBorder(selected);
					}else {
						btn.setBorder(notSelected);
					}
				}
			}
		});
		textBtn.setIcon(new javax.swing.ImageIcon(WhiteboardClient.class.getResource("/icon/wenzi_huaban1.png")));
		
		tools.add(penBtn);
		tools.add(lineBtn);
		tools.add(circleBtn);
		tools.add(ovalBtn);
		tools.add(recBtn);
		tools.add(eraserBtn);
		tools.add(textBtn);
		
		// color selector button
		colorBtn = new JButton();
		colorBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                Color selectedColor = JColorChooser.showDialog(frame, "Choose a Color", Color.BLACK);

                if (selectedColor != null) {
                    //System.out.println("Selected color: " + selectedColor);
                    whiteboardUI.setColor(selectedColor);
                    showColor.setBackground(selectedColor);
                }
            }
		});
		colorBtn.setIcon(new javax.swing.ImageIcon(WhiteboardClient.class.getResource("/icon/huaban.png")));

		
		// slider to change stroke size
		sizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 20, strokeSize);
		sizeSlider.setMajorTickSpacing(5);
        sizeSlider.setPaintTicks(true);
        sizeSlider.addChangeListener((ChangeListener) new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {
                    strokeSize = source.getValue(); // Update stroke size
                    whiteboardUI.setStrokeSize(strokeSize); 
                }
            }
        });

		// chatroom
		chatArea = new JScrollPane(chats);

		// reset and set all whiteboard
		resetBtn = new JButton("New");
		resetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "Reset without save the image?");
				if (option == JOptionPane.YES_OPTION) {
				    // Reset without saving the image
				    whiteboardUI.reset();
				    
				    try {
				    	server.resetAllWhiteboard();
				    }catch(RemoteException re) {
				    	JOptionPane.showMessageDialog(null, "Server is disconnected.");
				    }
				} else {
				    // User chose not to reset without saving
				    // do nothing
				}
			}
		});

		// save image to exist path
		saveBtn = new JButton("Save");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(imageName == null) {
						JOptionPane.showMessageDialog(null, "Please use Save As first");						
					}else {
						ImageIO.write(whiteboardUI.getWhiteboard(), "png", new File(imagePath + imageName));
					}
				}catch (IOException ioe) {
					JOptionPane.showMessageDialog(null, "Fail to save image.");
				}
			}
		});

		// save image to a new path
		saveAsBtn = new JButton("Save as");
		saveAsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FileDialog saveasdialog = new FileDialog(frame, "Save image", FileDialog.SAVE);
					saveasdialog.setVisible(true);
					if(saveasdialog.getFile()!=null) {
						imagePath = saveasdialog.getDirectory();
						imageName = saveasdialog.getFile();
						ImageIO.write(whiteboardUI.getWhiteboard(), "png", new File(imagePath + imageName));
					}
				}catch (IOException ioe) {
					JOptionPane.showMessageDialog(null, "Fail to open image.");
				}
			}
		});

		// open image from a path
		openBtn = new JButton("Open");
		openBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					FileDialog opendialog = new FileDialog(frame, "Open an image", FileDialog.LOAD);
					opendialog.setVisible(true);
					if(opendialog.getFile()!=null) {
						imagePath = opendialog.getDirectory();
						imageName = opendialog.getFile();
						
						BufferedImage img = ImageIO.read(new File(imagePath + imageName));
						whiteboardUI.drawImage(img);
						
						ByteArrayOutputStream imageArray = new ByteArrayOutputStream();
						ImageIO.write(img, "png", imageArray);
						server.managerOpenImage(imageArray.toByteArray());
					}
				}catch (IOException ioe) {
					JOptionPane.showMessageDialog(null, "Fail to open image.");
				}
				whiteboardUI.repaint();
			}
		});

		// hide button to non-manager clients
		if (!isManager) {
			openBtn.setVisible(false);
			resetBtn.setVisible(false);
			saveBtn.setVisible(false);
			saveAsBtn.setVisible(false);
		}
		
		// box to show current color
		showColor = new JTextArea();
		showColor.setBackground(Color.black);

		
		users = new JList<>(userList);
		userArea = new JScrollPane(users);
		
		// allow to kick users
		if(isManager) {
			users.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent ev) {
					@SuppressWarnings("unchecked")
					JList<String> list = (JList<String>)ev.getSource();
					if (ev.getClickCount() == 2) {
						int index = list.locationToIndex(ev.getPoint());
						String selectedUser = list.getModel().getElementAt(index);
						try {
							// not chose manager
							if (! getName().equals(selectedUser)) {
								int option = JOptionPane.showConfirmDialog(frame, "Remove " + selectedUser + "?", "Warning", JOptionPane.YES_NO_OPTION);
								if (option == JOptionPane.YES_OPTION) {
									try {
										server.clientKicked(selectedUser);
										updateClientSet(server.getClientSet());
									}catch (IOException ioe){
										System.err.println("IO error");
									}
								}
							}
						}catch(HeadlessException e) {
							System.err.println("Headless error");
						}catch(RemoteException e) {
							JOptionPane.showMessageDialog(null, "Server offline!");
						}
					}
				}
			});
		}
		
		
		chats = new JList<>(chatList);
		chatArea = new JScrollPane(chats);
		
		enterChat = new JTextField();
		enterChat.setColumns(10);
		
		// send button
		sendBtn = new JButton("Send");
		sendBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(!enterChat.getText().equals("")) {
					try {
						//System.out.println(enterChat.getText());
						server.addChat(clientName + ":" + enterChat.getText());
						// update scroll pane
						SwingUtilities.invokeLater(()->{
							JScrollBar vert = chatArea.getVerticalScrollBar();
							vert.setValue(vert.getMaximum());
						});
					}catch(RemoteException re) {
						JOptionPane.showMessageDialog(null, "Server offline!");
					}
					enterChat.setText("");
				}
			}
		});
		
		users = new JList<>(userList);
		userArea = new JScrollPane(users);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				// manager exit and kick all users
				if (isManager) {
					if(JOptionPane.showConfirmDialog(frame, "Please save your work if needed. Manager exit will cause all user exit.", "Close window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						try {
							server.managerExit();
						}catch (IOException ioe) {
							ioe.printStackTrace();
							
						}finally {
							System.exit(0);
						}
					}
				}else {
					// user exit and update user set
					if(JOptionPane.showConfirmDialog(frame, "User exit.", "Close window?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
						try {
							server.clientExit(getName());;
							updateClientSet(server.getClientSet());
						}catch (IOException ioe) {
							ioe.printStackTrace();
							
						}finally {
							System.exit(0);
						}
					}
				}
			}
		});
		
		// layout
		GroupLayout layout = new GroupLayout(content);
		content.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(penBtn)
						.addComponent(lineBtn)
						.addComponent(recBtn)
						.addComponent(circleBtn)
						.addComponent(textBtn)
						.addComponent(ovalBtn)
						.addComponent(eraserBtn)
						.addComponent(colorBtn)
						
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(whiteboardUI)
						.addComponent(sizeSlider)
						.addComponent(chatArea)
						.addGroup(layout.createSequentialGroup()
								.addComponent(enterChat)
								.addComponent(sendBtn))
						)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(resetBtn)
						.addComponent(openBtn)
						.addComponent(saveBtn)
						.addComponent(saveAsBtn)
						.addComponent(userArea)
						.addComponent(showColor))
				
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGroup(layout.createSequentialGroup()
								.addComponent(penBtn)
								.addComponent(lineBtn)
								.addComponent(recBtn)
								.addComponent(circleBtn)
								.addComponent(textBtn)
								.addComponent(ovalBtn)
								.addComponent(eraserBtn)
								.addComponent(colorBtn)
								
								)
						.addComponent(whiteboardUI)
						.addGroup(layout.createSequentialGroup()
								.addComponent(resetBtn)
								.addComponent(openBtn)
								.addComponent(saveBtn)
								.addComponent(saveAsBtn)
								.addComponent(userArea)
								.addComponent(showColor))
								)
				.addGroup(layout.createSequentialGroup()
						.addComponent(sizeSlider)
						.addComponent(chatArea)
						.addGroup(layout.createParallelGroup()
								.addComponent(enterChat)
								.addComponent(sendBtn)))
				);
		
		layout.linkSize(SwingConstants.HORIZONTAL, resetBtn, saveBtn, saveAsBtn, openBtn);
		layout.linkSize(SwingConstants.HORIZONTAL, penBtn, eraserBtn, lineBtn, circleBtn,recBtn, ovalBtn,colorBtn,textBtn);
		//set visual
		frame.setMinimumSize(new Dimension(800,600));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		
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
		
		// draw text 
	public Shape makeText(Shape shape, Point start) {
		int x = start.x - 5;
		int y = start.y - 20;
		int width = 130;
		int height = 25;
		shape = new RoundRectangle2D.Double(x,y,width,height,15,15);
		return shape;
	}

	
	@Override
	// assign manager
	public void assignManager() throws RemoteException {
		this.isManager = true;
		
	}

	@Override
	// return manager status
	public boolean isManager() throws RemoteException {
		return this.isManager;
	}

	@Override
	// ask for permission
	public boolean askPermission(String name) throws RemoteException {
		if(JOptionPane.showConfirmDialog(frame, name + " wants to join whiteboard.", "Give permission?", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			return true;
		}else {
			return false;
		}
		
	}

	@Override
	public void setPermission(boolean permission) throws RemoteException {
		this.permission = permission;
		
	}
	
	@Override
	public boolean getPermission() throws RemoteException{
		return this.permission;
	}

	@Override
	public void setName(String name) throws RemoteException {
		this.clientName = name;
		
	}

	@Override
	public String getName() throws RemoteException {
		return this.clientName;
	}

	@Override
	public void updateClientSet(Set<ClientInterface> clientSet) throws RemoteException {
		this.userList.removeAllElements();
		for(ClientInterface c : clientSet) {
			try {
				userList.addElement(c.getName());
			}catch (RemoteException re) {
				JOptionPane.showMessageDialog(null, "Fail to update user list"); 
			}
		}
		
	}

	@Override
	public void syncMessage(MessageInterface msg) throws RemoteException {
		// skip self
		if (msg.getName().compareTo(clientName)==0) {
			return;
		}
		Shape shape = null;
		
		// store client and point
		if (msg.getState().equals("start")) {
			startPts.put(msg.getName(), msg.getPoint()); 
		}
		
		Point startPt = (Point) startPts.get(msg.getName());
		
		whiteboardUI.getGraphic().setPaint(msg.getColor());
		whiteboardUI.getGraphic().setStroke(new BasicStroke(msg.getStroke()));
		
		if (msg.getState().equals("draw")) {			
			shape = makeLine(shape, startPt, msg.getPoint());
			startPts.put(msg.getName(),msg.getPoint());
			whiteboardUI.getGraphic().draw(shape);
			whiteboardUI.repaint();
		}
		
		if (msg.getState().equals("end")) {
			switch (msg.getType()) {
			case "line":
				shape = makeLine(shape, startPt, msg.getPoint());
				break;
				
			//case "pen":
				//shape = makeLine(shape, startPt, msg.getPoint());
				//break;
				
			case "rec":
				shape = makeRec(shape, startPt, msg.getPoint());
				break;
				
			case "circle":
				shape = makeCircle(shape, startPt, msg.getPoint());
				break;
				
			case "oval":
				shape = makeOval(shape, startPt, msg.getPoint());
				break;
				
			case "text":
				whiteboardUI.getGraphic().setFont(new Font("TimesRoman", Font.PLAIN, 20));
				whiteboardUI.getGraphic().drawString(msg.getText(), msg.getPoint().x, msg.getPoint().y);
				System.out.println("Sync text" + msg.getText());
				break;
			}
			
			if (!msg.getType().equals("text")) {
				try {
					whiteboardUI.getGraphic().draw(shape);
				}catch (Exception e) {
					
				}
			}
			
			// repaint ui
			whiteboardUI.repaint();
			startPts.remove(msg.getName());
			return;
		}
	}

	@Override
	public void resetWhiteboard() throws RemoteException {
		whiteboardUI.reset();
		
	}

	@Override
	public void closeUI() throws RemoteException {
		// if no permission
		if (!this.permission) {
			Thread t = new Thread(new Runnable(){
				public void run() {
					JOptionPane.showMessageDialog(null, "Fail to get permission, you can not join.");
					System.exit(0);
				}
			});
			t.start();
		}
		// if get kicked
		Thread t = new Thread(new Runnable(){
			public void run() {
				JOptionPane.showMessageDialog(null, "You have been removed from whiteboard.");
				System.exit(0);
			}
		});
		t.start();
		
	}

	@Override
	public void addChat(String text) throws RemoteException {
		//System.out.println(text);
		this.chatList.addElement(text);
		
	}

	@Override
	public byte[] getImage() throws IOException {
		ByteArrayOutputStream imageArray = new ByteArrayOutputStream();
		ImageIO.write(this.whiteboardUI.getWhiteboard(), "png", imageArray);
		return imageArray.toByteArray();
	}

	@Override
	public void openImage(byte[] image) throws RemoteException {
		try {
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(image));
			this.whiteboardUI.drawImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static void main(String args[]) throws MalformedURLException, RemoteException, NotBoundException{
		String hostName = args[0];
		String port = args[1];
		String serverName = "WhiteboardServer";
		
		String serverAddr = "//" + hostName + ":" + port + "/" + serverName;
		
		server = (ServerInterface) Naming.lookup(serverAddr);
		ClientInterface client = new WhiteboardClient();
		
		// when client log in check for name
		boolean validName = false;
		String tempName = "";
		while(validName == false) {
			
			// set a valid temp name
			tempName = JOptionPane.showInputDialog("Enter your name:");
			if (tempName.equals("")){
				JOptionPane.showMessageDialog(null, "You have no choice but enter name loser!");
			}else{
				validName = true;
			}
			
			for(ClientInterface c : server.getClientSet()) {
				if (c.getName().equals(tempName) || c.getName().equals("*" + tempName)) {
					validName = false;
					JOptionPane.showMessageDialog(null, "Name has been taken, change for another one or you shall not pass!");
				}
			}
		}
		client.setName(tempName);
		
		// register client into server
		try {
			if (client.getPermission()) {
				server.register(client);
				System.out.println("registered");
			}
		}catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Fail to register. Server offline.");
		}
		
		// start UI	
		System.out.println("starting ui");
		client.drawBoard(server);
		
		
		
	}
}









































