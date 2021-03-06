/**
 * 
 */
package com.burritopos.presentation;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
//import java.util.UUID;
import org.apache.log4j.*;
import java.util.Random;
import org.springframework.context.support.*;

import com.burritopos.business.InventoryManager;
import com.burritopos.domain.Inventory;
import com.burritopos.exception.ServiceLoadException;

/**
 * @author james.bloom
 *
 */
public class MainUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2404746632782793567L;
    private static Logger dLog = Logger.getLogger(MainUI.class);
	private JDesktopPane theDesktop = new JDesktopPane();
	private JMenuBar menubar = new JMenuBar();
	private Inventory i = new Inventory(new Integer("1"),50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50);
	private InventoryManager iManager;
    
	// Spring configuration
    private static final String SPRING_CONFIG_DEFAULT = "applicationContext.xml";
	
	public MainUI() {
		super("Neato Burrito");

        dLog.trace("In MainUI: " + MainUI.class.getCanonicalName());
		try {
            Random rand = new Random();
            i.setId(rand.nextInt());
            
            //Spring Framework IoC
            ClassPathXmlApplicationContext beanfactory = null;
            try {
                beanfactory = new ClassPathXmlApplicationContext(SPRING_CONFIG_DEFAULT);
                iManager = (InventoryManager)beanfactory.getBean("inventoryManager");

            } catch (Exception e) {
                dLog.error("Error setting Spring bean", e);
            } finally {
                if (beanfactory != null) {
                    beanfactory.close();
                }
            }
			
            //Factory Pattern
            //iManager = new InventoryManager();
			iManager.createInventory(i);
		}
		catch(Exception e) {
			dLog.error("Unable to create Inventory", e);
		}
		
		this.setBounds(0, 0, 500, 500);
		Container container = getContentPane();
		container.add(theDesktop);
		setJMenuBar(menubar);
		JMenu fileMenu = new JMenu("File");
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('X');
		exitItem.addActionListener ( new ActionListener () {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			}
		);
		fileMenu.add(exitItem);
		menubar.add(fileMenu);
		
		JMenu actionMenu = new JMenu("Action");
		JMenuItem createAccountItem = new JMenuItem("Create Order");
		createAccountItem.setMnemonic('A');
		createAccountItem.addActionListener ( new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				try {
					// get updated inventory
					i = iManager.getInventory(i.getId());
					
					OrderUI caUI = new OrderUI("New Order", i);
					caUI.setVisible(true);
					theDesktop.add(caUI); 	
				}
				catch(ServiceLoadException e1) {
					dLog.error("Exception in Create Order", e1);
				}
				catch(Exception e2) {
					dLog.error("Exception in calculateTotal", e2);
				}
			}
		});
		actionMenu.add(createAccountItem);
		menubar.add(actionMenu);

		JMenuItem viewInventoryItem = new JMenuItem("View Inventory");
		viewInventoryItem.setMnemonic('I');
		viewInventoryItem.addActionListener ( new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				try {
					// get updated inventory
					i = iManager.getInventory(i.getId());
					
					InventoryUI iUI = new InventoryUI("View Inventory",i);
					iUI.setVisible(true);
					theDesktop.add(iUI); 	
				}
				catch(ServiceLoadException e1) {
					dLog.error("Exception in View Inventory", e1);
				}
				catch(Exception e2) {
					dLog.error("Exception in View Inventory", e2);
				}
			}
		});
		actionMenu.add(viewInventoryItem);
		menubar.add(actionMenu);
		
		JMenuItem viewOrderHistoryItem = new JMenuItem("View Order History");
		viewOrderHistoryItem.setMnemonic('H');
		viewOrderHistoryItem.addActionListener ( new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				try {
					OrderViewUI ovUI = new OrderViewUI("Order History");
					ovUI.setVisible(true);
					theDesktop.add(ovUI); 	
				}
				catch(ServiceLoadException e1) {
					dLog.error("Exception in View Order History", e1);
				}
				catch(Exception e2) {
					dLog.error("Exception in View Order History", e2);
				}
			}
		});
		actionMenu.add(viewOrderHistoryItem);
		menubar.add(actionMenu);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

}
