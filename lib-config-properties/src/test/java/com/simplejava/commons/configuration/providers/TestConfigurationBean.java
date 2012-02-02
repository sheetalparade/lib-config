package com.simplejava.commons.configuration.providers;

import org.junit.Test;

import junit.framework.Assert;

import com.simplejava.commons.configuration.ConfigurationBean;
import com.simplejava.commons.configuration.beans.TestBean;
import com.simplejava.commons.configuration.exception.ConfigurationException;

public class TestConfigurationBean
{
   
   @Test
   public void testGetBean() throws ConfigurationException
   {
      ConfigurationBean<TestBean> configurationBean = new ConfigurationBean<TestBean>();
      TestBean bean = configurationBean.getBean(TestBean.class.getName());
      Assert.assertNotNull(bean);
      Assert.assertEquals("This is a test", bean.getTesting());
      Assert.assertNull(bean.getB());
   }
}
