package com.simplejava.commons.configuration.exception;

public class KeyNotFoundException extends Exception
{

   private static final long serialVersionUID = 1L;

   public KeyNotFoundException()
   {
   }

   public KeyNotFoundException(String message)
   {
      super(message);
   }

   public KeyNotFoundException(Throwable cause)
   {
      super(cause);
   }

   public KeyNotFoundException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public KeyNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
   {
      super(message, cause, enableSuppression, writableStackTrace);
   }

}
