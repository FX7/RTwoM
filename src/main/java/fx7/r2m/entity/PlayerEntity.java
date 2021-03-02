package fx7.r2m.entity;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import fx7.r2m.access.Context;
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
	public OfflinePlayer toMinecraftParameter() throws RestException
	{
		return PlayerParameterProvider.fromParameters(getEntityName());
	}

	public static OfflinePlayer getOfflinePlayer(String name)
	{
		if (name == null)
			return null;

		OfflinePlayer player = Bukkit.getOfflinePlayer(name);
		if (player == null || !player.hasPlayedBefore())
			player = null;

		return player;
	}
}
