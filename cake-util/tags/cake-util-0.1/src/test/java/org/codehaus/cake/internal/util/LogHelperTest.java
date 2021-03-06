/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.internal.util;

import static org.junit.Assert.assertEquals;

import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.codehaus.cake.util.Loggers;
import org.codehaus.cake.util.Logger.Level;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LogHelperTest {
    static final ResourceBundle bundle = ResourceBundle
            .getBundle("org.codehaus.cake.internal.util.loghelper");

    Document doc;

    Element e;

    @Test
    public void getLogLevel() {
        assertEquals(Level.Trace, LogHelper.getLogLevel(Loggers.systemErrLogger(Level.Trace)));
        assertEquals(Level.Debug, LogHelper.getLogLevel(Loggers.systemErrLogger(Level.Debug)));
        assertEquals(Level.Info, LogHelper.getLogLevel(Loggers.systemErrLogger(Level.Info)));
        assertEquals(Level.Warn, LogHelper.getLogLevel(Loggers.systemErrLogger(Level.Warn)));
        assertEquals(Level.Error, LogHelper.getLogLevel(Loggers.systemErrLogger(Level.Error)));
        assertEquals(Level.Fatal, LogHelper.getLogLevel(Loggers.systemErrLogger(Level.Fatal)));
        assertEquals(Level.Off, LogHelper.getLogLevel(Loggers.systemErrLogger(Level.Off)));
    }

    @Before
    public void setup() throws ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        doc = builder.newDocument();
        e = doc.createElement("element");
        doc.appendChild(e);
    }

    @Test
    public void toLevel() {
        e.setAttribute("level", Level.Debug.toString());
        assertEquals(Level.Debug, LogHelper.toLevel(e));
    }

    @Test(expected = IllegalArgumentException.class)
    public void toLevelIAE() {
        e.setAttribute("level", "unknownlevel");
        assertEquals(Level.Debug, LogHelper.toLevel(e));
    }
}
