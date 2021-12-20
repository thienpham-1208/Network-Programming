package pubsubbro;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import action.Action;
import action.PublishDataAction;
import action.UnsubcriptionAction;
import action.JoinAction.ClientType;

import java.io.IOException;

public class subClientHandler extends ClientHandler 
{	
	
	public subClientHandler(long id, Socket socket, ObjectInputStream in, ObjectOutputStream out, String location) 
	{
		super(id, socket, in, out, location);
		// TODO Auto-generated constructor stub		
	}
	
	public ObjectOutputStream getOut() 
	{
		return this.out;
	}
	
	@Override
	public void run() 
	{
		while (true) 
		{			
			try 
			{
				Object act = this.in.readObject();
				if (act == null || !(act instanceof UnsubcriptionAction)) 
				{
					continue;
				}
				Thread.sleep(Config.SLEEP_TIME);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) 
			{
				e.printStackTrace();
			} 
			catch (InterruptedException  e) 
			{				
			}			
		}
					
	}
	
	public void sendAction(Action act) 
	{
		try 
		{
			out.writeObject(act);
			out.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		String location = "hi";
		Subscriber pub = new Subscriber(ClientType.SUBSCRIBER, location);
		pub.process();
	}
}