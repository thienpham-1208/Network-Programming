package pubsubbro;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import action.Action;
import action.PublishDataAction;
import action.ReplyJoinAction;
import action.UnsubcriptionAction;
import action.JoinAction.ClientType;


public class Subscriber extends Client
{
	
	public Subscriber(ClientType clientType, String location) 
	{
		super(clientType, location);		
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
			close();
		} 
		else 
		{		
			System.out.println(Config.messageToClient.get(reply.getState()));
			while (true) 
			{
				try 
				{
					Action action = (Action) in.readObject();
					if (action instanceof PublishDataAction) 
					{
						PublishDataAction updateData = (PublishDataAction) action;
						System.out.println(updateData.getData());			
					} 
					else if (action instanceof UnsubcriptionAction) 
					{
						UnsubcriptionAction mess = (UnsubcriptionAction) action;
						System.out.println(mess.getMessage());
						close();						
					}
				} 
				catch(IOException e) 
				{
					e.printStackTrace();
				} 
				catch(ClassNotFoundException e) 
				{
					e.printStackTrace();
				}
			}
		}	
	}
	
	public void quit() 
	{
		String msg = "quit action from sub " + socket.getLocalPort();	
		sendAction(new UnsubcriptionAction(msg));
		close();	
	}
	
	public static void main(String[] args) 
	{
		String location = "hello";
		Subscriber pub = new Subscriber(ClientType.SUBSCRIBER, location);
		pub.process();
	}	
}
