package selective.repeat;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WindowChange 
{
    public WindowChange() {
    }

    public WindowChange(int windowsize) {
        this.windowsize=windowsize;
        
    }
     public ArrayList<Integer> Ack = new ArrayList<Integer>();
      public ArrayList<String> dist = new ArrayList<String>();
      volatile boolean m;
     LinkedHashMap<Integer, DatagramPacket> eldata = new LinkedHashMap<Integer, DatagramPacket>();
     int windowsize;
     int awlElWindow;
     int a5erElWindow;
     public synchronized boolean eh(int w)
     {
         if (w==getAwlElWindow())
             m=false;
         notifyAll();
         return m;
     }
     public void change(ArrayList <Integer> Ack)
     {
         for (int k = 0; k < Ack.size(); k++) {
            if ((Ack.get(k) == getAwlElWindow())) {
                System.out.println("Ack " + k + " waslt");
                awlElWindow++;
                System.out.println("7a5aly awl el window ba2a b "+awlElWindow);
            }  
            setAwlElWindow(awlElWindow);
        }
         
         setA5erElWindow(getAwlElWindow() + windowsize);
     }
    
     public Integer effect()
     {
        awlElWindow= getAwlElWindow();
         return awlElWindow;
     }
    public int getAwlElWindow() {
        return awlElWindow;
    }

    public void setAwlElWindow(int awlElWindow) {
        this.awlElWindow = awlElWindow;
    }

    public int getA5erElWindow() {
        return a5erElWindow;
    }

    public void setA5erElWindow(int a5erElWindow) {
        this.a5erElWindow = a5erElWindow;
    }
}
