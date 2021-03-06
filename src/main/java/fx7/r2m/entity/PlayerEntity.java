package fx7.r2m.entity;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import fx7.r2m.access.AccessManager;
import fx7.r2m.access.Context;
import fx7.r2m.access.EntityAccess;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.parameter.MinecraftParameter;
import fx7.r2m.rest.parameter.player.PlayerParameterProvider;

public class PlayerEntity extends ContextEntity implements MinecraftParameter<OfflinePlayer>
{
	public PlayerEntity(String name)
	{
		super(Context.PLAYER, name);
	}

	@Override
	public OfflinePlayer toMinecraftParameter(Set<EntityAccess> enntityAccess) throws RestException
	{
		return PlayerParameterProvider.fromParameters(enntityAccess, getEntityName());
	}

	public static OfflinePlayer getOfflinePlayer(Set<EntityAccess> enntityAccess, String name) throws RestException
	{
		if (!AccessManager.getInstance().hasAccess(enntityAccess, Context.PLAYER, name))
			throw RestException.noEntityAccess(name, Context.PLAYER);

		if (name == null)
			return null;

		OfflinePlayer player = Bukkit.getOfflinePlayer(name);
		if (player == null || !player.hasPlayedBefore())
			player = null;

		return player;
	}
}
