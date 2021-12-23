package pubsubbro;

import action.Action;
import action.JoinAction.ClientType;
import action.PublishDataAction;
import action.ReplyJoinAction;
import action.UnsubcriptionAction;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Publisher extends Client
{
    JFrame frame;
    JPanel panel;
    JButton btnSubmit;
    JLabel lbName;
    JTextField tfInput;
    JTextArea taReceipt;
    StringBuilder sb;

    boolean check = true;

    public Publisher(ClientType clientType, String location)
    {
        super(clientType, location);
    }

    public Publisher()
    {
        initialize();
    }

    public static void main(String[] args)
    {
        String location = "hello/hi";
        Publisher pub = new Publisher(ClientType.PUBLISHER, location);
        pub.process();
    }

    public void process()
    {
        Action act = receiveAction();
        if (!(act instanceof ReplyJoinAction))
        {
            return;
        }
        ReplyJoinAction reply = (ReplyJoinAction) act;
        if (!(reply.getState() == 0))
        {
            System.out.println(Config.messageToClient.get(reply.getState()));

        }
        else
        {
            System.out.println(Config.messageToClient.get(reply.getState()));
            int cnt = 0;
            Thread thread = new Thread()
            {
                public void run()
                {
                    System.out.println("type quit");
                    String es = scan.nextLine();
                    if (es.equalsIgnoreCase("quit"))
                    {
                        quit();
                        check = false;
                        System.out.println("quit!!");
                    }
                }
            };
            thread.start();
            while (check)
            {
                try
                {
                    PublishDataAction data_update = new PublishDataAction(this.location + " send data " + String.valueOf(cnt), this.location);
                    out.writeObject(data_update);
                    out.flush();
                    System.out.println("Send completed");
                    cnt += 1;
                    Thread.sleep(Config.SLEEP_TIME);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (InterruptedException e)
                {
                }
            }
        }
    }

    public void initialize()
    {
        sb = new StringBuilder();
        lbName = new JLabel("Location:");
        lbName.setBounds(10, 10, 60, 20);

        // Create textfield to read customer's name
        tfInput = new JTextField(30);
        tfInput.setBounds(70, 10, 100, 20);

        // Create button to submit customer's name
        btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(e ->
        {
            String location = tfInput.getText();
            updateMessage(location);
            Publisher pub = new Publisher(ClientType.PUBLISHER, location);
            pub.process();
        });
        btnSubmit.setBounds(180, 10, 99, 20);
        // Create text area to display receipt
        taReceipt = new JTextArea("", 36, 5);
        taReceipt.setEditable(false);
        taReceipt.setBounds(10, 70, 350, 500);
        // Create panel to hold button, textfields, label, textarea
        panel = new JPanel(null);
        panel.add(lbName);
        panel.add(btnSubmit);
        panel.add(tfInput);
        panel.add(taReceipt);
        panel.setPreferredSize(new Dimension(310, 160));

        // Create frame which is the main window
        frame = new JFrame("Publisher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setBounds(100, 100, 350, 500);
        frame.setVisible(true);
    }

    public void updateMessage(String msg)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                sb.append(msg).append("\n");
                taReceipt.setText(sb.toString());
            }
        });
    }

    public void quit()
    {
        String msg = "quit action from pub " + getLocation();
        sendAction(new UnsubcriptionAction(msg));
        System.out.println("Send quit completed");
        close();
    }
}
