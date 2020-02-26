package com.company.Cliente;

import javax.swing.*;
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

public class Cliente extends JFrame {

    private JTextField textImput;
    private JButton sendButton;
    private JTextArea text1;
    private JButton seleccionarButton;
    private JTextArea text2;
    private JPanel mainPanel;
    private Socket s;


    public Cliente(Socket s) {
        //
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.add(mainPanel);
        this.setSize(400, 400);
        // SOCKET para comunicacion
        this.s = s;


        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

    }


    public static void main(String[] args) {


    }
}

