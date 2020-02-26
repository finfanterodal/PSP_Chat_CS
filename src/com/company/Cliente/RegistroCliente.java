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

    //
    public RegistroCliente() {
        //
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.add(mainPanel);
        this.setSize(600, 400);

        //
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    String nickName = textNickname.getText();
                    Socket s = new Socket();
                    InetSocketAddress addr = new InetSocketAddress(textIP.getText(), Integer.parseInt(textPort.getText()));
                    s.connect(addr);
                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                    dout.writeUTF(nickName);
                    String validacion = new DataInputStream(s.getInputStream()).readUTF();
                    if (validacion.equals("registrado")) {
                        JOptionPane.showMessageDialog(null, "Ya estás registrado.");
                    } else {
                        setVisible(false);
                        Cliente cliente = new Cliente(s);
                    }
                } catch (Exception ex) {

                }
            }
        });
    }


    public static void main(String[] args) {
        RegistroCliente myClient = new RegistroCliente();
    }
}