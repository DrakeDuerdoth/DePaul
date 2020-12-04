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


public class JokeClient{

// GLOBAL VARIABLES_______________________
    
    static String jokeModeState = "0000";
    static String proverbModeState = "0000";
    static int clientPort = 4545;                                               //these are my global variables. I could not figure out how 
  
    static Socket socket;
    static PrintStream toTheServer;
//________________________________________


    public static void main(String args[]) {
        String serverName;                                                                          // String variable not assigned to anything.
        if (args.length < 1) serverName = "localhost";                                                               // If the args array length is less than 1, then assign "localhost" to serverName
        else serverName = args[0];                                                                   // Otherwise, assign the first argument as serverName
        System.out.println("Welcome!\nUsing Server: Joke, Port: 4545\n");
        Scanner input = new Scanner(System.in);
        System.out.println("What's your name?");
        String username = input.nextLine();
        
        System.out.println("Press enter to hear a joke or proverb. ");
        try{
        while(true){
            System.out.flush();                                                 //flushing stream by writing any output bytes to the output stream, then flushing 
            String userEntry = input.nextLine().toLowerCase();
            if(userEntry.equals("") || userEntry.equals("joke")){
                socket = new Socket(serverName, clientPort);
            BufferedReader fromTheServer =
                new BufferedReader(new InputStreamReader(socket.getInputStream())); //creating I/O streams for the socket
            toTheServer = new PrintStream(socket.getOutputStream()); //sending the name of the machine or IP to the server
            toTheServer.println(userEntry); // sending state of both proverb and userEntry to the server.
            toTheServer.println(jokeModeState);
            toTheServer.println(proverbModeState);
            toTheServer.println(userName);
            toTheServer.flush(); //flushing stream by writing any output bytes to the output stream, then flushing

            String line1 = fromTheServer.readLine(); 
            String line2 = fromTheServer.readLine();
            String line3 = fromTheServer.readLine();
            System.out.println(line1);
            jokeModeState = line2;
            proverbModeState = line3;
                
            }
        }}
        catch (IOException ioe) {System.out.println(ioe);
        }
    }}

        
        
        
        
        
        
        
       