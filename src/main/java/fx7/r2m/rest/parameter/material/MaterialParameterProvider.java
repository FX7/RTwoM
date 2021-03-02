package fx7.r2m.rest.parameter.material;

import org.bukkit.Material;

import fx7.r2m.rest.RestException;

public interface MaterialParameterProvider
{
	public boolean hasMoreMaterial();

	public Material peekMaterial() throws RestException;

	public Material consumeMaterial() throws RestException;

	public static Material fromParameters(String materialName) throws RestException
	{
		Material material = null;
		if (materialName != null && !materialName.isEmpty())
			material = Material.getMaterial(materialName);

		if (material == null)
			throw RestException.invalidParameter("'" + materialName + "' is not a valid material");

		return material;
	}
}
