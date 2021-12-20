package pubsubbro;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


import action.Action;
import action.JoinAction.ClientType;
import action.PublishDataAction;
import action.ReplyJoinAction;
import action.UnsubcriptionAction;

public class Publisher extends Client
{
	public Publisher(ClientType clientType, String location) 
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
		} 
		else 
		{		
			System.out.println(Config.messageToClient.get(reply.getState()));
			while (true) 
			{
				try 
				{
					String data = "hello";
					PublishDataAction data_update = new PublishDataAction(data, this.location);		
					out.writeObject(data_update);
					out.flush();
					
					System.out.println("Send completed");
					Thread.sleep(Config.SLEEP_TIME);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				} 
				catch (InterruptedException  e) 
				{

				}
			}
 		}	
	}	
	
	public void quit() 
	{
		String msg = "quit action from pub " + getLocation();	
		sendAction(new UnsubcriptionAction(msg));
		close();	
	}
}
