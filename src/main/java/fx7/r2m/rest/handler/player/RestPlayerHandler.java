package fx7.r2m.rest.handler.player;

import org.bukkit.OfflinePlayer;

import fx7.r2m.Coordinator;
import fx7.r2m.access.Context;
import fx7.r2m.rest.handler.AbstractEntityRestHandler;

public class RestPlayerHandler extends AbstractEntityRestHandler<OfflinePlayer>
{
	public static final Context CONTEXT = Context.PLAYER;

	public RestPlayerHandler(Coordinator coordinator)
	{
		super(coordinator);
	}

	@Override
	protected Context getContext()
	{
		return CONTEXT;
	}
}
