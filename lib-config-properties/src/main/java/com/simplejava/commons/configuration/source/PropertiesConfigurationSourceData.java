package com.simplejava.commons.configuration.source;

import java.util.Hashtable;
import java.util.Properties;

import com.simplejava.commons.configuration.exception.KeyNotFoundException;
import com.simplejava.commons.configuration.interfaces.ConfigurationSourceData;

public class PropertiesConfigurationSourceData implements ConfigurationSourceData
{
   private Hashtable<String, String> data = new Hashtable<String, String>();

   public Hashtable<String, ?> getValues()
   {
      return data;
   }

   public void set(String key, Object value)
   {
      data.put(key, value.toString());

   }

   public Object get(String key) throws KeyNotFoundException
   {
      String value = data.get(key);
      if(value == null)
      {
         throw new KeyNotFoundException("Source does not contain key "+key);
      }
      return value;
   }

   public int getPriority()
   {
      return 10;
   }
   
   public void setProperties(Properties props)
   {
      if(props != null)
      {
         for(Object key: props.keySet())
         {
            data.put(key.toString(), props.getProperty(key.toString()));
         }
      }
   }

}
