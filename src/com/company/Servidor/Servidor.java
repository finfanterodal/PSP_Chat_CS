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
    private ServerSocket serverSocket;
    static ArrayList<ServidorHandle> clientes = new ArrayList<ServidorHandle>();


    public Servidor() {
        //Configuracion panel
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(mainPanel);
        this.setSize(600, 400);
        this.setVisible(true);


        try {
            serverSocket = new ServerSocket();
            InetSocketAddress addr = new InetSocketAddress("192.168.0.4 ", 5555);
            serverSocket.bind(addr);
            System.out.println("Servidor en iniciado:" + serverSocket);
            statusText.setText("Conected");
            statusText.setForeground(Color.GREEN);

        } catch (Exception e) {
            e.printStackTrace();
        }


        while (true) {
            try {
                if (clientes.isEmpty()) {
                    text1.append("No hay clientes conectados todavía." + "\n");
                }
                Socket socket = serverSocket.accept();
                String i = new DataInputStream((socket.getInputStream())).readUTF();
                if (!(clientes.size() == 5)) {
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
                            cliente.start();
                        }
                    } else {
                        text1.append("Nuevo cliente conectado: " + i + "\n");
                        DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                        dout.writeUTF("");
                        ServidorHandle cliente = new ServidorHandle(i, socket);
                        clientes.add(cliente);
                        text1.append("Actualmente hay " + clientes.size() + "  clientes conectados." + "\n");
                        cliente.start();
                    }
                } else {
                    DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                    dout.writeUTF("Servidor lleno");
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }


    }


    public Color selectColor() {
        int index = 0;
        Color color = Color.DARK_GRAY;
        switch (index) {
            case 0:
                color = Color.GREEN;
                break;
            case 2:
                color = Color.black;
                break;
            case 3:
                color = Color.BLUE;
                break;
            case 4:
                color = Color.CYAN;
                break;
            case 5:
                color = Color.magenta;
                break;
            case 6:
                color = Color.PINK;
                break;
            case 7:
                color = Color.ORANGE;
                break;
            case 8:
                color = Color.RED;
                break;
            case 9:
                color = Color.GRAY;
                break;
        }
        return color;
    }

    //LOGICA DE CONTROL DE CADA UNO DE LOS CLIENTES QUE LLEGAN
    class ServidorHandle extends Thread {
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

        public void run() {

            try {
                sendToAllNewConnection();
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
                    if (!comingText.equals("/bye")) {
                        System.out.printf(comingText);
                        text1.append(nombre);
                        text1.append(": " + comingText + "\n");
                        //Enviamos los datos que llegan al resto datos al resto
                        sendToAll(comingText);
                    } else {
                        aux = true;
                    }
                }
                closeConnection();

            } catch (IOException e) {
                e.printStackTrace();
                closeConnection();
            }

        }

        private void closeConnection() {
            try {
                String comingText = nombre + ": se ha desconectado." + "\n";
                text1.append(comingText);
                sendToAll(comingText);
                removeCliente(nombre);
                if (clientes.isEmpty()) {
                    text1.append("No hay clientes conectados todavía." + "\n");
                }
                din.close();
                dout.close();
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        public void sendToAll(String msg) {
            for (int i = 0; i < clientes.size(); i++) {
                try {
                    DataOutputStream dou = new DataOutputStream(clientes.get(i).socket.getOutputStream());
                    dou.writeUTF(nombre + ": " + msg);
                    dou.flush();
                } catch (IOException e) {
                    removeCliente(clientes.get(i).nombre);
                }
            }
        }

        public void sendToAllNewConnection() {
            for (int i = 0; i < clientes.size(); i++) {
                try {
                    if (clientes.get(i).nombre.equals(nombre)) {
                        //
                    } else {
                        DataOutputStream dou = new DataOutputStream(clientes.get(i).socket.getOutputStream());
                        dou.writeUTF(nombre + " acaba de conectarse a la sala de chat.");
                        dou.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        new Servidor();
    }

}
