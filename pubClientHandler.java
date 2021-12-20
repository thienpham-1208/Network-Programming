package pubsubbro;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;

import action.Action;
import action.PublishDataAction;
import action.UnsubcriptionAction;
import action.JoinAction.ClientType;

public class pubClientHandler extends ClientHandler 
{

	private Set<subClientHandler> sub;
	
	
	public pubClientHandler(long id, Socket socket, ObjectInputStream in, ObjectOutputStream out, Set<subClientHandler> setSub, String location) 
	{
		super(id, socket, in, out, location);
		// TODO Auto-generated constructor stub
		this.sub = setSub;	
	}
	
	public void setSub(Set<subClientHandler> sub) 
	{
		this.sub = sub;
	}
	
	public Set<subClientHandler> getSub() 
	{
		return this.sub;
	}
		
	
	@Override
	public void run() 
	{
		while (true) 
		{
			try 
			{
				Action act = (Action) this.in.readObject();				
					// get data to other sub
				for (subClientHandler s : sub) 
				{
					s.sendAction(act);									
				}  		
			} 
			catch (IOException e) 
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
