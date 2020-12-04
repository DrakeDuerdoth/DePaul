/*--------------------------------------------------------

1. Drake Duerdoth  09/27/2020:

2. Java version used, if not the official version for the class:

JAVA se

3. Precise command-line compilation examples / instructions:


> javac JokeServer.java
> javac JokeClient.java
> javac JokeClientAdmin.java


4. Precise examples / instructions to run this program:

In separate shell windows:

> java JokeServer
> java JokeClient
> java JokeClientAdmin

All acceptable commands are displayed on the various consoles.

This runs across machines, in which case you have to pass the IP address of
the server to the clients. For exmaple, if the server is running at
140.192.1.22 then you would type:

> java JokeClient 140.192.1.22
> java JokeClientAdmin 140.192.1.22

5. List of files needed for running the program.

 a. checklist.html
 b. JokeServer.java
 c. JokeClient.java
 d. JokeClientAdmin.java

5. Notes:

I feel like I'm so close to having this work correctly.
I believe the issue is in the JokeClient.
I wanted to try hashmapping but did this method instead 
as a last resort.

----------------------------------------------------------*/


import java.io.*; // Get the Input Output libraries
import java.net.*; // Get the Java networking libraries
import java.util.*;


public class JokeClientAdmin{
    public static String whatUserEntry;
    public static Socket socket;
    public static PrintStream toTheServer;
    public static BufferedReader getFromServer;
    public static String reply;
    public static void main(String args[]) {
        String serverName;                                                                          // String variable not assigned to anything.
        if (args.length < 1) serverName = "localhost"; 
                                                                         // If the args array length is less than 1, then assign "localhost" to serverName
        else serverName = args[0]; 
                                                                              // Otherwise, assign the first argument as serverName
        System.out.println("Running jokeAdmin... Port 4545\n");
        System.out.println("Press s to change server mode.");
        
       try{ 
        Scanner input = new Scanner (System.in);
        
        while(true){
            whatUserEntry = input.nextLine();
            socket = new Socket(serverName, 5050); // create a socket at port 5050
            getFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            toTheServer = new PrintStream(socket.getOutputStream());

            toTheServer.println(whatUserEntry); //send what the server entered to the server
            toTheServer.flush(); // flush

            reply = getFromServer.readLine(); // reply would be the line 
            System.out.println(reply); // print the reply to the admin
        }     }catch (IOException ioe) {System.out.println(ioe);                      
    }
} }                                                 
     