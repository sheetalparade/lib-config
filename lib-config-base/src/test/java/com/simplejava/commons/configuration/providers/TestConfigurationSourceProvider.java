package com.simplejava.commons.configuration.providers;

import java.util.Hashtable;

import com.simplejava.commons.configuration.exception.ConfigurationException;
import com.simplejava.commons.configuration.exception.KeyNotFoundException;
import com.simplejava.commons.configuration.interfaces.ConfigurationSourceData;
import com.simplejava.commons.configuration.interfaces.ConfigurationSourceProvider;

public class TestConfigurationSourceProvider implements ConfigurationSourceProvider
{
   
   public TestConfigurationSourceProvider()
   {
      configData = new ConfigurationSourceData()
      {
         public Hashtable<String, Object> h = new Hashtable<String, Object>();
         
         public void set(String key, Object value)
         {
            h.put(key, value);
         }
         
         public Hashtable<String, ?> getValues()
         {
            return h;
         }
         
         public int getPriority()
         {
            return 1;
         }
         
         public Object get(String key) throws KeyNotFoundException
         {
            return h.get(key);
         }
      };
   }
   
   public ConfigurationSourceData loadSource(Class<?> beanType) throws ConfigurationException
   {
      configData.set("example", "example");
      configData.set("integer", "2323");
      return configData;
   }
   
   private ConfigurationSourceData configData;

}
