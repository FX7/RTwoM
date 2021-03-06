package fx7.r2m.rest.handler.world;

import org.bukkit.Location;
import org.bukkit.Material;

import fx7.r2m.access.Context;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.RestReturnable;
import fx7.r2m.rest.parameter.location.LocationParameterProvider;
import fx7.r2m.rest.parameter.location.LocationParameterReceiver;
import fx7.r2m.rest.parameter.material.MaterialParameterProvider;
import fx7.r2m.rest.server.RequestMethod;

public class GetMaterialAction extends RestAction implements LocationParameterReceiver, MaterialParameterProvider
{
	private LocationParameterProvider locationParameter;

	private Material material;

	public GetMaterialAction(String actionName)
	{
		super(Context.WORLD, RequestMethod.GET, actionName);
	}

	@Override
	public RestReturnable excecute() throws RestException
	{
		if (locationParameter == null)
			throw RestException.invalidParameter("LocationParameter missing.");

		return RestReturnable.plainTextOK((peekMaterial()).name());
	}

	@Override
	public void setLocationParameter(LocationParameterProvider parameter)
	{
		this.locationParameter = parameter;
	}

	@Override
	public boolean hasMoreMaterial()
	{
		try
		{
			return peekMaterial() != null;
		} catch (RestException e)
		{
			return false;
		}
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

	public Material getNextMaterial(boolean consume) throws RestException
	{
		if (material != null)
		{
			Material material = this.material;
			if (consume)
			{
				this.locationParameter = null;
				this.material = null;
			}
			return material;
		}

		if (locationParameter == null)
			throw RestException.invalidParameter("LocationParameter missing.");

		Location location = locationParameter.peekLocation();
		Material material = location.getBlock().getType();
		this.material = material;
		return material;
	}
}
