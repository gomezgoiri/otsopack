/*
 *  Copyright 1999,2004 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.log4j;

public class Logger extends Category 
{
    private final static Logger logger = new Logger();
 
    protected Logger() 
    { 
    	
    }
 
    public void fatal(String messagePattern, Object arg1, Object arg2) 
    {
    }
    
    public static Logger getInstance(String name) {
        return logger;
    }
    public static Logger getInstance(Class clazz) {
        return logger;
    }
    public static Logger getRootLogger() {
        return logger;
    }
    public static void setLogServerAddress(String address)
    {
    	logger.logServerAddress = address;
       	logger.socketLogEnabled = (address != null);    		
    }
    
    public void trace(Object message) {
    }
    public void trace(Object message, Throwable t) {
    }
    public void trace(Object messagePattern, Object arg) {
    }
    public void trace(String messagePattern, Object arg1, Object arg2) {
    }
}
