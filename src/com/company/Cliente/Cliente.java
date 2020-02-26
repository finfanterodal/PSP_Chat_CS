package com.company.Cliente;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente extends JFrame implements Runnable {

    private JTextField textImput;
    private JButton sendButton;
    private JTextArea text1;
    private JButton seleccionarButton;
    private JTextArea text2;
    private JPanel mainPanel;
    //
    private boolean aux = false;
    private Socket s;
    private DataOutputStream dout;
    private DataInputStream din;

    public Cliente(Socket s) {
        //
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(mainPanel);
        this.setSize(400, 800);
        this.setVisible(true);
        // SOCKET para comunicacion
        try {
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
            this.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.s = s;
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (!textImput.getText().equals(".bye")) {
                        dout.writeUTF(textImput.getText());
                    } else {
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
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //LEER DEL SERRVIDOR Y ESCRIBIR EN PANTALLA
    @Override
    public void run() {
        while (!aux) {
            try {
                String msg = din.readUTF();
                text1.append(msg + "\n");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }


}

