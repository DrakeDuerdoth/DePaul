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



import java.io.*;                                                                           // Getting I/O libraries
import java.net.*;                                                                          // Getting Java networking libraries
import java.util.*;                                                                         // Getting java utilities


class AdminWorker extends Thread{
    Socket adminSock = new Socket();
    AdminWorker (Socket adminSock) {this.adminSock = adminSock;} //this is the constructor for the adminworker socket
    
    
    
    public void run(){                                                                         // Get I/O streams in/out from the socket:
        PrintStream toAdminWorker = null;                                                      //PrintStream is used to print different data types conveniently. output is set to null
        Scanner fromAdminWorker = null;                                                        //BufferedReader takes in character-input, buffering characters, arrays, and lines. 
        try {                                                                                  //I wanted to use a scanner to able to use nextLine. I'm more familiar with it 
            fromAdminWorker = new Scanner
                (new InputStreamReader(adminSock.getInputStream()));
            toAdminWorker = new PrintStream(adminSock.getOutputStream());
            String userInput = fromAdminWorker.nextLine().toLowerCase();                              //Creating an empty String then assigning it to the line read from input

        switch(userInput){
            
            case "s": //changing server
                JokeServer.maintenanceMode = false;                                                         // If you type in an s, it will change the mode of the server
                if (JokeServer.proverbMode == true) {
                    JokeServer.proverbMode = false;
                    JokeServer.jokeMode = true;
                    toAdminWorker.println("Now in joke mode...");
                    break;
                } 
                else{
                    JokeServer.maintenanceMode = false;                                                      //this would be the switch from joke to proverb
                    JokeServer.proverbMode = true;
                    JokeServer.jokeMode = false;
                    toAdminWorker.println("Now in proverb mode...")
                    break;
                }
            default:
                toAdminWorker.println("Must type s or press enter...");
                break;
            }
            adminSock.close();}
            catch (IOException x) {                                                             
            System.out.println("Server read error");                                     
            x.printStackTrace ();
            }
        }
    }
      


class clientWorker extends Thread {                                                               // Class definition
    Socket clientSocket;  // socket used for the client
    String jokeModeState;   //states for both modes. this would be when it fills up to "1111", then it would reset, and a random number would then be generated
    String proverbModeState;                                                                        
    clientWorker(Socket clientSocket) {this.clientSocket = clientSocket;}  
    
                                                                                            // Constructor which assigns argument s to local socket(sock)
    public void run(){                                                                      // Get I/O streams in/out from the socket:
        PrintStream toClient = null;                                                          //PrintStream is used to print different data types conveniently. output is set to null
        
        Scanner fromClient;                                                       
        try {
            fromClient = new Scanner
                (new InputStreamReader(clientSocket.getInputStream()));
            toClient = new PrintStream(clientSocket.getOutputStream());
      
        try {
            String line = fromClient.nextLine();
            jokeModeState = fromClient.nextLine();                                  
            if(jokeModeState.equals("1111")) jokeModeState = "0000";  //this is resetting the state of joke and proverb modes
            
            proverbModeState = fromClient.nextLine();
            if(proverbModeState.equals("1111")) proverbModeState = "0000";
    

        String name = fromClient.nextLine();
            if(line.equals("")){
                if(JokeServer.jokeMode == true && JokeServer.proverbMode == false && JokeServer.maintenanceMode == false){ // if the client entered enter and all these conditions are good, then it would go get the joke. 
                System.out.println("Looking up" + name);
                System.out.flush();
                toClient.println(printJoke(name));
                toClient.println(jokeModeState);
                toClient.println(proverbModeState);
            }
            else if(JokeServer.jokeMode == false && JokeServer.proverbMode == true && JokeServer.maintenanceMode == false){
                System.out.println("Looking up " + name);                                       // Printing statement of its process
                System.out.flush();
                toClient.println(printProverb(name));    // if the client entered enter and all these conditions are good, then it would go get the proverb. 
                toClient.println(jokeModeState);
                toClient.println(proverbModeState);
            }

            else if(JokeServer.maintenanceMode == true && JokeServer.jokeMode == false && JokeServer.proverbMode == false){
                System.out.println("In maintenance mode...");
                toClient.println("In maintenance mode...");
                toClient.println(jokeModeState);
                toClient.println(proverbModeState);
            }
        }
    }catch (RuntimeException x) {                                                             //If those two trys don't work, then throw an IO Exception. 
            System.out.println("Server read error");                                        //When this error occurs, this statement will print. 
            x.printStackTrace ();
            }
            
        
        
        clientSocket.close();                                                                       // closing the socket connection, but not the server
        }catch (IOException ioe) {System.out.println(ioe);
    }
    
}


    

