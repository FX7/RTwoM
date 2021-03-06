package fx7.r2m.rest.parameter.player;

import java.util.Set;

import org.bukkit.OfflinePlayer;

import fx7.r2m.access.EntityAccess;
import fx7.r2m.entity.PlayerEntity;
import fx7.r2m.rest.RestException;

public interface PlayerParameterProvider
{
	public boolean hasMorePlayer();

	public OfflinePlayer peekPlayer() throws RestException;

	public OfflinePlayer consumePlayer() throws RestException;

	public static OfflinePlayer fromParameters(Set<EntityAccess> entityAccess, String playerName) throws RestException
	{
		OfflinePlayer player = PlayerEntity.getOfflinePlayer(entityAccess, playerName);
		if (player == null)
			throw RestException.invalidParameter("'" + playerName + "' is not a valid player");

		return player;
	}
}
