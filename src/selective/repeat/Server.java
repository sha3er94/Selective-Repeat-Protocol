package selective.repeat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {
   
    private String names;
    public ArrayList<String> dist;
    DatagramSocket Socket;
    DatagramSocket SendSocket;
    InetAddress IPAddress;
    public ArrayList<Integer> Ack = new ArrayList<Integer>();
    int seqnumber;
        int awlElWindow;
        int a5erElWindow;
    LinkedHashMap<Integer, DatagramPacket> eldata = new LinkedHashMap<Integer, DatagramPacket>();
    LinkedHashMap<Integer, DatagramPacket> eldata2 = new LinkedHashMap<Integer, DatagramPacket>();
    Hashtable<Integer, Integer> acks = new Hashtable<Integer, Integer>();
    Hashtable<Integer, Integer> numbers;
    public Server(String name, ArrayList dista, DatagramSocket re, InetAddress IPAddress,
         Hashtable<Integer, Integer> numbers) {
        this.names = name;
        this.dist = dista;
        this.SendSocket = re;
        this.IPAddress = IPAddress;
        this.numbers=numbers;
    }
  public Server(String name, ArrayList dista, DatagramSocket re, InetAddress IPAddress,
         Hashtable<Integer, Integer> numbers, int awlElWindow, int a5erElWindow) {
        this.names = name;
        this.dist = dista;
        this.SendSocket = re;
        this.IPAddress = IPAddress;
        this.numbers=numbers;
        this.awlElWindow=awlElWindow;
        this.a5erElWindow=a5erElWindow;
    }

    public synchronized void Recieve(DatagramSocket clientSocket, DatagramPacket receivePacket) throws InterruptedException, IOException {
        clientSocket.receive(receivePacket);
        notifyAll();
    }

    public synchronized void Send(DatagramSocket bb3tSocket, DatagramPacket sendPacket) throws InterruptedException, IOException {
        bb3tSocket.send(sendPacket);
        notifyAll();
    }

    @Override
    public void run() {
        // try {
        for (int i = 0; i < dist.size(); i++) {
            acks.put(i, 9999);
        }
        byte[] receiveAck = new byte[1024];
        int windowsize;
        windowsize = (dist.size() / 2) + 1;
        int j = 0;
        WindowChange w = new WindowChange(windowsize);
        if (names.equals("send")) {
//            awlElWindow = w.effect();
//            a5erElWindow = awlElWindow + windowsize;
            if (dist.size() < a5erElWindow) {
                a5erElWindow = dist.size();
            }
            for (int i = awlElWindow; i < a5erElWindow; i++) {
                try {
                    String m = dist.get(i);
                    seqnumber = i;
                    String I = "" + "@#%" + i;
                    RandomAccessFile f = new RandomAccessFile(m, "r");
                    byte[] b = new byte[(int) f.length() + 1];
                    byte[] e = I.getBytes();
                    byte[] destination = new byte[b.length + e.length];
                    System.arraycopy(b, 0, destination, 0, b.length);
                    System.arraycopy(e, 0, destination, b.length, e.length);
                    f.read(destination);
                    DatagramPacket sendPacket = new DatagramPacket(destination, destination.length, IPAddress, 9875);
                    System.out.println("bb3t packet " + i);
                    if (!numbers.contains(i)) {
                        Send(SendSocket, sendPacket);}
                    else
                    {
                        numbers.remove(i);
                        System.out.println("Packet rakam " + i + " matba3atetsh");
                    }
                    awlElWindow = w.effect();
                } catch (InterruptedException | IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        } else {
            while (true) {
                try {
                    SendSocket.setSoTimeout(5000);
                    DatagramPacket receiveAc = new DatagramPacket(receiveAck, receiveAck.length, IPAddress, 9876);
                    Recieve(SendSocket, receiveAc);
                    int t=windowsize;
                    String e = new String(receiveAc.getData()).trim();
                    int r;
                    try {
                        r = Integer.parseInt(e);
                        acks.replace(r, r);
                    } catch (Exception z) {
                        r = 0;
                    }
                    Ack.add(r);
                    Collections.sort(Ack);
                    int a=awlElWindow;
                    int g=a5erElWindow;
                    w.change(Ack);
                    awlElWindow = w.effect();
                    a5erElWindow=awlElWindow+windowsize;
                    if (a!=awlElWindow && Ack.size()==t)
                    {
                       (new Thread(new Server("send", dist, SendSocket, IPAddress,numbers,awlElWindow,a5erElWindow))).start();
                       t=t+windowsize;
                    }
                    if (Ack.size() == dist.size()) {
                        break;
                    }
                } catch (SocketTimeoutException p) {
                   
                    try {
                        int h = 0;
                        for (int k = 0; k < acks.size(); k++) {
                            if (acks.get(k) == 9999) {
                                h = k;
                                System.out.println(h);
                                acks.put(h, h);
                                break;
                            }
                        }
                        System.out.println("Timed Out "+h);
                        System.out.println("Ack bta3t packet " + h + " matba3atetsh");
                        String m = dist.get(h);
                        String I = "" + "@#%" + h;
                        RandomAccessFile f = new RandomAccessFile(m, "r");
                        byte[] b = new byte[(int) f.length() + 1];
                        byte[] e = I.getBytes();
                        byte[] destination = new byte[b.length + e.length];
                        System.arraycopy(b, 0, destination, 0, b.length);
                        System.arraycopy(e, 0, destination, b.length, e.length);
                        f.read(destination);
                        System.out.println("Bb3t el packet el wa23et number "+h);
                        DatagramPacket sendPacket = new DatagramPacket(destination, destination.length, IPAddress, 9875);
                        Send(SendSocket, sendPacket);
                        awlElWindow = w.effect();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (SocketException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        awlElWindow = w.effect();
        a5erElWindow = awlElWindow + windowsize;
        j = a5erElWindow;
    }

    public static void main(String args[]) throws Exception {
        ArrayList<String> mai3 = new ArrayList<String>();
        File mai4 = new File("server.in.txt");
        try {
            Scanner mai5 = new Scanner(mai4);
            while (mai5.hasNextLine()) {
                mai3.add(mai5.nextLine());
            }
            System.out.println(mai3);
            mai5.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] receiveData = new byte[1024];
        DatagramSocket RecieveSocket = new DatagramSocket(Integer.parseInt(mai3.get(0)));
        DatagramSocket SendSocket = new DatagramSocket();
        ArrayList<String> dist = new ArrayList<>();
        ByteReadAndWrite read = new ByteReadAndWrite();
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
        dist = read.readAndFragment(mai2.get(3), 512);
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        try {
            RecieveSocket.receive(receivePacket);
        } catch (Exception e) {
            System.out.println("7aga bazet");
            // break; 
        }
        InetAddress IPAddress = receivePacket.getAddress();
        int mt = dist.size();
        String g = "" + mt;
       int windowsize = (dist.size() / 2) + 1;
        byte[] result = g.getBytes();
        DatagramPacket sendsize = new DatagramPacket(result, result.length, IPAddress, Integer.parseInt(mai3.get(1)));
        SendSocket.send(sendsize);
      //  long s = System.currentTimeMillis();
           Random rand = new Random(); 
         int z=rand.nextInt(10);
         System.out.println(z);
         double plp =0.3;
        // double plp = Double.parseDouble(mai3.get(3));
         System.out.println(plp);
        int loss=(int) (dist.size()*plp);
        System.out.println("loss "+loss);
         Hashtable<Integer, Integer> numbers;
         numbers = new Hashtable<Integer, Integer>();
         int RandomNumber1;
         for (int i = 0; i < loss; i++)
         {
         while(!numbers.contains(RandomNumber1=rand.nextInt(10)+10))
         {
         numbers.put(RandomNumber1, RandomNumber1);
         break;
         }
         }
        (new Thread(new Server("receive", dist, RecieveSocket, IPAddress,numbers))).start();
        (new Thread(new Server("send", dist, SendSocket, IPAddress,numbers,0,windowsize))).start();
    }

}
