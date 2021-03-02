package fx7.r2m.access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import fx7.r2m.rest.RestException;

@SerializableAs("AppAccess")
public class AppAccess implements ConfigurationSerializable, Comparable<AppAccess>
{
	private static final String PARAM_APP_NAME = "appName";
	private String appName;

	private static final String PARAM_APP_KEY = "appKey";
	private String appKey;

	private static final String PARAM_CONTEXTS = "contexts";
	private Set<Context> contexts = new HashSet<>();

	private static final String PARAM_ENTITY_ACCESS = "entityAccess";
	private Set<EntityAccess> entityAccess = new HashSet<>();

	private AppAccess(String appName, String appKey)
	{
		this.appName = appName;
		this.appKey = appKey;
	}

	public String getAppKey()
	{
		return appKey;
	}

	public String getAppName()
	{
		return appName;
	}

	private void checkAccess(Context context) throws RestException
	{
		if (context == null || !contexts.contains(context))
			throw RestException.noContextAccess(context);
	}

	public void checkAccess(Context context, String entityName, String accessKey) throws RestException
	{
		checkAccess(new EntityAccess(context, entityName, accessKey));
	}

	public void checkAccess(EntityAccess entityAccess) throws RestException
	{
		if (entityAccess == null)
			throw RestException.noEntityAccess(null, null);

		checkAccess(entityAccess.getContext());

		Optional<EntityAccess> access = this.entityAccess.stream().filter(e -> e.equals(entityAccess)).findFirst();
		if (!access.isPresent())
			throw RestException.noEntityAccess(entityAccess.getEntityName(), entityAccess.getContext());
	}

	public static AppAccess deserialize(Map<String, Object> serialized)
	{
		AppAccess access = new AppAccess(//
				(String) serialized.get(PARAM_APP_NAME), //
				(String) serialized.get(PARAM_APP_KEY));

		List<EntityAccess> entityAccess = (List<EntityAccess>) serialized.get(PARAM_ENTITY_ACCESS);
		if (entityAccess == null)
			entityAccess = new ArrayList<>();
		access.entityAccess.addAll(entityAccess);

		List<String> contexts = (List<String>) serialized.get(PARAM_CONTEXTS);
		if (contexts == null)
			contexts = new ArrayList<>();
		for (String e : contexts)
			access.addAccess(Context.fromString(e));

		return access;
	}

	private void addAccess(Context context)
	{
		if (context == null)
			return;

		contexts.add(context);
	}

	@Override
	public Map<String, Object> serialize()
	{
		Map<String, Object> serialized = new HashMap<String, Object>();

		serialized.put(PARAM_APP_NAME, appName);
		serialized.put(PARAM_APP_KEY, appKey);

		List<EntityAccess> entityAccess = new ArrayList<>(this.entityAccess);
		serialized.put(PARAM_ENTITY_ACCESS, entityAccess);

		List<String> scope = new ArrayList<>();
		for (Context e : this.contexts)
			scope.add(e.name());
		serialized.put(PARAM_CONTEXTS, scope);

		return serialized;
	}

	@Override
	public int compareTo(AppAccess o)
	{
		return appName.compareTo(o.appName);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appKey == null) ? 0 : appKey.hashCode());
		result = prime * result + ((appName == null) ? 0 : appName.hashCode());
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
		AppAccess other = (AppAccess) obj;
		if (appKey == null)
		{
			if (other.appKey != null)
				return false;
		} else if (!appKey.equals(other.appKey))
			return false;
		if (appName == null)
		{
			if (other.appName != null)
				return false;
		} else if (!appName.equals(other.appName))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "AppAccess [appName=" + appName + ", appKey=*****]";
	}
}
