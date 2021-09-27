/**
 * 
 * @author Anthony
 */

package primenumberfinder;

import java.net.*;
import java.io.*;
import java.util.*;

public class PrimeNumberFinderClient {

    public static void main (String args[]) {

        //Initialize Socket variable

        Socket s = null;
        
        //Initialize Scanner object
        
        Scanner input = new Scanner(System.in);
        
        //Repeatedly send messages to the server
        
        while (true) {

            try {

                int serverPort = 7896;

                s = new Socket("localhost", serverPort); 

                DataInputStream in = new DataInputStream( s.getInputStream());

                DataOutputStream out = new DataOutputStream( s.getOutputStream());
                
                //Get input message
                
                System.out.print("Message to send: ");
                
                String message = input.nextLine();

                out.writeUTF(message); // UTF is a string encoding; see Sec 4.3

                String data = in.readUTF(); 

                System.out.println("Received: "+ data);

            } catch (UnknownHostException e){

                System.out.println("Sock:"+e.getMessage()); 

            } catch (EOFException e){

                System.out.println("EOF:"+e.getMessage());

            } catch (IOException e){

                System.out.println("IO:"+e.getMessage());

            } finally {

                if(s != null) try {

                    s.close();

                } catch (IOException e){

                    /*close failed*/

                }

            }

        }

    }

}
