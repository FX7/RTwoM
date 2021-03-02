package fx7.r2m.rest.parameter.request;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;

import fx7.r2m.access.EntityAccess;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.parameter.itemstack.ItemStackParameter;
import fx7.r2m.rest.parameter.itemstack.ItemStackParameterProvider;
import fx7.r2m.rest.parameter.location.LocationParameter;
import fx7.r2m.rest.parameter.location.LocationParameterProvider;
import fx7.r2m.rest.parameter.material.MaterialParameterProvider;
import fx7.r2m.rest.parameter.player.PlayerParameterProvider;

public class RequestPostParameters extends RequestParameters implements PlayerParameterProvider,
		MaterialParameterProvider, ItemStackParameterProvider, LocationParameterProvider
{
	private static final String PARAM_MATERIALS = "materials";
	private static final String PARAM_MATERIAL = "material";
	private ParameterList<String> materialParameters;

	private static final String PARAM_PLAYERS = "players";
	private static final String PARAM_PLAYER = "player";
	private ParameterList<String> playerParameters;

	private static final String PARAM_ITEM_STACKS = "itemStacks";
	private static final String PARAM_ITEM_STACK = "itemStack";
	private ParameterList<ItemStackParameter> itemStackParameters;

	private static final String PARAM_LOCATIONS = "locations";
	private static final String PARAM_LOCATION = "location";
	private ParameterList<LocationParameter> locationParameters;

	private static final String PARAM_ACCESS = "entityAccess";
	private static final Class<EntityAccess> PARAM_ACCESS_CLASS = EntityAccess.class;

	private final Map<String, Object> parameters;

	private RequestPostParameters(Map<String, Object> parameters)
	{
		this.parameters = parameters;
		this.materialParameters = new ParameterList<>(parameters, PARAM_MATERIAL, PARAM_MATERIALS, String.class);
		this.playerParameters = new ParameterList<>(parameters, PARAM_PLAYER, PARAM_PLAYERS, String.class);
		this.itemStackParameters = new ParameterList<>(parameters, PARAM_ITEM_STACK, PARAM_ITEM_STACKS,
				ItemStackParameter.class);
		this.locationParameters = new ParameterList<>(parameters, PARAM_LOCATION, PARAM_LOCATIONS,
				LocationParameter.class);
	}

	@Override
	public boolean hasMoreMaterial()
	{
		return materialParameters.hasNext();
	}

	@Override
	public Material peekMaterial() throws RestException
	{
		return MaterialParameterProvider.fromParameters(materialParameters.peekNext());
	}

	@Override
	public Material consumeMaterial() throws RestException
	{
		return MaterialParameterProvider.fromParameters(materialParameters.getNext());
	}

	@Override
	public boolean hasMorePlayer()
	{
		return playerParameters.hasNext();
	}

	@Override
	public OfflinePlayer peekPlayer() throws RestException
	{
		return PlayerParameterProvider.fromParameters(playerParameters.peekNext());
	}

	@Override
	public OfflinePlayer consumePlayer() throws RestException
	{
		return PlayerParameterProvider.fromParameters(playerParameters.getNext());
	}

	@Override
	public boolean hasMoreItemStack()
	{
		return this.itemStackParameters.hasNext();
	}

	@Override
	public ItemStack peekItemStack() throws RestException
	{
		return itemStackParameters.peekNext().toMinecraftParameter();

	}

	@Override
	public ItemStack consumeItemStack() throws RestException
	{
		return itemStackParameters.getNext().toMinecraftParameter();
	}

	@Override
	public boolean hasMoreLocation()
	{
		return locationParameters.hasNext();
	}

	@Override
	public Location peekLocation() throws RestException
	{
		return locationParameters.peekNext().toMinecraftParameter();
	}

	@Override
	public Location consumeLocation() throws RestException
	{
		return locationParameters.getNext().toMinecraftParameter();
	}

	public List<EntityAccess> getEntityAccess() throws RestException
	{
		return getList(PARAM_ACCESS, PARAM_ACCESS_CLASS, parameters.get(PARAM_ACCESS));
	}

	private static <T> List<T> getList(String key, Class<T> clazz, Object value) throws RestException
	{
		List<?> list = get(key, List.class, value);
		List<T> result = new ArrayList<>(list.size());
		for (Object v : list)
		{
			T t = get("-", clazz, v);
			result.add(t);
		}
		return result;
	}

	private static <T> T get(String key, Class<T> clazz, Object value) throws RestException
	{
		if (value == null)
			throw RestException.invalidParameter(clazz.getName() + " parameter '" + key + "' expected!");

		Gson gson = new Gson();
		if (value instanceof Map)
		{
			try
			{
				JsonElement jsonElement = gson.toJsonTree(value);
				T fromJson = gson.fromJson(jsonElement, clazz);
				if (fromJson == null)
					throw RestException.invalidParameter("Parameter '" + key + "' is not a valid " + clazz.getName());

				return fromJson;
			} catch (JsonSyntaxException e)
			{
				throw RestException.invalidParameter("Parameter '" + key + "' is not a valid " + clazz.getName());
			}
		} else if (value instanceof List)
		{
			try
			{
				JsonElement jsonElement = gson.toJsonTree(value);
				T fromJson = gson.fromJson(jsonElement, clazz);
				if (fromJson == null)
					throw RestException.invalidParameter("Parameter '" + key + "' is not a valid " + clazz.getName());

				return fromJson;
			} catch (JsonSyntaxException e)
			{
				throw RestException.invalidParameter("Parameter '" + key + "' is not a valid " + clazz.getName());
			}
		}
		{
			try
			{
				return (T) value;
			} catch (ClassCastException e)
			{
				throw RestException.invalidParameter("Parameter '" + key + "' is not a valid " + clazz.getName());
			}
		}
	}

	public static RequestPostParameters fromData(HttpExchange httpExchange)
	{
		InputStream inputStream = httpExchange.getRequestBody();

		BufferedReader jsonReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
		Map<String, Object> parameters = null;

		try
		{
			Gson gson = new Gson();
			Type type = new TypeToken<Map<String, Object>>()
			{
			}.getType();
			parameters = gson.fromJson(jsonReader, type);
		} catch (Exception e)
		{
		}

		if (parameters == null)
			parameters = new HashMap<>();

		return new RequestPostParameters(parameters);
	}

	private static class ParameterList<T>
	{
		private final Map<String, Object> parametersMap;

		private final String singleParameter;
		private final String arrayParameter;
		private final Class<T> clazz;

		private List<T> parameters;

		public ParameterList(Map<String, Object> parametersMap, String singleParameter, String arrayParameter,
				Class<T> clazz)
		{
			this.parametersMap = parametersMap;
			this.singleParameter = singleParameter;
			this.arrayParameter = arrayParameter;
			this.clazz = clazz;
		}

		public boolean hasNext()
		{
			if (this.parameters == null)
				this.parameters = buildParameters();

			return parameters.size() > 0;
		}

		public T peekNext() throws RestException
		{
			return getNext(false);
		}

		public T getNext() throws RestException
		{
			return getNext(true);
		}

		private T getNext(boolean consume) throws RestException
		{
			if (this.parameters == null)
				this.parameters = buildParameters();

			if (parameters.isEmpty())
				throw RestException
						.invalidParameter("No (more) '" + singleParameter + "' / '" + arrayParameter + "' parameters");
			if (consume)
				return parameters.remove(0);
			return parameters.get(0);
		}

		private List<T> buildParameters()
		{
			List<T> parameters = null;
			try
			{
				// build parametersList
				T parameter = get(singleParameter, clazz, parametersMap.get(singleParameter));
				parameters = new ArrayList<>();
				parameters.add(parameter);
			} catch (RestException e)
			{
			}

			// we get here, if single parameter failed
			try
			{
				// build parametersList
				parameters = getList(arrayParameter, clazz, parametersMap.get(arrayParameter));
			} catch (RestException e)
			{
			}

			if (parameters == null)
				parameters = new ArrayList<>();
			return parameters;
		}
	}
}
