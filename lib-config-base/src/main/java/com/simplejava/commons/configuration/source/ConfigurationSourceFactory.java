package com.simplejava.commons.configuration.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicBoolean;

import com.simplejava.commons.configuration.exception.ConfigurationException;
import com.simplejava.commons.configuration.interfaces.ConfigurationSourceData;
import com.simplejava.commons.configuration.interfaces.ConfigurationSourceProvider;

/**
 * Loads all the Source Data from various services.
 * Singleton implementation 
 */
public class ConfigurationSourceFactory
{
   private static ConfigurationSourceFactory instance = null;
   private static Object mutex = new Object();
   private ConfigurationSourceData sourceData = null; 
   private AtomicBoolean initilized = new AtomicBoolean(false);
   /**
    * Default private constructor
    */
   private ConfigurationSourceFactory()
   {
   }
   
   /**
    * static instance creator for singleton implementation
    * @return
    */
   public static ConfigurationSourceFactory getInstance()
   {
      if(instance == null)
      {
         synchronized(mutex)
         {
            if(instance == null)
            {
               instance = new ConfigurationSourceFactory();
            }
         }
      }
      return instance;
   }
   
   /**
    * Returns the cached ConfigurationSourceData.
    * If not initialized will load the data before returning
    * 
    * @return
    * @throws ConfigurationException
    */
   public ConfigurationSourceData getSourceData(Class<?> beanType) throws ConfigurationException
   {
      if(!initilized.get())
      {
         synchronized (mutex)
         {
            if(!initilized.get())
            {
               this.sourceData =combineSourceData(loadSources(beanType)); 
               this.initilized.set(true);
            }
         }
      }
      return sourceData;
   }

   /**
    * Refresh the source data.
    * Subsequent call to get source data will load it back from all the sources.
    */
   public void refreshSourceData()
   {
      this.initilized.set(false);
   }
   /**
    * Using ServiceLoader find all the available services and order the ConfigurationSourceData based of priority
    * 
    * @return List of all available ConfigurationSourceData ordered by priority
    * @throws ConfigurationException
    */
   private List<ConfigurationSourceData> loadSources(Class<?> beanType) throws ConfigurationException
   {
      List<ConfigurationSourceData> sources = new ArrayList<ConfigurationSourceData>();
      
      ServiceLoader<ConfigurationSourceProvider> services = ServiceLoader.load(ConfigurationSourceProvider.class);
      for (ConfigurationSourceProvider provider : services)
      {
         sources.add(provider.loadSource(beanType));
      }
      Collections.sort(sources, new Comparator<ConfigurationSourceData>()
      {
         public int compare(ConfigurationSourceData one, ConfigurationSourceData two)
         {
            if(one.getPriority()>two.getPriority())
            {
               return -1;
            }
            else if(one.getPriority()<two.getPriority())
            {
               return 1;
            }
            return 0;
         }
         
         public boolean equals(Object obj){
            return false;
         }
      });
      return sources;
   }
   
   /**
    * 
    * @param List of ConfigurationSourceData
    * @return since combined ConfigurationSourceData
    */
   private ConfigurationSourceData combineSourceData(List<ConfigurationSourceData> sources)
   {
      if(sources == null || sources.size()==0)
      {
         return null;
      }
      if(sources.size()==1)
      {
         return sources.get(0);
      }
      ConfigurationSourceData source = sources.get(0);
      for (ConfigurationSourceData src : sources)
      {
         if(src == source)
         {
            continue;
         }
         Hashtable<String, ?> values = src.getValues();
         for (String key : values.keySet())
         {
            source.set(key, values.get(key));
         }
      }
      return source;
   }
}
