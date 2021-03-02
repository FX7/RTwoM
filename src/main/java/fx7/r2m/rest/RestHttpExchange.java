package fx7.r2m.rest;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import com.sun.net.httpserver.HttpExchange;

import fx7.r2m.access.AppAccess;
import fx7.r2m.access.Context;
import fx7.r2m.access.EntityAccess;
import fx7.r2m.entity.script.ScriptManager;
import fx7.r2m.rest.parameter.ParametersProvider;
import fx7.r2m.rest.parameter.request.RequestGetParameters;
import fx7.r2m.rest.parameter.request.RequestPostParameters;
import fx7.r2m.rest.server.RequestMethod;

public class RestHttpExchange implements ParametersProvider
{
	protected final HttpExchange httpExchange;
	private final AppAccess appAccess;
	private final ScriptManager scriptManager;
	private final Context context;

	private RequestGetParameters getParameters;
	private RequestPostParameters postParameters;
	private RequestMethod requestMethod;

	public RestHttpExchange(HttpExchange httpExchange, AppAccess appAccess, ScriptManager scriptManager,
			Context context)
	{
		this.httpExchange = httpExchange;
		this.appAccess = appAccess;
		this.scriptManager = scriptManager;
		this.context = context;
	}

	public AppAccess getAppAccess()
	{
		return appAccess;
	}

	public ScriptManager getScriptManager()
	{
		return scriptManager;
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
			getGetParameters().peekPlayer();
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

	public List<EntityAccess> getEntityAccess() throws RestException
	{
		return getPostParameters().getEntityAccess();
	}

	private RequestGetParameters getGetParameters()
	{
		if (getParameters != null)
			return getParameters;

		getParameters = RequestGetParameters.fromURI(context, getEntityId(), httpExchange);
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

		postParameters = RequestPostParameters.fromData(httpExchange);
		return postParameters;
	}
}
