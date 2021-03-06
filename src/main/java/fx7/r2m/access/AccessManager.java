package fx7.r2m.access;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class AccessManager
{
	private Map<String, AppAccess> appKeyToAppAccess;

	private static AccessManager accessManager;

	public static AccessManager getInstance()
	{
		return accessManager;
	}

	public static void initInstance(List<AppAccess> access)
	{
		AccessManager.accessManager = new AccessManager(access);
	}

	private AccessManager(List<AppAccess> access)
	{
		this.appKeyToAppAccess = initAccessMap(access);
	}

	private Map<String, AppAccess> initAccessMap(List<AppAccess> access)
	{
		Map<String, AppAccess> appKeyToAppAccess = new HashMap<>();

		if (access != null)
		{
			for (AppAccess aa : access)
				appKeyToAppAccess.put(aa.getAppKey(), aa);
		}

		return appKeyToAppAccess;
	}

	public AppAccess getAccess(String appName, String appKey)
	{
		if (appName == null || appName.isEmpty())
			return null;

		if (appKey == null || appKey.isEmpty())
			return null;

		AppAccess appAccess = appKeyToAppAccess.get(appKey);
		if (appAccess == null || !appAccess.getAppName().equals(appName))
			return null;

		return appAccess;
	}

	public List<AppAccess> getAppAccesss()
	{
		Collection<AppAccess> appAccesss = appKeyToAppAccess.values();
		List<AppAccess> access = new ArrayList<>();
		if (appAccesss != null)
			access.addAll(appAccesss);

		Collections.sort(access);
		return access;
	}

	public boolean hasAccess(Set<EntityAccess> accesses, Context context, String entityName)
	{
		if (accesses == null)
			return false;

		if (entityName == null || entityName.isEmpty())
			return false;

		Optional<EntityAccess> access = accesses.stream()
				.filter(e -> e.getContext() == context && e.getEntityName().equals(entityName)).findFirst();

		return access.isPresent();
	}
}
