package com.simplejava.commons.configuration.providers;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.simplejava.commons.configuration.beans.TestBean;
import com.simplejava.commons.configuration.exception.ConfigurationException;
import com.simplejava.commons.configuration.exception.KeyNotFoundException;
import com.simplejava.commons.configuration.interfaces.ConfigurationSourceData;
import com.simplejava.commons.configuration.providers.PropertyFileSourceProvider;

public class TestPropertyFileProvider
{
   
   @Before
   public void init() throws Exception
   {
   }
   
   @Test
   public void testLoadSource() throws ConfigurationException, KeyNotFoundException
   {
      Class<?> c = TestBean.class;
      ConfigurationSourceData sourceData = new PropertyFileSourceProvider().loadSource(c);
      String value = (String)sourceData.get("testing");
      Assert.assertNotNull(value);
   }
   
   @Test
   public void testGetOverrideLocation()
   {
      PropertyFileSourceProvider provider = new PropertyFileSourceProvider();
      String filename = provider.getOverrideLocation();
      Assert.assertEquals("/tmp", filename);
   }
}
