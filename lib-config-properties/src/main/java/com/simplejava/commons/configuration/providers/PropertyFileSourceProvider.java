package com.simplejava.commons.configuration.providers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.simplejava.commons.configuration.exception.ConfigurationException;
import com.simplejava.commons.configuration.interfaces.ConfigurationSourceData;
import com.simplejava.commons.configuration.interfaces.ConfigurationSourceProvider;
import com.simplejava.commons.configuration.source.PropertiesConfigurationSourceData;


public class PropertyFileSourceProvider implements ConfigurationSourceProvider
{
   private Logger logger = Logger.getLogger(PropertyFileSourceProvider.class);
   
   public ConfigurationSourceData loadSource(Class<?> beanType) throws ConfigurationException
   {
      String fileName = "/"+beanType.getName().toLowerCase().replaceAll("\\.", "/")+".properties";
      InputStream is = beanType.getResourceAsStream(fileName);
      Properties prop = new Properties();
      try
      {
         prop.load(is);
      }
      catch (IOException e)
      {
         throw new ConfigurationException("Unable to load property file "+fileName, e);
      }
      finally
      {
         try
         {
            is.close();
         }
         catch (Exception e2)
         {
            logger.error("unanble to close inputstream for resource "+fileName, e2);
         }
      }
      String overrideLocation = getOverrideLocation();
      if(overrideLocation != null)
      {
         String finalOverrideLocation = overrideLocation+(fileName.startsWith("/")?"":"/")+fileName;
         Properties overrides = new Properties();
         InputStream overrideIs = null;
         try
         {
            overrideIs = new FileInputStream(finalOverrideLocation);
            overrides.load(overrideIs);
            for(Entry<Object, Object> entry : overrides.entrySet())
            {
               prop.put(entry.getKey(), entry.getValue());
            }
         }
         catch (Exception e)
         {
            logger.error("Unable to load overrides from file "+finalOverrideLocation, e);
         }
         finally
         {
            try
            {
               overrideIs.close();
            }
            catch (Exception e2)
            {
               logger.error("Unable to close resource "+finalOverrideLocation, e2);
            }
         }
      }
      
      PropertiesConfigurationSourceData configurationData = new PropertiesConfigurationSourceData();
      configurationData.setProperties(prop);
      return configurationData;
   }
   
   protected String getOverrideLocation()
   {
      Properties overrideMetaData = new Properties();
      String fileName = "/"+this.getClass().getName().toLowerCase().replaceAll("\\.", "/")+".properties";
      InputStream is = PropertyFileSourceProvider.class.getResourceAsStream(fileName);
      try
      {
         overrideMetaData.load(is);
      }
      catch (Exception e)
      {
         logger.error("Unable to load overrides meta data from source "+fileName, e);
      }
      finally
      {
         try
         {
            is.close();
         }
         catch (Exception e2)
         {
            logger.error("Unable to load input stream for resource "+fileName, e2);
         }
      }
      return overrideMetaData.getProperty("overridelocation");
   }
   
}
