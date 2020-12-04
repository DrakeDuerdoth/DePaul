import java.io.*; // Getting I/O libraries
import java.net.*; // Getting Java networking libraries

class Worker extends Thread { // Class definition
    Socket sock; // Class member, socket, local to Worker.
    Worker (Socket s) {sock = s;} // Constructor which assigns argument s to local socket(sock)
    public void run(){
        // Get I/O streams in/out from the socket:
        PrintStream output = null; //PrintStream is used to print different data types conveniently. output is set to null
        BufferedReader input = null; //BufferedReader takes in character-input, buffering characters, arrays, and lines. 
        try {
            input = new BufferedReader
                (new InputStreamReader(sock.getInputStream()));
            output = new PrintStream(sock.getOutputStream());
                try {
            String siteOrIP;
            siteOrIP = input.readLine (); //Creating an empty String then assigning it to the line read from input
            System.out.println("Looking up " + siteOrIP); // Printing statement of its process
            printRemoteAddress(siteOrIP, output);
        } 
        catch (IOException x) { //If those two trys don't work, then throw an IO Exception. 
            System.out.println("Server read error"); //When this error occurs, this statement will print. 
            x.printStackTrace ();
        }
        sock.close(); // closing the connection, but not the InetServer;
        } 
        catch (IOException ioe) {System.out.println(ioe);}
    }

    static void printRemoteAddress (String siteOrIP, PrintStream output) {
        try {
            output.println("Looking up " + siteOrIP + "..."); //Using the worker class to print what it's looking up
            InetAddress machine = InetAddress.getByName (siteOrIP);
            output.println("Host name : " + machine.getHostName ()); // To client
            output.println("Host IP : " + toText (machine.getAddress () )+ "\n"); //Returns IP address of this InetAddress object.
        } 
        catch(UnknownHostException ex) { //Throw an unknown host exception if the previous try fails. 
            output.println ("Failed in attempt to look up " + siteOrIP); //Prints this if exception is caught.
            }
    }


    static String toText (byte ip[]) { 
        StringBuffer result = new StringBuffer ();
        for (int i = 0; i < ip.length; ++ i) {
                if (i > 0) result.append (".");
                result.append (0xff & ip[i]);
            }
        return result.toString ();
        }
    }

    public class InetServer {
        public static void main(String a[]) throws IOException {
            int q_length = 6; // Number of requests for OpSys to queue //
            int portNumber = 1750; //Chose port 1750, no reasoning behind that
            Socket socket;
            ServerSocket serverSocket = new ServerSocket(portNumber, q_length); //Assigning a new socket with the two variables portNumber & q_length
            System.out.println
                ("Elliott-Duerdoth Inet server 1.8 starting up, listening at port 1750.\n");
            while (true) {
                socket = serverSocket.accept(); // Waiting for next client connection
                new Worker(socket).start(); // Makes the worker handle the socket
            }
        }
    }