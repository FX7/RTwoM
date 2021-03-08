package fx7.r2m.rest.handler.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;

import fx7.r2m.access.Context;
import fx7.r2m.rest.RestAction;
import fx7.r2m.rest.RestException;
import fx7.r2m.rest.RestReturnable;
import fx7.r2m.rest.parameter.location.LocationParameterProvider;
import fx7.r2m.rest.parameter.location.LocationParameterReceiver;
import fx7.r2m.rest.parameter.powerable.PowerParameterProvider;
import fx7.r2m.rest.parameter.powerable.PowerParameterReceiver;
import fx7.r2m.rest.server.RequestMethod;

public class SetLeverPower extends RestAction implements LocationParameterReceiver, PowerParameterReceiver
{
	private LocationParameterProvider locationParameter;
	private PowerParameterProvider powerParameter;

	public SetLeverPower(String actionName)
	{
		super(Context.WORLD, RequestMethod.POST, actionName);
	}

	@Override
	public void setLocationParameter(LocationParameterProvider parameter)
	{
		this.locationParameter = parameter;
	}

	@Override
	public void setPowerParameter(PowerParameterProvider parameter)
	{
		this.powerParameter = parameter;
	}

	@Override
	public RestReturnable excecute() throws RestException
	{
		if (locationParameter == null)
			throw RestException.invalidParameter("LocationParameter missing.");

		if (powerParameter == null)
			throw RestException.invalidParameter("PowerParameter missing.");

		Location location = locationParameter.peekLocation();
		boolean powered = powerParameter.peekPower();

		coordinator.callSyncMethod(() -> setPower(location, powered));
		return RestReturnable.simpleOK();
	}

	private Void setPower(Location location, boolean powered) throws RestException
	{
		Block block = location.getBlock();
		BlockData blockData = block.getBlockData();
		if (block.getType() != Material.LEVER || !(blockData instanceof Powerable))
			throw RestException.invalidParameter("Block at given location is no Lever / not powerable");

		Powerable powerData = (Powerable) blockData;
		powerData.setPowered(powered);
		block.setBlockData(powerData);
		block.getState().setBlockData(powerData);
		block.getState().update(true);
		
		return null;
	}
}
