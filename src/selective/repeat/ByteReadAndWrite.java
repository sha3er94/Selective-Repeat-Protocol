package selective.repeat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.zip.CRC32;
import javax.swing.*;

public class ByteReadAndWrite {

    public ArrayList<String> readAndFragment(String SourceFileName, int CHUNK_SIZE) throws IOException {
        //log ("File Is Reading "+ SourceFileName );
        File willBeRead = new File(SourceFileName);
        int FILE_SIZE = (int) willBeRead.length();
        ArrayList<String> nameList = new ArrayList<String>();
        int NUMBER_OF_CHUNKS = 0;
        byte[] temporary = null;

        try {
            InputStream inStream = null;
            int totalBytesRead = 0;

            try {
                inStream = new BufferedInputStream(new FileInputStream(willBeRead));

                while (totalBytesRead < FILE_SIZE) {
                    String PART_NAME = "data" + NUMBER_OF_CHUNKS + ".txt";
                    int bytesRemaining = FILE_SIZE - totalBytesRead;
                    if (bytesRemaining < CHUNK_SIZE) // Remaining Data Part is Smaller Than CHUNK_SIZE
                    // CHUNK_SIZE is assigned to remain volume
                    {
                        CHUNK_SIZE = bytesRemaining;
                        // System.out.println("CHUNK_SIZE: " + CHUNK_SIZE);
                    }
                    temporary = new byte[CHUNK_SIZE]; //Temporary Byte Array
                    int bytesRead = inStream.read(temporary, 0, CHUNK_SIZE);

                    if (bytesRead > 0) // If bytes read is not empty
                    {
                        totalBytesRead += bytesRead;
                        NUMBER_OF_CHUNKS++;
                    }

                    write(temporary, "D://" + PART_NAME);
                    nameList.add("D://" + PART_NAME);
                    // System.out.println("Total Bytes Read: " + totalBytesRead);
                }

            } finally {
                inStream.close();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return nameList;
    }

    void write(byte[] DataByteArray, String DestinationFileName) {
        try {
            OutputStream output = null;
            try {
                output = new BufferedOutputStream(new FileOutputStream(DestinationFileName));
                output.write(DataByteArray);
                // System.out.println("Writing Process Was Performed");
            } finally {
                output.close();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
