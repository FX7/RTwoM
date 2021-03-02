package fx7.r2m.rest.handler.player;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fx7.r2m.access.Context;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.RestReturnable;
import fx7.r2m.rest.parameter.location.LocationParameter;
import fx7.r2m.rest.parameter.location.LocationParameterProvider;
import fx7.r2m.rest.parameter.player.PlayerParameterProvider;
import fx7.r2m.rest.parameter.player.PlayerParameterReceiver;
import fx7.r2m.rest.server.RequestMethod;

public class GetLastLocationAction extends RestAction implements PlayerParameterReceiver, LocationParameterProvider
{
	private PlayerParameterProvider playerParameter;

	private Location location;

	public GetLastLocationAction(String actionName)
	{
		super(Context.PLAYER, RequestMethod.GET, actionName);
	}

	@Override
	public void setPlayerParameter(PlayerParameterProvider parameter)
	{
		this.playerParameter = parameter;
	}

	@Override
	public RestReturnable excecute() throws RestException
	{
		return LocationParameter.fromLocation(peekLocation());
	}

	@Override
	public boolean hasMoreLocation()
	{
		try
		{
			return peekLocation() != null;
		} catch (RestException e)
		{
			return false;
		}
	}

	@Override
	public Location peekLocation() throws RestException
	{
		return getNextLocation(false);
	}

	@Override
	public Location consumeLocation() throws RestException
	{
		return getNextLocation(true);
	}

	private Location getNextLocation(boolean consume) throws RestException
	{
		if (location != null)
		{
			Location location = this.location;
			if (consume)
			{
				this.playerParameter = null;
				this.location = null;
			}
			return location;
		}

		if (playerParameter == null)
			throw RestException.invalidParameter("PlayerParameter missing.");

		OfflinePlayer offlinePlayer = playerParameter.peekPlayer();
		if (offlinePlayer.getLastPlayed() == 0)
			throw RestException.invalidParameter("Player '" + offlinePlayer.getName() + "' never played before.");

		Location location = null;

		Player onlinePlayer = offlinePlayer.getPlayer();
		if (onlinePlayer != null)
			location = onlinePlayer.getLocation();
		else
			location = LocationParameter.getLocationFromOfflinePlayer(offlinePlayer);

		if (location == null)
			throw RestException.unexpectedBehavior(
					"Couldnt determine the last location of player '" + offlinePlayer.getName() + "'!");

		this.location = location;
		return location;
	}
}
