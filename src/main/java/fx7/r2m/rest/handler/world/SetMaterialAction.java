package fx7.r2m.rest.handler.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import fx7.r2m.access.Context;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.RestReturnable;
import fx7.r2m.rest.parameter.location.LocationParameterProvider;
import fx7.r2m.rest.parameter.location.LocationParameterReceiver;
import fx7.r2m.rest.parameter.material.MaterialParameterProvider;
import fx7.r2m.rest.parameter.material.MaterialParameterReceiver;
import fx7.r2m.rest.server.RequestMethod;

public class SetMaterialAction extends RestAction implements LocationParameterReceiver, MaterialParameterReceiver
{
	private LocationParameterProvider locationParameter;
	private MaterialParameterProvider materialParameter;

	public SetMaterialAction(String actionName)
	{
		super(Context.WORLD, RequestMethod.POST, actionName);
	}

	@Override
	public RestReturnable excecute() throws RestException
	{
		if (locationParameter == null)
			throw RestException.invalidParameter("LocationParameter missing.");

		if (materialParameter == null)
			throw RestException.invalidParameter("MaterialParameter missing.");

		Location location = locationParameter.peekLocation();
		Material material = materialParameter.peekMaterial();

		coordinator.callSyncMethod(() -> setMaterial(location, material));

		return RestReturnable.simpleOK();
	}

	@Override
	public void setMaterialParameter(MaterialParameterProvider parameter)
	{
		this.materialParameter = parameter;
	}

	@Override
	public void setLocationParameter(LocationParameterProvider parameter)
	{
		this.locationParameter = parameter;
	}

	private Void setMaterial(Location location, Material material)
	{
		Block block = location.getBlock();
		if (block.getType() != material)
			block.setType(material);
		return null;
	}
}
