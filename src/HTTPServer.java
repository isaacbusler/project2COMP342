
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//djlfjslk

public class HTTPServer {

    public static final int PORT = 80;
    public static final String IP = "127.0.0.1";
    public static final String CRLF = "\r\n";
    public static final String EOH = CRLF + CRLF;

    //ADDED FROM PROJECT 1
    // This will hold the path to the project
    private static Path currentPath;
    // This will hold the path to the server_Folder
    private static Path pathToFolder;
    // the string version of currentPath
    private static String currentPathString;
    // The string version of pathToFolder
    private static String currentFolderString;
    // Holds the number of bytes send by the server or recived by the server
    private static int numBytes;

    public static void main(String[] args){

        System.out.println("server is listening to port 80");
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            while(true){

                // Handles finding the correct_path
                currentPath = Paths.get("");
                pathToFolder = currentPath.resolve("server_folder");
                currentPathString = currentPath.toAbsolutePath().toString();
                currentFolderString = pathToFolder.toString();

                Socket socket = serverSocket.accept();
                System.out.println("get connection from IP: " + socket.getRemoteSocketAddress());

                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                // good to handle strings from stream
                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(dataInputStream));
                String line = bufferedReader.readLine();
                while(line != null && !line.isEmpty()){
                    System.out.println(line);
                    line  = bufferedReader.readLine();
                }

                //gets the file name from the input stream
                String fileName = bufferedReader.readLine();
                //contents from the file stored
                byte[] fileContents = get(fileName);

                // If numbytes is not -1 then the file has contents
                if (numBytes != -1) {

                    // Write the contents of the file to the new file
                    dataOutputStream.writeInt(numBytes);
                    dataOutputStream.write(fileContents, 0, numBytes);

                    // Otherwise return -1 to tell the client that the file doesn't exist
                } else {
                    dataOutputStream.writeInt(-1);
                }


                bufferedReader.close();
                // TODO: send back a response
                // generate a HTTP request a print writer, handy to handle output stream


            }

        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    //COPIED FROM PROJECT 1
    public static byte[] get(String fileName){
        String pathToFile;
        // Creates the path to the file
        pathToFile = pathToFolder.toAbsolutePath().resolve(fileName).toString();

        // teys to open the file
        try {

            // Reads the data from the file
            File file = new File(pathToFile);
            FileInputStream fileReader = new FileInputStream(pathToFile);


            numBytes = (int) file.length();

            // Stores teh bytes into the byte array
            byte[] fileData = fileReader.readAllBytes();

            return fileData;

            // If the file couldn't be opened, set numBytes to -1
        } catch (IOException e){
            numBytes = -1;
            return null;
        }

    }


}























//CODE THAT WORKS WITH A LOOP FOR SENDING FILES TO THE CLIENT

/*

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HTTPServer {

    public static final int PORT = 80;
    public static final String IP = "127.0.0.1";
    public static final String CRLF = "\r\n";
    public static final String EOH = CRLF + CRLF;


    //ADDED FROM PROJECT 1
    // This will hold the path to the project
    private static Path currentPath;
    // This will hold the path to the server_Folder
    private static Path pathToFolder;
    // the string version of currentPath
    private static String currentPathString;
    // The string version of pathToFolder
    private static String currentFolderString;
    // Holds the number of bytes send by the server or recived by the server
    private static int numBytes;

    public static void main(String[] args){

        System.out.println("server is listening to port 80");
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            // Handles finding the correct_path
            currentPath = Paths.get("");
            pathToFolder = currentPath.resolve("server_folder");
            currentPathString = currentPath.toAbsolutePath().toString();
            currentFolderString = pathToFolder.toString();
            Socket socket = serverSocket.accept();
            System.out.println("get connection from IP: " + socket.getRemoteSocketAddress());

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            // good to handle strings from stream
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(dataInputStream));
            //String line = bufferedReader.readLine();
            //while(line != null && !line.isEmpty()){
            //    System.out.println(line);
            //    line  = bufferedReader.readLine();
            //bufferedReader.close();
            //}

            while(true){

                // TODO: send back a response
                // generate a HTTP request a print writer, handy to handle output stream

                //gets the ip address from the input stream
                String ipAddress = bufferedReader.readLine(); //WILL BE NEEDED FOR PARTS 2 AND 3
                //gets the file name from the input stream
                String fileName = bufferedReader.readLine();
                System.out.println(fileName);
                //contents from the file stored
                byte[] fileContents = get(fileName);

                // If numbytes is not -1 then the file has contents
                if (numBytes != -1) {

                    // Write the contents of the file to the new file
                    dataOutputStream.writeInt(numBytes);
                    dataOutputStream.write(fileContents, 0, numBytes);

                    // Otherwise return -1 to tell the client that the file doesn't exist
                } else {
                    dataOutputStream.writeInt(-1);
                }
                String randomStringThatHasToBeHereToMakeItWork = bufferedReader.readLine().trim();
            }

        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }




    }

    //COPIED FROM PROJECT 1
    public static byte[] get(String fileName){
        String pathToFile;
        // Creates the path to the file
        pathToFile = pathToFolder.toAbsolutePath().resolve(fileName).toString();

        // teys to open the file
        try {

            // Reads the data from the file
            File file = new File(pathToFile);
            FileInputStream fileReader = new FileInputStream(pathToFile);


            numBytes = (int) file.length();

            // Stores teh bytes into the byte array
            byte[] fileData = fileReader.readAllBytes();

            return fileData;

            // If the file couldn't be opened, set numBytes to -1
        } catch (IOException e){
            numBytes = -1;
            return null;
        }

    }


}
 */
