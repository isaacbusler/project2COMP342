
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            //printWriter.print("GET / HTTP/1.1" + CRLF);
            //printWriter.print("Host: " + SERVER_ADDR + CRLF);
            //printWriter.print("Connection: close" + CRLF);
            //printWriter.print("Accept: */*" + EOH);
            //printWriter.flush();

            // TODO: receive the feedback

            //get path information
            currentPath = Paths.get("");
            currentPath = currentPath.resolve("client_folder");
            currentDirectory = currentPath.toString();

            Scanner scanner = new Scanner(System.in);
            while(true) {

                String fileName;
                // Gets the request from the user
                String message = scanner.nextLine().trim();
                Scanner messageReader = new Scanner(message);
                // Gets the ip address from the user
                String ipAddress = messageReader.next();
                // Sends the ip address to the Server
                printWriter.print(ipAddress + CRLF);

                // if there is no file name specified it is set to "index.html"
                if (messageReader.hasNext()) {
                    fileName = messageReader.next();
                }
                else {
                    fileName = "index.html";
                }
                // Sends the file name to the Server
                printWriter.print(fileName + EOH);
                printWriter.flush();

                // Separates the file name from the file path provided (should there be one)
                String fileNameOnly = "";
                char[] fileNameChars = new char[fileName.length()];
                for (int i = 0; i < fileName.length(); i++) {
                    fileNameChars[i] = fileName.charAt(i);
                }
                for (char i : fileNameChars)    {
                    fileNameOnly = fileNameOnly + i;
                    if (i == '/')   {
                        fileNameOnly = "";
                    }
                }

                numBytes = dataInputStream.readInt();
                if(numBytes != -1) {
                    byte[] fileContents = new byte[numBytes];
                    dataInputStream.read(fileContents, 0, numBytes);

                    String pathToFile = currentPath.toAbsolutePath().resolve(fileNameOnly).toString();

                    FileOutputStream fileOutputStream = new FileOutputStream(pathToFile);

                    fileOutputStream.write(fileContents, 0, numBytes);


                    System.out.println("Saved file: " + fileNameOnly);
                }
                else {
                    System.out.println("Error: HTTP/1.1 404 Not Found");
                }

                //System.out.println("After sending the request, wait for response: ");
                //printWriter.close();

            }

        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
