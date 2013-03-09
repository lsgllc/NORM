package com.txdot.isd.rts.client.general.ui;

import java.util.Vector;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.txdot.isd.rts.services.exception.RTSException;

/*
 * RTSDynamicMenu.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		10/12/2006	Created Class. Task 42.
 * 							defect 8900 Ver. Exempts
 * ---------------------------------------------------------------------
 */

/**
 * An implementation of <code>JMenu</code>.
 * <p>
 * This allows a <code>JMenu</code> to be loaded dynamically at runtime 
 * with properties from a given xml file.
 * <p>
 * This class can handle JMenu, JMenuItem and JSeparator.
 * Using: <code><Menu></code> will add a new menu.
 * Using: <code><Separator /></code> will add a JSeparator.
 * Using: <code><MenuItem></code> will add a JMenuItem.
 * <p>
 * <code>JMenu</code> - Handles the following properties:
 * <br>Text, Mnemonic
 * <p>
 * <code>JMenuItem</code> - Handles the following properties:
 * <br>Text, Mnemonic, Accelerator, ActionCommand
 * <p>
 * See KeyStroke.getKeyStroke() for the different combinations that
 * can be used for Accelerators. ie. ctrl alt M
 * <p>
 * 
 * @version	Exempts			10/12/2006
 * @author	Jeff Seifert
 * <br>Creation Date:		10/12/2006 10:30:00
 * @see JMenu
 * @see JMenuItem
 * @see JSeparator
 */

public class RTSDynamicMenu extends JMenu
{
	private static final String ACCELERATOR_TAG = "Accelerator";
	private static final String ACTION_CMD_TAG = "ActionCommand";
	private static final String MENU_ITEM_TAG = "MenuItem";
	private static final String MENU_TAG = "Menu";
	private static final String MENU_XML_FILE = "/cfg/menu.xml";
	private static final String MNEMONIC_TAG = "Mnemonic";
	private static final String SEPARATOR_TAG = "Separator";
	private static final String TEXT_TAG = "Text";

	/**
	 * RTSDynamicMenu.java Constructor
	 * 
	 * @param asMenuName String - Menu name to load.
	 * @param avActionComponents Vector 
	 * 		- Used to add to Action Listeners list.
	 */
	public RTSDynamicMenu(String asMenuName, Vector avActionComponents)
	{
		super();
		loadDynamicMenu(asMenuName, avActionComponents);
	}

