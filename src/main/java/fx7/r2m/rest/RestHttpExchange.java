package fx7.r2m.rest;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sun.net.httpserver.HttpExchange;

import fx7.r2m.access.Context;
import fx7.r2m.access.EntityAccess;
import fx7.r2m.rest.parameter.ParametersProvider;
import fx7.r2m.rest.parameter.request.RequestGetParameters;
import fx7.r2m.rest.parameter.request.RequestPostParameters;
import fx7.r2m.rest.server.RequestMethod;

public class RestHttpExchange implements ParametersProvider
{
	private final HttpExchange httpExchange;
	private final Context context;
	private final Set<EntityAccess> entityAccess = new HashSet<>();

	private RequestGetParameters getParameters;
	private RequestPostParameters postParameters;
	private RequestMethod requestMethod;

	public RestHttpExchange(HttpExchange httpExchange, Context context, Set<EntityAccess> entityAccess)
	{
		this.httpExchange = httpExchange;
		this.context = context;
		if (entityAccess != null)
			this.entityAccess.addAll(entityAccess);
	}

	public Set<EntityAccess> getEntityAccess()
	{
		return entityAccess;
	}

	@Override
	public boolean hasMoreItemStack()
	{
		return getGetParameters().hasMoreItemStack() || getPostParameters().hasMoreItemStack();
	}

	@Override
	public ItemStack peekItemStack() throws RestException
	{
		if (getGetParameters().hasMoreItemStack())
			return getGetParameters().peekItemStack();
		return getPostParameters().peekItemStack();
	}

	@Override
	public ItemStack consumeItemStack() throws RestException
	{
		if (getGetParameters().hasMoreItemStack())
			return getGetParameters().consumeItemStack();
		return getPostParameters().consumeItemStack();
	}

	@Override
	public boolean hasMoreInventory()
	{
		return getGetParameters().hasMoreInventory() || getPostParameters().hasMoreInventory();
	}

	@Override
	public Inventory peekInventory() throws RestException
	{
		if (getGetParameters().hasMoreInventory())
			return getGetParameters().peekInventory();
		return getPostParameters().peekInventory();
	}

	@Override
	public Inventory consumeInventory() throws RestException
	{
		if (getGetParameters().hasMoreInventory())
			return getGetParameters().consumeInventory();
		return getPostParameters().consumeInventory();
	}

	@Override
	public boolean hasMoreMaterial()
	{
		return getGetParameters().hasMoreMaterial() || getPostParameters().hasMoreMaterial();
	}

	@Override
	public Material peekMaterial() throws RestException
	{
		if (getGetParameters().hasMoreMaterial())
			return getGetParameters().peekMaterial();
		return getPostParameters().peekMaterial();
	}

	@Override
	public Material consumeMaterial() throws RestException
	{
		if (getGetParameters().hasMoreMaterial())
			return getGetParameters().consumeMaterial();
		return getPostParameters().consumeMaterial();
	}

	@Override
	public boolean hasMorePlayer()
	{
		return getGetParameters().hasMorePlayer() || getPostParameters().hasMorePlayer();
	}

	@Override
	public OfflinePlayer peekPlayer() throws RestException
	{
		if (getGetParameters().hasMorePlayer())
			return getGetParameters().peekPlayer();
		return getPostParameters().peekPlayer();
	}

	@Override
	public OfflinePlayer consumePlayer() throws RestException
	{
		if (getGetParameters().hasMorePlayer())
			return getGetParameters().consumePlayer();
		return getPostParameters().consumePlayer();
	}

	@Override
	public boolean hasMoreLocation()
	{
		return getGetParameters().hasMoreLocation() || getPostParameters().hasMoreLocation();
	}

	@Override
	public Location peekLocation() throws RestException
	{
		if (getGetParameters().hasMoreLocation())
			return getGetParameters().peekLocation();
		return getPostParameters().peekLocation();
	}

	@Override
	public Location consumeLocation() throws RestException
	{
		if (getGetParameters().hasMoreLocation())
			return getGetParameters().consumeLocation();
		return getPostParameters().consumeLocation();
	}

	@Override
	public boolean hasMorePower()
	{
		return getGetParameters().hasMorePower() || getPostParameters().hasMorePower();
	}

	@Override
	public boolean peekPower() throws RestException
	{
		if (getGetParameters().hasMorePower())
			return getGetParameters().peekPower();
		return getPostParameters().peekPower();
	}

	@Override
	public boolean consumePower() throws RestException
	{
		if (getGetParameters().hasMorePower())
			return getGetParameters().consumePower();
		return getPostParameters().consumePower();
	}

	private RequestGetParameters getGetParameters()
	{
		if (getParameters != null)
			return getParameters;

		getParameters = RequestGetParameters.fromURI(entityAccess, context, getEntityId(), httpExchange);
		return getParameters;
	}

	public final RequestMethod getRequestMethod() throws RestException
	{
		if (requestMethod != null)
			return requestMethod;

		try
		{
			requestMethod = RequestMethod.valueOf(httpExchange.getRequestMethod());
			return requestMethod;
		} catch (IllegalArgumentException e)
		{
			throw RestException.unknownMethod(httpExchange.getRequestMethod(), context);
		}
	}

	public final String getEntityAction()
	{
		String[] path = getPath();
		if (path == null)
			return null;

		return path[path.length - 1];
	}

	public final String getEntityId()
	{
		String[] path = getPath();
		if (path == null)
			return null;

		return path[path.length - 2];
	}

	private String[] getPath()
	{
		// BASE_CONTEXT/CONTEXT/entityId/action
		String[] path = httpExchange.getRequestURI().getPath().split("/");
		if (path == null || path.length < 4)
			return null;

		if (!path[path.length - 3].equals(context.asContext()))
			return null;

		return path;
	}

	private RequestPostParameters getPostParameters()
	{
		if (this.postParameters != null)
			return this.postParameters;

		postParameters = RequestPostParameters.fromData(entityAccess, httpExchange);
		return postParameters;
	}
}
