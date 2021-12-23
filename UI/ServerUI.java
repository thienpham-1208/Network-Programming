package UI;

import javax.swing.*;
import java.awt.*;

public class ServerUI
{
    JFrame frmServerManagement;
    TextArea txtMessage;
    JButton btnStart;

    public static void main(String[] args)
    {
        String location = "b";
        String[] parts = location.split("/");
        System.out.println(parts.length);
        for (String part : parts)
        {
            System.out.println(part);
        }
    }

    public void updateMessage(String msg)
    {
        txtMessage.append(msg + "\n");
    }

    public void initialize()
    {
        frmServerManagement = new JFrame();
        frmServerManagement.setForeground(UIManager.getColor("RadioButtonMenuItem.foreground"));
        frmServerManagement.getContentPane().setFont(new Font("Segoe UI", Font.PLAIN, 13));
        frmServerManagement.getContentPane().setForeground(UIManager.getColor("RadioButtonMenuItem.acceleratorSelectionForeground"));
        frmServerManagement.setTitle("Server Management");
        frmServerManagement.setResizable(false);
        frmServerManagement.setBounds(200, 200, 500, 500);
        frmServerManagement.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmServerManagement.getContentPane().setLayout(null);
        frmServerManagement.setBackground(Color.ORANGE);
        frmServerManagement.setVisible(true);

        btnStart = new JButton("START");
        btnStart.setBackground(UIManager.getColor("RadioButtonMenuItem.selectionBackground"));
        btnStart.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnStart.setBounds(200, 30, 80, 50); /////// Vi tri button START
        frmServerManagement.getContentPane().add(btnStart);
        btnStart.addActionListener(e ->
        {
            process();
            listen();
        });

        txtMessage = new TextArea();
        txtMessage.setBackground(Color.WHITE);
        txtMessage.setForeground(Color.BLACK);
        txtMessage.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtMessage.setEditable(false);
        txtMessage.setBounds(0, 100, 500, 358); ////// Vi tri textArea
        frmServerManagement.getContentPane().add(txtMessage);
    }

    public void process()
    {

    }

    public void listen()
    {

    }

}
