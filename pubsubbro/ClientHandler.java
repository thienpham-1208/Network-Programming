package pubsubbro;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread
{
    protected Socket socket = null;

    protected ObjectInputStream in = null;

    protected ObjectOutputStream out = null;

    protected long id;

    protected String location;

    ClientHandler[] clientHandlers;

    public ClientHandler(long id, Socket socket, ObjectInputStream in, ObjectOutputStream out, String location, ClientHandler[] clientHandlers)
    {
        this.id = id;
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.location = location;
        this.clientHandlers = clientHandlers;
    }

    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    @Override
    public void run()
    {

    }

    public String getLocation()
    {
        return this.location;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof ClientHandler other))
        {
            return false;
        }
        return id == other.id;
    }

    public void close()
    {
        try
        {
            socket.close();
            in.close();
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String toString()
    {
        return id + " " + location;
    }
}
