package com.simplejava.commons.configuration.interfaces;

import java.util.Hashtable;

import com.simplejava.commons.configuration.exception.KeyNotFoundException;

/**
 * Source for configuration utility to get data to load the beans.
 */
public interface ConfigurationSourceData
{
   public Hashtable<String, ?> getValues();
   
   public void set(String key, Object value);
   
   public Object get(String key) throws KeyNotFoundException;
   
   public int getPriority();
}
