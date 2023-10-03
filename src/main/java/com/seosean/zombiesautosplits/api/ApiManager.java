package com.seosean.zombiesautosplits.api;

import java.util.HashMap;
import java.util.Map;

public class ApiManager {
    private static final ApiManager INSTANCE = new ApiManager();
    private Map<Class<?>, Object> apiInstances = new HashMap<>();

    private ApiManager() {
    }

    public static ApiManager getInstance() {
        return INSTANCE;
    }

    public void registerApiInstance(Class<?> apiClass, Object apiInstance) {
        apiInstances.put(apiClass, apiInstance);
    }

    public <T> T getApiInstance(Class<T> apiClass) {
        Object apiInstance = apiInstances.get(apiClass);
        if (apiInstance != null && apiClass.isInstance(apiInstance)) {
            return apiClass.cast(apiInstance);
        }
        return null;
    }
}