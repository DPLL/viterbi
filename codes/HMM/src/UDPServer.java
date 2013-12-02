import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer
{
	int port = 9996;
	protected String receive() throws IOException
    {
		DatagramSocket serverSocket = new DatagramSocket(port);
        System.out.println("In the UDPserver");
        while(true)
        {
	        byte[] receiveData = new byte[256];
	        //byte[] sendData = new byte[256];
	    	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	    	serverSocket.receive(receivePacket);
	        String sentence = new String( receivePacket.getData());
	        System.out.println("RECEIVED: " + sentence);
	
	        /*
	        InetAddress IPAddress = receivePacket.getAddress();
	        int port = receivePacket.getPort();
	        String capitalizedSentence = sentence.toUpperCase();
	        sendData = capitalizedSentence.getBytes();
	        DatagramPacket sendPacket =
	        new DatagramPacket(sendData, sendData.length, IPAddress, port);
	        serverSocket.send(sendPacket);*/
	        return sentence;
        }
    }
}