	/**
	 * Used to test the menu.xml changes without having to launch the
	 * RTS application.  This will create a JFrame + MenuBar + The menus
	 * in the menu.xml file.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			JFrame laTestFrame = new JFrame();
			JMenuBar laTestMenuBar = new JMenuBar();

			Document laXMLDoc =
				DocumentBuilderFactory
					.newInstance()
					.newDocumentBuilder()
					.parse(
					RTSDynamicMenu
						.class
						.getResource(MENU_XML_FILE)
						.openStream());
			// Get all of the menu tags
			NodeList laRefTypeNodeList =
				laXMLDoc.getElementsByTagName(MENU_TAG);
			for (int j = 0; j < laRefTypeNodeList.getLength(); j++)
			{
				Node laRefTypeNode = laRefTypeNodeList.item(j);
				if (laRefTypeNode != null
					&& laRefTypeNode.getNodeType() == Node.ELEMENT_NODE
					&& laRefTypeNode
						.getAttributes()
						.getNamedItem(TEXT_TAG)
						.getNodeValue()
						!= null)
				{
					RTSDynamicMenu laDynamicMenu =
						new RTSDynamicMenu(
							laRefTypeNode
								.getAttributes()
								.getNamedItem(TEXT_TAG)
								.getNodeValue(),
							new Vector());
					laTestMenuBar.add(laDynamicMenu);
				}
			}
			laTestFrame.setJMenuBar(laTestMenuBar);
			laTestFrame
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laTestFrame.show();
			java.awt.Insets insets = laTestFrame.getInsets();
			laTestFrame.setSize(
				laTestFrame.getWidth() + insets.left + insets.right,
				laTestFrame.getHeight() + insets.top + insets.bottom);
			laTestFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			laTestFrame.setVisible(true);
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	 * Loads a dynamic JMenu.  The menu is driven from the contents
	 * of the menu.xml file.
	 * 
	 * @param asMenuName String 
	 * @param avActionComponents Vector
	 */
	private void loadDynamicMenu(
		String asMenuName,
		Vector avActionComponents)
	{
		try
		{
			// Load the XML document as a resource
			Document laXMLDoc =
				DocumentBuilderFactory
					.newInstance()
					.newDocumentBuilder()
					.parse(
					RTSDynamicMenu
						.class
						.getResource(MENU_XML_FILE)
						.openStream());

			// Loop through all of the menus find the one that matches
			// the menu name passed.			
			NodeList laMenuNodeList =
				laXMLDoc.getElementsByTagName(MENU_TAG);
			for (int j = 0; j < laMenuNodeList.getLength(); j++)
			{
				Node laMenuNode = laMenuNodeList.item(j);
				// If the menu matches the Name passed.
				if (laMenuNode != null
					&& laMenuNode.getNodeType() == Node.ELEMENT_NODE
					&& laMenuNode
						.getAttributes()
						.getNamedItem(TEXT_TAG)
						.getNodeValue()
						.equals(
						asMenuName))
				{
					setMenuAttribute(laMenuNode, this, TEXT_TAG);
					setMenuAttribute(laMenuNode, this, MNEMONIC_TAG);

					// Loop through all the menu items.
					NodeList laMenuItemNodeList =
						laMenuNode.getChildNodes();

					for (int i = 0;
						i < laMenuItemNodeList.getLength();
						i++)
					{
						Node laMenuItemNode =
							laMenuItemNodeList.item(i);

						if (laMenuItemNode != null
							&& laMenuItemNode.getNodeType()
								== Node.ELEMENT_NODE)
						{
							// JMenuItem
							if (laMenuItemNode.getNodeName() != null
								&& laMenuItemNode.getNodeName().equals(
									MENU_ITEM_TAG))
							{
								// Create the menu item and set it's
								// properties
								JMenuItem laMenuItem = new JMenuItem();
								setMenuAttribute(
									laMenuItemNode,
									laMenuItem,
									TEXT_TAG);
								setMenuAttribute(
									laMenuItemNode,
									laMenuItem,
									MNEMONIC_TAG);
								setMenuAttribute(
									laMenuItemNode,
									laMenuItem,
									ACCELERATOR_TAG);
								setMenuAttribute(
									laMenuItemNode,
									laMenuItem,
									ACTION_CMD_TAG);
								// Add it to the list of action 
								// listeners
								avActionComponents.add(laMenuItem);

								// Add the menu item to the menu
								this.add(laMenuItem);
							}
							// JSeparator
							else if (
								laMenuItemNode.getNodeName() != null
									&& laMenuItemNode
										.getNodeName()
										.equals(
										SEPARATOR_TAG))
							{
								JSeparator laSeparator =
									new JSeparator();
								// Add the Separator to the menu
								this.add(laSeparator);
							}
						}
					}
				}
			}
		}
		catch (Exception aeRTSEx)
		{
			// Done for logging.
			new RTSException(RTSException.JAVA_ERROR, aeRTSEx);
		}
	}
	/**
	 * Sets the JMenuItem attribute using the XML attributes for the 
	 * given node.
	 * 
	 * @param aaMenuNode Node
	 * @param aaMenuItem JMenuItem
	 * @param asAttribute String
	 */
	private void setMenuAttribute(
		Node aaMenuNode,
		JMenuItem aaMenuItem,
		String asAttribute)
	{
		if (aaMenuNode.getAttributes().getNamedItem(asAttribute)
			!= null
			&& aaMenuNode
				.getAttributes()
				.getNamedItem(asAttribute)
				.getNodeValue()
				!= null
			&& aaMenuNode
				.getAttributes()
				.getNamedItem(asAttribute)
				.getNodeValue()
				.length()
				> 0)
		{
			String lsValue =
				aaMenuNode
					.getAttributes()
					.getNamedItem(asAttribute)
					.getNodeValue();
			if (asAttribute.equals(TEXT_TAG))
			{
				aaMenuItem.setText(lsValue);
			}
			else if (asAttribute.equals(MNEMONIC_TAG))
			{
				// Must be UpperCase for KeyStroke.getKeyStroke()
				// to work.
				aaMenuItem.setMnemonic(
					KeyStroke
						.getKeyStroke(lsValue.toUpperCase())
						.getKeyCode());
			}
			else if (asAttribute.equals(ACCELERATOR_TAG))
			{
				aaMenuItem.setAccelerator(
					KeyStroke.getKeyStroke(lsValue));
			}
			else if (asAttribute.equals(ACTION_CMD_TAG))
			{
				aaMenuItem.setActionCommand(lsValue);
			}
		}
	}
}