    public int randomNum(int min,int max){ 
        Random rand = new Random();
        int ranNum = rand.nextInt((max - min) +1) + min;
        return ranNum;
    }

    public String printJoke (String name) {
        String returnedJoke = "";

        while(true){

            int randomNumber = randomNum(0,3);
            switch(randomNumber){
                case 0:
                    if(jokeModeState.charAt(0) == '0'){
                        returnedJoke = "JA " + name + ": What do you call birds that stick together? Vel-crows!";
                        jokeModeState = "1" + jokeModeState.substring(1);
                        return returnedJoke;
                    }
                case 1:
                    if(jokeModeState.charAt(1) == '0'){
                        returnedJoke = "JB " + name + ": What do you call a dinosaur with an extensive vocabulary? A thesaurus!";
                        jokeModeState = jokeModeState + jokeModeState.substring(0,1) + "1" + jokeModeState.substring(2);
                        return returnedJoke;
                    }
                case 2:
                    if(jokeModeState.charAt(2) == '0'){
                        returnedJoke = "JC " + name + ": What do you call two birds in love? Tweethearts!";
                        jokeModeState = jokeModeState + jokeModeState.substring(0,2) + "1" + jokeModeState.substring(3);
                        return returnedJoke;
                    }
                case 3:
                    if(jokeModeState.charAt(3) == '0'){
                        returnedJoke = "JD " + name + ": What do you call a farm that makes bad jokes? Corny!";
                        jokeModeState = jokeModeState + jokeModeState.substring(0,3) + "1" + jokeModeState.substring(3);
                        return returnedJoke;
                    }
                }

        }
    }

    public String printProverb (String name) {
        String returnedProverb = ""; // blank string, but if certain conditions are met, then this will be filled with the correct proverb

        while(true){ // infinite loop

            int randomNumber = randomNum(0,3); // creating random number 
            switch(randomNumber){
                case 0:
                    if(proverbModeState.charAt(0) == '0'){
                        returnedProverb = "PA " + name + ": Every rose has its thorn.";
                        proverbModeState = "1" + proverbModeState.substring(1);
                        return returnedProverb;
                    }
                case 1:
                    if(proverbModeState.charAt(1) == '0'){
                        returnedProverb = "PB " + name + ": I am open and ready to be positive";
                        proverbModeState = proverbModeState + proverbModeState.substring(0,1) + "1" + proverbModeState.substring(2);
                        return returnedProverb;
                    }
                case 2:
                    if(proverbModeState.charAt(2) == '0'){
                        returnedProverb = "PC " + name + ": A wise person listens and take in more instruction";
                        proverbModeState = proverbModeState + proverbModeState.substring(0,2) + "1" + proverbModeState.substring(3);
                        return returnedProverb;
                    }
                case 3:
                    if(proverbModeState.charAt(3) == '0'){
                        proverbModeState = "PD" + name + ": Half a loaf is better than none.";
                        proverbModeState = proverbModeState + proverbModeState.substring(0,3) + "1" + proverbModeState.substring(3);
                        return returnedProverb;
                    }
                }

        }
    }
}

    public class JokeServer {

       static boolean maintenanceMode = false;                                          //These were my "switches" to toggles through each mode
       static boolean proverbMode = false;
       static boolean jokeMode = true;
        public static void main(String a[]) throws IOException {                        //Main function                                                          
            int q_len = 6;
            int port = 4545;                                                            //Port Number for JokeServer
            Socket sock;
               
        
            AdminLooper AL = new AdminLooper();                                             // create a DIFFERENT thread
            Thread t = new Thread(AL);
            t.start();                                                                      // ...and start it, waiting for administration input
            
            ServerSocket servsock = new ServerSocket(port, q_len);
                
            System.out.println("Joke server starting up at port 4545.\n");
            while (true) {
              sock = servsock.accept();                                                     // wait for the next client connection:
              new Worker (sock).start();
            }
        }
    }
        class AdminLooper implements Runnable {      
            public void run(){                                                              // RUNning the Admin listen loop
            System.out.println("In the admin looper thread");
              
              int q_len = 6;                                                                // Number of requests for OpSys to queue 
              int port = 5050;                                                              // We are listening at a different port for Admin clients
              Socket sock;
          
              try{
                ServerSocket adminServSock = new ServerSocket(port, q_len);
                while (true) {
                    sock = adminServSock.accept();                                                         // wait for the next ADMIN client connection:
                    new AdminWorker (sock).start(); 
                }
              }catch (IOException ioe) {System.out.println(ioe);}
            }
        }
    