package pubsubbro;

import action.Action;
import action.JoinAction.ClientType;
import action.UnsubcriptionAction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

public class pubClientHandler extends ClientHandler
{

    boolean check = true;
    private Set<subClientHandler> sub;


    public pubClientHandler(long id, Socket socket, ObjectInputStream in, ObjectOutputStream out, Set<subClientHandler> setSub, String location, ClientHandler[] clientHandlers)
    {
        super(id, socket, in, out, location, clientHandlers);
        // TODO Auto-generated constructor stub
        this.sub = setSub;
    }

    public static void main(String[] args)
    {
        String location = "hello";
        Subscriber sub = new Subscriber(ClientType.SUBSCRIBER, location);
        sub.process();
    }

    public Set<subClientHandler> getSub()
    {
        return this.sub;
    }

    public void setSub(Set<subClientHandler> sub)
    {
        this.sub = sub;
    }

    @Override
    public void run()
    {
        while (check)
        {
            try
            {
                Action act = (Action) this.in.readObject();
                // send data to other sub
                for (subClientHandler s : sub)
                {
                    s.sendAction(act);
                }
                /*
                 * for (subClientHandler s : sub) { for (ClientHandler cl : clientHandlers) { if
                 * (cl instanceof subClientHandler) { subClientHandler sub = (subClientHandler)
                 * cl; if (sub.equals(cl)) { System.out.println(sub); s.sendAction(act); } } } }
                 */
                if (act instanceof UnsubcriptionAction)
                {
                    for (subClientHandler s : sub)
                    {
                        for (int i = 0; i < Config.MAX_CONNECTIONS; i++)
                        {
                            if (clientHandlers[i] instanceof subClientHandler)
                            {
                                subClientHandler sub = (subClientHandler) clientHandlers[i];
                                if (sub.equals(s))
                                {
                                    clientHandlers[i] = null;
                                    s.close();
                                }
                            }
                        }
                    }
                    for (int i = 0; i < Config.MAX_CONNECTIONS; i++)
                    {
                        if (clientHandlers[i] instanceof pubClientHandler)
                        {
                            pubClientHandler pub = (pubClientHandler) clientHandlers[i];
                            if (pub.equals(this))
                            {
                                clientHandlers[i] = null;
                                check = false;
                                close();
                            }
                        }
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

}
