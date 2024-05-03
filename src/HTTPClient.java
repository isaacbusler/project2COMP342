
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

public class HTTPClient {

    public static final int PORT = 80;
    public static final String SERVER_ADDR = "127.0.0.1" ; // "www.google.com";
    public static final String CRLF = "\r\n";
    public static final String EOH = CRLF + CRLF;

    public static final int CHUNK_SIZE = 512;				// size of fragment to process



    //GLOBAL VARIABLES ADDED FROM PROJECT 1
    private static String currentDirectory; //String of the current path
    private static Path currentPath; //the path of the client

    private static int numBytes; //number of bytes in the message being sent or received


    public static void main(String[] args) {

        System.out.println("client is requesting ... ");
        try {
            Socket socket = new Socket(SERVER_ADDR, PORT);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            // generate a HTTP request a print writer, handy to handle output stream
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(dataOutputStream, StandardCharsets.ISO_8859_1));
            printWriter.print("GET / HTTP/1.1" + CRLF);
            printWriter.print("Host: " + SERVER_ADDR + CRLF);
            printWriter.print("Connection: close" + CRLF);
            printWriter.print("Accept: */*" + EOH);
            printWriter.flush();

            // TODO: receive the feedback

            //Following code is some stuff from project 1 with slight edits
            Scanner scanner = new Scanner(System.in);
            while(true) {
                String fileName = "";
                // Gets the ip address request from the user
                String ipAddress = scanner.next();
                // Sends the ip address to the Server
                dataOutputStream.writeUTF(ipAddress);

                // Gets the filename if there is one
                if (scanner.hasNext()) {
                    fileName = scanner.next();
                }

                if (!fileName.equals(""))   {
                    // Get the index.html file
                }
                else {
                    // Get the specified file
                }

                // Probably will want to use this (and can probably get rid of the if/else
                numBytes = dataInputStream.readInt();
                if(numBytes != -1) {
                    byte[] fileContents = new byte[numBytes];
                    dataInputStream.read(fileContents, 0, numBytes);

                    String pathToFile = currentPath.toAbsolutePath().resolve(fileName).toString();

                    FileOutputStream fileOutputStream = new FileOutputStream(pathToFile);

                    fileOutputStream.write(fileContents, 0, numBytes);
                    printWriter.print(dataInputStream.readUTF());
                } else {
                    printWriter.print(fileName + " does not exist.");
                }




                System.out.println("After sending the request, wait for response: ");
                printWriter.close();

            }

        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
