package gui.swing.test;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class Test {
    
	   private JFrame mainFrame;
	   private JLabel headerLabel;
	   private JLabel statusLabel;
	   private JPanel controlPanel;

	   public Test(){
	      prepareGUI();
	   }

	   public static void main(String[] args){
	      Test  test = new Test();      
	      test.showListDemo();
	   }

	   private void prepareGUI(){
	      mainFrame = new JFrame("Emergency Transcriber System");
	      mainFrame.setSize(400,400);
	      mainFrame.setLayout(new GridLayout(3, 1));
	      mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });    
	      headerLabel = new JLabel("", JLabel.CENTER);        
	      statusLabel = new JLabel("",JLabel.CENTER);    

	      statusLabel.setSize(350,100);

	      controlPanel = new JPanel();
	      controlPanel.setLayout(new FlowLayout());

	      mainFrame.add(headerLabel);
	      mainFrame.add(controlPanel);
	      mainFrame.add(statusLabel);
	      mainFrame.setVisible(true);  
	   }

	   private void showListDemo(){                                       

	      headerLabel.setText("Emergency Transcriber System"); 

	      final DefaultListModel keywords = new DefaultListModel();

	      keywords.addElement("amiodarone");
	      keywords.addElement("asystole");
	      keywords.addElement("attach");
	      keywords.addElement("capnography");
	      keywords.addElement("compression");
	      keywords.addElement("clear");
	      keywords.addElement("CPR");
	      keywords.addElement("compression");
	      keywords.addElement("defibrillator");
	      keywords.addElement("epinephrine");
	      keywords.addElement("IO");
	      keywords.addElement("IV");
	      keywords.addElement("oxgen");
	      keywords.addElement("PEA");
	      keywords.addElement("pressure");
	      keywords.addElement("resuscitation");
	      keywords.addElement("rhythm");
	      keywords.addElement("shock");
	      keywords.addElement("Vfib");
	      keywords.addElement("Vtach");

	      final JList keywordList = new JList(keywords);
	      keywordList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	      keywordList.setSelectedIndex(0);
	      keywordList.setVisibleRowCount(5);        

	      JScrollPane keywordListScrollPane = new JScrollPane(keywordList);    

/*	      final DefaultListModel vegName = new DefaultListModel();

	      vegName.addElement("Lady Finger");
	      vegName.addElement("Onion");
	      vegName.addElement("Potato");
	      vegName.addElement("Tomato");

	      final JList vegList = new JList(vegName);
	      vegList.setSelectionMode(
	         ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	      vegList.setSelectedIndex(0);
	      vegList.setVisibleRowCount(3);        

	      JScrollPane vegListScrollPane = new JScrollPane(vegList);       */

	      JButton showButton = new JButton("Confirm");

	      showButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { 
	            String data = "";
	            if (keywordList.getSelectedIndex() != -1) {                     
	               data = "Fruits Selected: " + keywordList.getSelectedValue(); 
	               statusLabel.setText(data);
	            }
/*	            if(vegList.getSelectedIndex() != -1){
	               data += " Vegetables selected: ";
	               for(Object vegetable:vegList.getSelectedValues()){
	                  data += vegetable + " ";
	               }
	            }*/
	            statusLabel.setText(data);
	         }
	      }); 

	      controlPanel.add(keywordListScrollPane);    
	      //controlPanel.add(vegListScrollPane);    
	      controlPanel.add(showButton);    
		  
	      mainFrame.setVisible(true);             
	   }
	}