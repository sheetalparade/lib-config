package com.simplejava.commons.configuration;

import junit.framework.Assert;

import org.junit.Test;

import com.simplejava.commons.configuration.ConfigurationBean;
import com.simplejava.commons.configuration.exception.ConfigurationException;

public class ConfigurationBeanTest
{

   @Test
   public void testGetBeanInstance() throws ConfigurationException
   {
      ConfigurationBean<TestingBean> config = new ConfigurationBean<TestingBean>();
      
      TestingBean test = new TestingBean();
      Assert.assertSame(config.getBean(test), test);
      Assert.assertEquals("example", test.getExample());
      Assert.assertEquals(new Integer(2323), test.getInteger());
   }
   
   @Test
   public void testGetBeanClassName() throws ConfigurationException
   {
      ConfigurationBean<TestingBean> config = new ConfigurationBean<TestingBean>();
      TestingBean bean = config.getBean(TestingBean.class.getName());
      Assert.assertNotNull(bean);
      Assert.assertEquals("example", bean.getExample());
      Assert.assertEquals(new Integer(2323), bean.getInteger());
   }
}
