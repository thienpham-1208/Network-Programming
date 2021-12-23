package action;

import java.io.Serial;
import java.io.Serializable;

public abstract class Action implements Serializable
{
    @Serial
    private static final long serialVersionUID = -5178457197113136818L;

    public abstract boolean matches(Action act);
}
