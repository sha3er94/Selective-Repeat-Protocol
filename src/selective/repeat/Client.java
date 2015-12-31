package selective.repeat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Scanner;
import sun.security.krb5.internal.SeqNumber;

class Client extends Thread {

    public static void main(String args[]) throws Exception {
        ArrayList<String> mai2 = new ArrayList<String>();
        File mai = new File("client.in.txt");
        try {
            Scanner mai1 = new Scanner(mai);
            while (mai1.hasNextLine()) {
                mai2.add(mai1.nextLine());

            }
            System.out.println(mai2);
            mai1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int SeqNumber;
        String SeqNumbe = "0";
        DatagramSocket SendSocket = new DatagramSocket();
        DatagramSocket RecieveSocket = new DatagramSocket(Integer.parseInt(mai2.get(1)));
        InetAddress IPAddress = InetAddress.getByName(mai2.get(0));
        byte[] sendData = new byte[1024];
        byte[] sendack = new byte[1024];
        byte[] newData = new byte[1024];
        byte[] newData2 = new byte[1024];
        byte[] mayar4 = new byte[1024];
        String request = "test.txt";
        sendData = request.getBytes();
        DatagramPacket receivePacket = new DatagramPacket(newData, newData.length, IPAddress, Integer.parseInt(mai2.get(1)));
        DatagramPacket receivesize = new DatagramPacket(newData2, newData2.length, IPAddress, Integer.parseInt(mai2.get(1)));
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Integer.parseInt(mai2.get(2)));
        SendSocket.send(sendPacket);
        RecieveSocket.receive(receivesize);
        String size = new String(receivesize.getData()).trim();
        int siz = Integer.parseInt(size);
        PrintWriter x = null;
        x = new PrintWriter("new.txt", "UTF-8");
        DatagramPacket sendAck;
        Hashtable<Integer, Integer> numbers;
        numbers = new Hashtable<Integer, Integer>();
        LinkedHashMap<Integer, String> eldata = new LinkedHashMap<Integer, String>();
        int COUNTER = 0;
        for (int i = 0; i < siz; i++) {

            for (int j = 0; j < mayar4.length; j++) {
                mayar4[j] = 0;
            }
            String mayar = null;        
            RecieveSocket.receive(receivePacket);
            mayar4 = receivePacket.getData();
            mayar = new String(mayar4).trim();
            String g[] = new String[2];
            g=mayar.split("@#%");
            mayar=g[0];
            try{
            SeqNumber = Integer.parseInt(g[1].trim());
            System.out.println("Packet number  "+g[1]+" waslt");
            }
            catch (ArrayIndexOutOfBoundsException e){
               // System.out.println("ZEFT CATCH");
                break;
            }
            String f = "" + SeqNumber;
            sendack = f.getBytes();
            sendAck = new DatagramPacket(sendack, sendack.length, IPAddress, Integer.parseInt(mai2.get(2)));
            if (!eldata.containsKey(SeqNumber)) {
                COUNTER++;
                eldata.put(SeqNumber, mayar);
            }
            if (!numbers.contains(SeqNumber)) {
                SendSocket.send(sendAck);
                System.out.println("ACK " +SeqNumber+" Etb3tt");
            } else {
                numbers.remove(SeqNumber);
                System.out.println("Packet rakam " + SeqNumber + " matba3atetsh");
                i--;
            }
        }
        for (int i = 0; i < eldata.size(); i++) {
            x.print(eldata.get(i));
        }
        x.close();
        SendSocket.close();
        RecieveSocket.close();
    }
}
