import SQL.Database;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {

    public static void main(String[] args) throws Exception {

        Socket socket;
        ServerSocket serverSocket;
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;

        serverSocket = new ServerSocket(1269);
        System.out.println("Server started");
        socket = serverSocket.accept();
        System.out.println("Client accepted");
        socket.setKeepAlive(true);

        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        final String endOfMessage = "_EOM";

        String message = "";

        byte[] msgBuffer = new byte[1024];
        int messageLen = 0;

        while (socket.isConnected()) {

            int result = bufferedReader.read();

            if (result == -1) {

                break;

            } else {

                msgBuffer[messageLen++] = (byte) result;

                byte[] msgArr = Arrays.copyOfRange(msgBuffer, 0, messageLen);

                message = new String(msgArr);

                if (message.endsWith(endOfMessage)) {

                    message = message.substring(0, messageLen - 4);

                    System.out.println(message);

                    try {

                        if (message.startsWith("SEARCH")) {

                            message = message.substring(6);

                            try {

                                System.out.println(Database.searchTable(Integer.parseInt(message)));
                                bufferedWriter.write(Database.searchTable(Integer.parseInt(message)) + endOfMessage);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else if (message.startsWith("INSERT")) {

                            message = message.substring(6);

                            String[] params = message.split("\\^\\^");

                            Database.insertStudent(Integer.parseInt(params[0]), params[1], params[2], params[3], Double.parseDouble(params[4]));

                        } else if (message.startsWith("FETCH")) {

                            bufferedWriter.write(Database.fetch() + endOfMessage);

                        } else if (message.startsWith("ADMININSERT")) {

                            message = message.substring(11);

                            String[] params = message.split("\\^\\^");

                            Database.insertAdmin(Integer.parseInt(params[0]), params[1], params[2], params[3], params[4]);

                        } else if (message.startsWith("LOGIN")) {

                            message = message.substring(5);

                            String[] params = message.split("\\^\\^");

                            bufferedWriter.write(Database.loginToSql(Integer.parseInt(params[0]), params[1]) + endOfMessage);

                        }

                    } catch (IOException e) {

                        e.printStackTrace();

                    } finally {

                        message = "";
                        bufferedWriter.flush();
                        msgBuffer = new byte[1024];
                        messageLen = 0;
                        socket = serverSocket.accept();
                        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    }
                }
            }
        }
    }
}
