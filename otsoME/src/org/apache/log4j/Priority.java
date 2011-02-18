/*
 *  Copyright (c) 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Sun Microsystems, Inc. for Project JXTA."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Sun", "Sun Microsystems, Inc.", "JXTA" and "Project JXTA"
 *  must not be used to endorse or promote products derived from this
 *  software without prior written permission. For written
 *  permission, please contact Project JXTA at http://www.jxta.org.
 *
 *  5. Products derived from this software may not be called "JXTA",
 *  nor may "JXTA" appear in their name, without prior written
 *  permission of Sun.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL SUN MICROSYSTEMS OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of Project JXTA.  For more
 *  information on Project JXTA, please see
 *  <http://www.jxta.org/>.
 *
 *  This license is based on the BSD license adopted by the Apache Foundation.
 */
package org.apache.log4j;

public class Priority {
    private static final Priority Priority = new Priority();
    public static final int OFF_INT = Integer.MAX_VALUE;
    public static final int FATAL_INT = 50000;
    public static final int ERROR_INT = 40000;
    public static final int WARN_INT = 30000;
    public static final int INFO_INT = 20000;
    public static final int DEBUG_INT = 10000;
    public static final int TRACE_INT = 5000;
    public static final int ALL_INT = Integer.MIN_VALUE;
    public static final Priority OFF = Priority;
    public static final Priority FATAL = Priority;
    public static final Priority ERROR = Priority;
    public static final Priority WARN = Priority;
    public static final Priority INFO = Priority;
    public static final Priority DEBUG = Priority;
    public static final Priority TRACE = Priority;
    public static final Priority ALL = Priority;
    int lvl=0;
    String PriorityStr="stub";
    int syslogEquivalent=0;
    protected Priority() {}
    public static Priority toPriority(String sArg) {
        return Priority;
    }
    public static Priority toPriority(int val) {
        return Priority;
    }
    public final int getSyslogEquivalent() {
        return syslogEquivalent;
    }
    public boolean isGreaterOrEqual(Priority r) {
        return false;
    }
    public static Priority[] getAllPossiblePriorities() {
        return new Priority[] {
                   Priority.FATAL, Priority.ERROR, Priority.WARN, Priority.INFO, Priority.DEBUG,
                   Priority.TRACE
               };
    }
    public final String toString() {
        return PriorityStr;
    }
    public final int toInt() {
        return lvl;
    }
    public static Priority toPriority(int val, Priority defaultPriority) {
        switch (val) {
        case ALL_INT:
            return ALL;
        case TRACE_INT:
            return TRACE;
        case DEBUG_INT:
            return DEBUG;
        case INFO_INT:
            return INFO;
        case WARN_INT:
            return WARN;
        case ERROR_INT:
            return ERROR;
        case FATAL_INT:
            return FATAL;
        case OFF_INT:
            return OFF;
        default:
            return defaultPriority;
        }
    }
    public static Priority toPriority(String sArg, Priority defaultPriority) {
        if (sArg == null) {
            return defaultPriority;
        }
        String s = sArg.toUpperCase();
        if (s.equals("ALL")) {
            return ALL;
        }
        if (s.equals("TRACE")) {
            return TRACE;
        }
        if (s.equals("DEBUG")) {
            return DEBUG;
        }
        if (s.equals("INFO")) {
            return INFO;
        }
        if (s.equals("WARN")) {
            return WARN;
        }
        if (s.equals("ERROR")) {
            return ERROR;
        }
        if (s.equals("FATAL")) {
            return FATAL;
        }
        if (s.equals("OFF")) {
            return OFF;
        }
        return defaultPriority;
    }
}
