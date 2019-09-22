package kaitech.io;

import kaitech.parsing.KaiTechErrorHandler;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import static org.junit.Assert.assertEquals;

public class KaiTechErrorHandlerTest {

    private KaiTechErrorHandler kteh;

    @Before
    public void init(){
        this.kteh = new KaiTechErrorHandler(System.err);
    }

    @Test
    public void testWarning(){
        try {
            this.kteh.warning(new SAXParseException("Warning Error", null));
            //assertEquals("Checking the warning field works", System.err., "SAX warning: Warning Error");
        }catch (SAXException e){
            //shouldnt happen
        }
    }

}

