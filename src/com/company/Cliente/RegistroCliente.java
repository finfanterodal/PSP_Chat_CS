package com.company.Cliente;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class RegistroCliente extends JFrame {
    //
    private JTextField textNickname;
    private JButton connectButton;
    private JTextField textIP;
    private JTextField textPort;
    private JPanel mainPanel;
    //
    private Socket s;
    private DataOutputStream dout;


    //
    public RegistroCliente() {
        //
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(mainPanel);
        this.setSize(600, 400);
        this.setVisible(true);

        //
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    String nickName = textNickname.getText();
                    s = new Socket();
                    InetSocketAddress addr = new InetSocketAddress(textIP.getText(), Integer.parseInt(textPort.getText()));
                    s.connect(addr);
                    dout = new DataOutputStream(s.getOutputStream());
                    dout.writeUTF(nickName);
                    String validacion = new DataInputStream(s.getInputStream()).readUTF();
                    if (validacion.equals("registrado")) {
                        JOptionPane.showMessageDialog(null, "Ya está registrado.");
                    } else {
                        setVisible(false);
                        new Cliente(s);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    public static void main(String[] args) {
        new RegistroCliente();

    }


}
