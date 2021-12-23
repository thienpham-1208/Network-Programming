package pubsubbro;

import java.util.HashMap;
import java.util.Map;

public class Config
{
    public static final int PORT = 1234;

    public static final int MAX_CONNECTIONS = 5;

    public static final int SLEEP_TIME = 5000;

    public static final Map<Integer, String> messageToClient = new HashMap<>();

    static
    {
        messageToClient.put(0, "Server accept JoinAction");
        messageToClient.put(1, "Server too busy");
        messageToClient.put(2, "Server haves Publisher at your location");
        messageToClient.put(3, "Server doesn't have Publisher at your location");
        messageToClient.put(4, "Publisher has invalid location");
        messageToClient.put(5, "Subscriber has invalid location");
    }
}


