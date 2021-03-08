package fx7.r2m.rest.parameter.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sun.net.httpserver.HttpExchange;

import fx7.r2m.access.Context;
import fx7.r2m.access.EntityAccess;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.parameter.location.LocationParameterProvider;
import fx7.r2m.rest.parameter.material.MaterialParameterProvider;
import fx7.r2m.rest.parameter.player.PlayerParameterProvider;

public class RequestGetParameters extends RequestParameters
{
	private static final String PARAM_MATERIAL = "m";
	private int materialIndex = 0;

	private static final String PARAM_LOCATION_WORLD = "w";
	private static final String PARAM_LOCATION_X = "x";
	private static final String PARAM_LOCATION_Y = "y";
	private static final String PARAM_LOCATION_Z = "z";
	private int locationIndex = 0;

	private static final String PARAM_PLAYER = "p";
	private int playerIndex = 0;

	private final Context context;
	private final String entityId;
	private final Map<String, List<String>> parameters;

	private RequestGetParameters(Set<EntityAccess> entityAccess, Context context, String entityId,
			Map<String, List<String>> parameters)
	{
		super(entityAccess);
		this.context = context;
		this.entityId = entityId;
		this.parameters = parameters;
	}

	@Override
	public boolean hasMoreLocation()
	{
		if (context == Context.WORLD && locationIndex == 0)
			return true;
		return hasParameter(PARAM_LOCATION_WORLD, locationIndex);
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
		String worldName;
		if (context == Context.WORLD && locationIndex == 0)
			worldName = getEntityId();
		else
			worldName = getString(PARAM_LOCATION_WORLD, locationIndex);

		Location location = LocationParameterProvider.fromParameters(entityAccess, //
				worldName, //
				getDouble(PARAM_LOCATION_X, locationIndex), //
				getDouble(PARAM_LOCATION_Y, locationIndex), //
				getDouble(PARAM_LOCATION_Z, locationIndex));

		if (consume)
			locationIndex++;
		return location;
	}

	@Override
	public boolean hasMorePlayer()
	{
		if (context == Context.PLAYER && playerIndex == 0)
			return true;
		return hasParameter(PARAM_PLAYER, playerIndex);
	}

	@Override
	public OfflinePlayer peekPlayer() throws RestException
	{
		return getNextPlayer(false);
	}

	@Override
	public OfflinePlayer consumePlayer() throws RestException
	{
		return getNextPlayer(true);
	}

	private OfflinePlayer getNextPlayer(boolean consume) throws RestException
	{
		String playerName;
		if (context == Context.PLAYER && playerIndex == 0)
			playerName = getEntityId();
		else
			playerName = getString(PARAM_PLAYER, playerIndex);

		OfflinePlayer player = PlayerParameterProvider.fromParameters(entityAccess, playerName);

		if (consume)
			playerIndex++;
		return player;
	}

	@Override
	public boolean hasMoreMaterial()
	{
		return hasParameter(PARAM_MATERIAL, materialIndex);
	}

	@Override
	public Material peekMaterial() throws RestException
	{
		return getNextMaterial(false);
	}

	@Override
	public Material consumeMaterial() throws RestException
	{
		return getNextMaterial(true);
	}

	private Material getNextMaterial(boolean consume) throws RestException
	{
		return MaterialParameterProvider.fromParameters(entityAccess,
				getString(PARAM_MATERIAL, consume ? materialIndex++ : materialIndex));
	}

	@Override
	public boolean hasMoreItemStack()
	{
		return false;
	}

	@Override
	public ItemStack peekItemStack() throws RestException
	{
		throw RestException.invalidParameter("No ItemStackParameter");
	}

	@Override
	public ItemStack consumeItemStack() throws RestException
	{
		throw RestException.invalidParameter("No ItemStackParameter");
	}

	@Override
	public boolean hasMoreInventory()
	{
		return false;
	}

	@Override
	public Inventory peekInventory() throws RestException
	{
		throw RestException.invalidParameter("No ItemInventoryParameter");
	}

	@Override
	public Inventory consumeInventory() throws RestException
	{
		throw RestException.invalidParameter("No ItemInventoryParameter");
	}

	@Override
	public boolean hasMorePower()
	{
		return false;
	}

	@Override
	public boolean peekPower() throws RestException
	{
		throw RestException.invalidParameter("No PowertParameter");
	}

	@Override
	public boolean consumePower() throws RestException
	{
		throw RestException.invalidParameter("No PowertParameter");
	}

	@Override
	public String toString()
	{
		return parameters.toString();
	}

	public static RequestGetParameters fromURI(Set<EntityAccess> entityAccess, Context context, String entityId,
			HttpExchange httpExchange)
	{
		Map<String, List<String>> requestParameters = new HashMap<>();
		String[] split = httpExchange.getRequestURI().toString().split("\\?");
		if (split.length == 2)
		{
			for (String param : split[1].split("&"))
			{
				String[] keyValue = param.split("=");
				if (keyValue.length != 2)
					continue;

				addParameter(requestParameters, keyValue[0], keyValue[1]);
			}
		}

		return new RequestGetParameters(entityAccess, context, entityId, requestParameters);
	}

	private List<String> getAll(String key)
	{
		List<String> parameters = this.parameters.get(key);
		if (parameters == null)
			return new ArrayList<>();
		return parameters;
	}

	private String getString(String key, int index) throws RestException
	{
		List<String> parameters = getAll(key);
		if (parameters == null || index >= parameters.size())
			throw RestException.invalidParameter("String parameter '" + key + "' at " + index + " is missing.");
		return parameters.get(index);
	}

	private boolean hasParameter(String key, int index)
	{
		List<String> parameters = getAll(key);
		return parameters != null && parameters.size() > index;
	}

	private double getDouble(String key, int index) throws RestException
	{
		List<String> parameters = getAll(key);
		if (parameters == null || index >= parameters.size())
			throw RestException.invalidParameter("Double parameter '" + key + "' at " + index + " is missing.");

		String parameter = parameters.get(index);
		return asDouble(parameter);
	}

	private String getEntityId()
	{
		return entityId;
	}

	private static void addParameter(Map<String, List<String>> requestParameters, String key, String value)
	{
		if (key == null || key.isEmpty() || value == null || value.isEmpty())
			return;

		List<String> parameters = requestParameters.get(key);
		if (parameters == null)
		{
			parameters = new ArrayList<>();
			requestParameters.put(key, parameters);
		}
		parameters.add(value);
	}
}
