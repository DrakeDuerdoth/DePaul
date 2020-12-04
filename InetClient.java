import java.io.*; // Get the Input Output libraries
import java.net.*; // Get the Java networking libraries

public class InetClient{

 public static void main (String args[]) {
    String serverName; // String variable not assigned to anything.
    if (args.length < 1) serverName = "localhost"; //If the args array length is less than 1, then assign "localhost" to serverName
    else serverName = args[0]; // Otherwise, assign the first argument as serverName
    System.out.println("Elliott-Duerdoth Inet Client, 1.8.\n"); 
    System.out.println("Using server: " + serverName + ", Port: 1750");
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); //getting ready to assign what input is given from the client
        try {
            String name; // taking empty String 
            do {
                System.out.print
                    ("Enter a hostname or an IP address, (quit!) to end: ");
                System.out.flush (); //flushing stream by writing any output bytes to the output stream, then flushing 
                name = in.readLine (); //assigning the input to the String name
                if (name.indexOf("quit!") < 0) // if it doesn't say quit!, then get the remote address using the name and serverName
                getRemoteAddress(name, serverName);
                } 
            while (name.indexOf("quit!") < 0); /* this is an easy (and kind of lazy) way to ensure using websites that have "quit" in them. Changing the 
                                                    request to cancel to "quit!" ensures all websites are accounted for */
            System.out.println ("Cancelled by user request.");
            } 
        catch (IOException x) {x.printStackTrace ();}
    }

static String toText (byte ip[]) { // making portable to 128 bit format
    StringBuffer result = new StringBuffer ();
    for (int i = 0; i < ip.length; ++ i) {
        if (i > 0) result.append (".");
        result.append (0xff & ip[i]);
        }
    return result.toString ();
 }

 static void getRemoteAddress (String name, String serverName){
    Socket socket;
    BufferedReader fromTheServer;
    PrintStream toTheServer;
    String textFromTheServer;

        try{
        socket = new Socket(serverName, 1750); //opening connection to port 1750


    fromTheServer =
        new BufferedReader(new InputStreamReader(socket.getInputStream())); //creating I/O streams for the socket
    toTheServer = new PrintStream(socket.getOutputStream()); //sending the name of the machine or IP to the server
    toTheServer.println(name); toTheServer.flush(); // printing the name and then flushing the server

 // reading two to three lines of response from client, then waiting
    for (int i = 1; i <=3; i++){
        textFromTheServer = fromTheServer.readLine();
        if (textFromTheServer != null) System.out.println(textFromTheServer);
        }

    socket.close(); //closing the socket
    } 
     catch (IOException print) {
        System.out.println ("Socket error."); //Printing this statement if an IOException is caught.
        print.printStackTrace ();
        }
    }
}