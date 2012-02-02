package com.simplejava.commons.configuration;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.simplejava.commons.configuration.exception.ConfigurationException;
import com.simplejava.commons.configuration.exception.KeyNotFoundException;
import com.simplejava.commons.configuration.interfaces.ConfigurationSourceData;
import com.simplejava.commons.configuration.source.ConfigurationSourceFactory;

/**
 * Main class used to configure beans from the property file.
 * 
 * Default properties if not provided is the same directory as the package with filename same as the class
 * 
 * This class can also be used as a factory class to create instances populated from properties.
 *
 */
public class ConfigurationBean<T>
{
   /**
    * Default Constructor
    */
   public ConfigurationBean()
   {
   }
   
   /**
    * Instance of the bean is created and then populated with values from properties at default location.
    * 
    * @param className Class name of the instance to create.
    * @return
    * @throws ConfigurationException
    */
   public T getBean(String className) throws ConfigurationException
   {
      return getBean(createInstance(className));
   }
   
   
   /**
    * the instance is populated with values from properties file.
    * 
    * @param instance
    * @return
    * @throws ConfigurationException
    */
   public T getBean(T instance) throws ConfigurationException
   {
      if(instance == null)
      {
         throw new ConfigurationException("The given instance is null");
      }
      
      return populate(instance, ConfigurationSourceFactory.getInstance().getSourceData(instance.getClass()));
   }
   
   
   /**
    * Create the instance of Bean using default no argument constructor.
    * @param className
    * @return
    * @throws ConfigurationException
    */
   @SuppressWarnings({ "unchecked", "rawtypes" })
   private T createInstance(String className) throws ConfigurationException
   {
      try
      {
         Class c = Class.forName(className);
         return (T)c.newInstance();
      }
      catch (Exception e)
      {
         throw new ConfigurationException("Error creating bean "+className, e);
      }
   }
   
   
   private T populate(T data, ConfigurationSourceData source) throws ConfigurationException
   {
      if(source == null)
      {
         return data;
      }
      BeanInfo beanInfo = null;
      try
      {
         beanInfo = Introspector.getBeanInfo(data.getClass());
         for (PropertyDescriptor desc : beanInfo.getPropertyDescriptors())
         {
            String name = desc.getName();
            Object value;
            try
            {
               value = source.get(name);
            }
            catch (KeyNotFoundException e1)
            {
               continue;
            }
            Method writer = desc.getWriteMethod();
            if(writer != null)
            {
               writer.setAccessible(true);
               Class<?>[] parameters = writer.getParameterTypes();
               if(parameters != null && parameters.length>0)
               {
                  Class<?> parameterClass = parameters[0];
                  Object convertedValue = null;
                  if(value.getClass().isAssignableFrom(parameterClass))
                  {
                     convertedValue = value;
                  }
                  else
                  {
                     convertedValue = convertValueType(parameterClass, value);
                  }
                  try
                  {
                     writer.invoke(data, convertedValue);
                  }
                  catch (Exception e)
                  {
                     logger.error("Unable to invoke writer", e);
                  }
               }
            }
         }
      }
      catch (IntrospectionException e)
      {
         throw new ConfigurationException("Unable to get bean info", e);
      }
      
      return data;
   }
   
   private Object convertValueType(Class<?> type, Object value) throws ConfigurationException
   {
      if (value == null)
         return null;
      String valueAsString = value.toString();
      try
      {
         if (type.isArray())
         {
            Class<?> componentType = type.getComponentType();
            List<Object> list = new ArrayList<Object>();
            String[] values = valueAsString.split(",");
            for (String val : values)
            {
               list.add(convertValueType(componentType, val.trim()));
            }
            Object array = Array.newInstance(componentType, list.size());
            for (int i = 0; i < list.size(); i++)
            {
               Array.set(array, i, list.get(i));
            }
            return array;
         }
         else if (String.class == type)
         {
            return valueAsString;
         }
         else if (boolean.class == type || Boolean.class == type)
         {
            return Boolean.parseBoolean(valueAsString);
         }
         else if (long.class == type || Long.class == type)
         {
            return Long.parseLong(valueAsString);
         }
         else if (int.class == type || Integer.class == type)
         {
            return Integer.parseInt(valueAsString);
         }
         else if (short.class == type || Short.class == type)
         {
            return Short.parseShort(valueAsString);
         }
         else if (byte.class == type || Byte.class == type)
         {
            return Byte.parseByte(valueAsString);
         }
         else if (char.class == type || Character.class == type)
         {
            return valueAsString.charAt(0);
         }
         else if (float.class == type || Float.class == type)
         {
            return Float.parseFloat(valueAsString);
         }
         else if (double.class == type || Double.class == type)
         {
            return Double.parseDouble(valueAsString);
         }
         else
         {
            Constructor<?>[] constructors = type.getConstructors();
            for (Constructor<?> c : constructors)
            {
               Class<?>[] types = c.getParameterTypes();
               if (types.length == 1 && String.class.equals(types[0]))
               {
                  return c.newInstance(valueAsString);
               }
            }
            throw new IllegalArgumentException("No valid constructors for type: " + type);
         }
      }
      catch (Throwable t)
      {
         throw new ConfigurationException("Could not convert value for type: " + type, t);
      }
   }
   
   
   private Logger logger = Logger.getLogger(ConfigurationBean.class);
}
