
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class HTTPClient {

    public static final int PORT = 80;
    public static String SERVER_ADDR = "127.0.0.1" ; // "www.google.com";
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

            Scanner scanner = new Scanner(System.in);
            String fileName;
            // Gets the request from the user

            String message = scanner.nextLine().trim();

            Scanner messageReader = new Scanner(message);

            if(messageReader.hasNext()){
                fileName = messageReader.next();
            } else {
                fileName = "index.html";
            }
            // Gets the ip address from the user
            SERVER_ADDR = messageReader.next();
            // TODO: receive the feedback
            System.out.println("After sending the request, wait for response: ");

            //get path information
            currentPath = Paths.get("");
            currentPath = currentPath.resolve("client_folder");
            currentDirectory = currentPath.toString();

            if (!SERVER_ADDR.equals("127.0.0.1")) {
                try {
                    String[] IPparts = SERVER_ADDR.split("/");
                    String fileNameOnly = SERVER_ADDR.replace(IPparts[0], "");
                    if(fileNameOnly.equals("")){
                        fileNameOnly = "index.html";
                    }

                    //Checks to see if its google or not.

                    SERVER_ADDR = "http://" + SERVER_ADDR + "/" + fileNameOnly;
                    URL url = new URL(SERVER_ADDR);
                    HttpURLConnection socket = (HttpURLConnection) url.openConnection();
                    socket.setRequestMethod("GET");
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                    numBytes = dataInputStream.readInt();
                    if (numBytes != -1) {
                        byte[] fileContents = new byte[numBytes];
                        dataInputStream.read(fileContents, 0, numBytes);

                        String pathToFile = currentPath.toAbsolutePath().resolve(fileNameOnly).toString();

                        FileOutputStream fileOutputStream = new FileOutputStream(pathToFile);

                        fileOutputStream.write(fileContents, 0, numBytes);


                        System.out.println("Saved file: " + fileNameOnly);
                    } else {
                        System.out.println("Error: HTTP/1.1 404 Not Found");
                    }
                } catch (MalformedURLException e) {
                    System.out.println("Error: HTTP/1.1 404 Not Found");
                }
            } else {
                String fileNameOnly = "";

                // The Input and Output Streams
                Socket socket = new Socket(SERVER_ADDR, PORT);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(dataOutputStream, StandardCharsets.ISO_8859_1));
                BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(dataInputStream));


                // The Request
                printWriter.print("GET " + fileName + " HTTP/1.1" + CRLF);
                printWriter.print("Host: " + SERVER_ADDR + CRLF);
                printWriter.print("Connection: close" + CRLF);
                printWriter.print("Accept: */*" + EOH);
                printWriter.flush();




                // Separates the file name from the file path provided (should there be one)
                char[] fileNameChars = new char[fileName.length()];
                for (int i = 0; i < fileName.length(); i++) {
                    fileNameChars[i] = fileName.charAt(i);
                }
                for (char i : fileNameChars) {
                    fileNameOnly = fileNameOnly + i;
                    if (i == '/') {
                        fileNameOnly = "";
                    }
                }
                // Makes the path to the file
                String pathToFile = currentPath.toAbsolutePath().resolve(fileNameOnly).toString();


                String line = bufferedReader.readLine();
                System.out.println(line);
                // Checks to see if this is a get request
                if(line != null){
                    String[] header = line.split(" ");
                    String requestType = header[1];
                    if(requestType.equals("200")){
                        downloadData(bufferedReader, line, pathToFile);
                    } else {
                        printResponse(bufferedReader, line);
                    }

                } else {
                    System.out.println("line is null");
                }


                printWriter.close();
            }

        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void downloadData(BufferedReader reader, String line, String pathToFile){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pathToFile);
            int count = 0;
            int dataComing = 0; //
            String fileContentsString = "";
            while (line != null) {
                System.out.println(line);
                //If the next line is the data, then get filecontents
                if (dataComing > 0) {
                    fileContentsString = line;
                    System.out.println(fileContentsString);
                    byte[] fileContents = fileContentsString.getBytes(StandardCharsets.ISO_8859_1);
                    System.out.println(Arrays.toString(fileContents));
                    fileOutputStream.write(fileContents, 0, fileContents.length);
                    fileOutputStream.flush();
                }
                //File Contents is the last thing before the Data is sent
                if (line.isEmpty()) {
                    System.out.println("NOW RECIEVING DATA");
                    dataComing++;
                }

                line = reader.readLine();

            }

            // Writes to the file


        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printResponse(BufferedReader reader, String line) {
        try {
            while (line != null && !line.isEmpty()) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

































//CODE THAT WORKS FOR RECEIVING FILES FROM THE SERVER CLASS ON A LOOP

/*

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
            //printWriter.print("Accept: *//*" + EOH);          GET RID OF ONE OF THE SLASHES
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

 */