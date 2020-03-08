package com.company.Cliente;


import com.company.Servidor.Servidor;

import javax.swing.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Cliente extends JFrame {

    private JTextField textImput;
    private JButton sendButton;
    private JTextArea text1;
    private JPanel mainPanel1;
    private JLabel nickName;
    //
    private boolean aux = false;
    private DataOutputStream dout;
    private DataInputStream din;
    private Socket clienteSocket;

    public Cliente(Socket s, String nickName) {


        //
        //this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(mainPanel1);
        this.setSize(600, 400);
        this.setVisible(true);
        this.nickName.setText(nickName);
        text1.append("Conectado a la sala de chat." + "\n");
        // SOCKET para comunicacion
        this.clienteSocket = s;

        try {
            din = new DataInputStream(clienteSocket.getInputStream());
            dout = new DataOutputStream(clienteSocket.getOutputStream());
            new ClienteReader().start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (!textImput.getText().equals("/bye")) {
                        dout.writeUTF(textImput.getText());
                    } else {
                        dout.writeUTF(textImput.getText());
                        aux = true;
                        close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.out.println("ME VOY ADIOS");
                try {
                    dout.writeUTF("/bye");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                close();
                System.exit(0);
            }
        });

    }


    public void close() {
        try {
            din.close();
            dout.close();
            clienteSocket.close();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClienteReader extends Thread {
        //LEER DEL SERRVIDOR Y ESCRIBIR EN PANTALLA
        public void run() {
            while (!aux) {
                try {
                    String msg = din.readUTF();
                    text1.append(msg + "\n");
                } catch (Exception ex) {
                    text1.append("Servidor desconectado." + "\n");
                    close();
                    System.out.println("Conexion Cerrada.");

                }

            }


        }
    }


}

