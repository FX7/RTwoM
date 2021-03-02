package fx7.r2m.access;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import fx7.r2m.rest.parameter.RestParameter;

@SerializableAs("EntityAccess")
public class EntityAccess implements ConfigurationSerializable, RestParameter
{
	private static final String PARAM_CONTEXT = "context";
	private Context context;

	private static final String PARAM_ENTITY_NAME = "entityName";
	private String entityName;

	private static final String PARAM_ACCESS_KEY = "accessKey";
	private String accessKey;

	EntityAccess(Context context, String entityName, String accessKey)
	{
		this.context = context;
		this.entityName = entityName;
		this.accessKey = accessKey;
	}

	public Context getContext()
	{
		return context;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public String getAccessKey()
	{
		return accessKey;
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> serialized = new HashMap<String, Object>();

		serialized.put(PARAM_CONTEXT, getContext().name());
		serialized.put(PARAM_ENTITY_NAME, getEntityName());
		serialized.put(PARAM_ACCESS_KEY, getAccessKey());

		return serialized;
	}

	public static EntityAccess deserialize(Map<String, Object> serialized)
	{
		return new EntityAccess(//
				Context.fromString((String) serialized.get(PARAM_CONTEXT)), //
				(String) serialized.get(PARAM_ENTITY_NAME), //
				(String) serialized.get(PARAM_ACCESS_KEY));
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessKey == null) ? 0 : accessKey.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((entityName == null) ? 0 : entityName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityAccess other = (EntityAccess) obj;
		if (accessKey == null)
		{
			if (other.accessKey != null)
				return false;
		} else if (!accessKey.equals(other.accessKey))
			return false;
		if (context != other.context)
			return false;
		if (entityName == null)
		{
			if (other.entityName != null)
				return false;
		} else if (!entityName.equals(other.entityName))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "EntityAccess [context=" + context + ", entityName=" + entityName + ", accessKey=*****]";
	}
}
