package fx7.r2m.rest.handler.player;

import fx7.r2m.access.Context;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.RestReturnable;
import fx7.r2m.rest.parameter.player.PlayerParameterProvider;
import fx7.r2m.rest.parameter.player.PlayerParameterReceiver;
import fx7.r2m.rest.server.RequestMethod;

public class GetLastPlayedAction extends RestAction implements PlayerParameterReceiver
{
	private PlayerParameterProvider playerParameter;

	public GetLastPlayedAction(String actionName)
	{
		super(Context.PLAYER, RequestMethod.GET, actionName);
	}

	@Override
	public RestReturnable excecute() throws RestException
	{
		if (playerParameter == null)
			throw RestException.invalidParameter("PlayerParameter missing.");

		String lastPlayed = Long.toString(playerParameter.peekPlayer().getLastPlayed());
		return RestReturnable.plainTextOK(lastPlayed);
	}

	@Override
	public void setPlayerParameter(PlayerParameterProvider parameter)
	{
		this.playerParameter = parameter;
	}
}
