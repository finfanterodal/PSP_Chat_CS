package com.company.Cliente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Cliente extends JFrame {

    private JTextField textImput;
    private JButton sendButton;
    private JTextArea text1;
    private JButton seleccionarButton;
    private JTextArea text2;
    private JPanel mainPanel1;
    //
    private boolean aux = false;
    private DataOutputStream dout;
    private DataInputStream din;
    private Socket clienteSocket;
    ;


    public Cliente(Socket s) {
        //
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(mainPanel1);
        this.setSize(400, 400);
        this.setVisible(true);
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
                    if (!textImput.getText().equals(".bye")) {
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
                    System.out.println("Conexion Cerrada.");
                    ;
                }

            }


        }
    }


}

