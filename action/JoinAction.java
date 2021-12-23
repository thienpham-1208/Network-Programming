package action;

public class JoinAction extends Action
{
	//Kết nối đến địa điểm
    private final String location;
    private final ClientType clientType;
    public JoinAction(ClientType clientType, String location)
    {
        this.clientType = clientType;
        this.location = location;
    }

    public ClientType getClientType()
    {
        return clientType;
    }

    public String getLocation()
    {
        return this.location;
    }

    public boolean matches(Action act)
    {
        return false;
    }

    public enum ClientType
    {
        PUBLISHER, SUBSCRIBER
    }
}
