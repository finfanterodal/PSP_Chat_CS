package com.company.Servidor;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Servidor extends JFrame {
    //
    private JTextArea text1;
    private JTextArea text2;
    private JPanel mainPanel;
    private JLabel statusText;
    //
    ServerSocket serverSocket;
    static ArrayList<ServidorHandle> clientes = new ArrayList<ServidorHandle>();


    public Servidor() {
        //Configuracion panel
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.add(mainPanel);
        this.setSize(600, 400);
        //

        try {
            serverSocket = new ServerSocket();
            InetSocketAddress addr = new InetSocketAddress("192.168.0.15", 5555);
            serverSocket.bind(addr);
            System.out.println("Servidor en iniciado:" + serverSocket);
            statusText.setText("Conected");
            statusText.setForeground(Color.GREEN);

        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                if (clientes.size() == 0) {
                    text1.append("No hay clientes conectados todavía." + "\n");
                }
                Socket socket = serverSocket.accept();
                String i = new DataInputStream((socket.getInputStream())).readUTF();
                if (!(clientes.size() == 0)) {
                    boolean aux = false;
                    for (int j = 0; j < clientes.size(); j++) {
                        if (clientes.get(j).nombre.equals(i)) {
                            aux = true;
                            break;
                        } else {
                            aux = false;
                        }
                    }
                    if (aux) {
                        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                        dout.writeUTF("registrado");
                    } else {
                        text1.append("Nuevo cliente conectado: " + i + "\n");
                        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                        dout.writeUTF("");
                        ServidorHandle cliente = new ServidorHandle(i, socket);
                        clientes.add(cliente);
                        text1.append("Actualmente hay " + clientes.size() + "  clientes conectados." + "\n");
                    }
                } else {
                    text1.append("Nuevo cliente conectado: " + i + "\n");
                    DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                    dout.writeUTF("");
                    ServidorHandle cliente = new ServidorHandle(i, socket);
                    clientes.add(cliente);
                    text1.append("Actualmente hay " + clientes.size() + "  clientes conectados." + "\n");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }


    //LOGICA DE CONTROL DE CADA UNO DE LOS CLIENTES QUE LLEGAN
    class ServidorHandle implements Runnable {
        //
        private Socket socket;
        private String nombre;
        private DataInputStream din;
        private DataOutputStream dout;
        private boolean aux = false;

        public ServidorHandle(String nombre, Socket s) {
            this.nombre = nombre;
            this.socket = s;
        }


        //RUN DEL NUEVO CLIENTE QUE HARA SUS MOVIDAS
        @Override
        public void run() {

            try {
                din = new DataInputStream(socket.getInputStream());
                dout = new DataOutputStream(socket.getOutputStream());
                while (!aux) {
                    while (din.available() == 0) {
                        try {
                            Thread.sleep(1);//Si no entran datos en sleep
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String comingText = din.readUTF();
                    if (!comingText.equals(".bye")) {
                        text1.append(nombre + ": " + comingText + "\n");
                        //Enviamos los datos que llegan al resto datos al resto
                        sendToAll(comingText);
                    } else {
                        comingText = nombre + ": Se a desconectado." + "\n";
                        sendToAll(comingText);
                        aux = true;
                    }
                }
                closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private void closeConnection() throws IOException {
            removeCliente(nombre);
            din.close();
            dout.close();
            socket.close();
        }

        public void sendToClient(String msg) {
            try {
                dout = new DataOutputStream(socket.getOutputStream());
                dout.writeUTF(nombre + ": " + msg);
                dout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void sendToAll(String msg) {
            for (int i = 0; i < clientes.size(); i++) {
                sendToClient(msg);
            }
        }

        public void removeCliente(String nombre) {

            for (int j = 0; j < clientes.size(); j++) {
                if (clientes.get(j).nombre.equals(nombre)) {
                    clientes.remove(j);
                }
            }
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
    }
}



