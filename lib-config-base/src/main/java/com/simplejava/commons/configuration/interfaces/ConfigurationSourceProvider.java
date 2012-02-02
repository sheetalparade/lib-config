package com.simplejava.commons.configuration.interfaces;

import com.simplejava.commons.configuration.exception.ConfigurationException;

/**
 * Service to load ConfigurationSource.
 * ServiceLocator will identify all the Source Provider,
 * Combine them and then use the sources to load the beans.
 */
public interface ConfigurationSourceProvider
{
   public ConfigurationSourceData loadSource(Class<?> beanType) throws ConfigurationException;
}
