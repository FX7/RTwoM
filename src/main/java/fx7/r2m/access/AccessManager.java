package fx7.r2m.access;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fx7.r2m.rest.RestException;

public class AccessManager
{
	private Map<String, AppAccess> appKeyToAppAccess;

	public AccessManager(List<AppAccess> access)
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

	public AppAccess getAccess(String appName, String appKey) throws RestException
	{
		if (appName == null || appName.isEmpty())
			throw RestException.noAppAccess();

		if (appKey == null || appKey.isEmpty())
			throw RestException.noAppAccess();

		AppAccess appAccess = appKeyToAppAccess.get(appKey);
		if (appAccess == null || !appAccess.getAppName().equals(appName))
			throw RestException.noAppAccess();

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

}
