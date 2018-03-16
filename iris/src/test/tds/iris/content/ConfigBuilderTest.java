package tds.iris.content;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigBuilderTest {
    final static String CONTENT_PATH = "";
    ConfigBuilder documentLookup = null;

    @Before
    public void setUp() throws Exception {
        documentLookup = new ConfigBuilder(CONTENT_PATH);
    }

    @Test
    public void getContentPathSubDirTest() {
        Assert.assertEquals(documentLookup.getContentPathSubDir("i-123-1234-1A"),"Items/");
        Assert.assertEquals(documentLookup.getContentPathSubDir("p-123-1234-1A"),"Stimuli/");
    }

    @Test
    public void getDocumentRepresentationTest() {
    }

    @Test
    public void addFileTest() {
    }

    @Test
    public void removeFileTest() {
    }

    @Test
    public void getRenderableDocumentTest() {
    }

    @Test
    public void getRenderableDocument1Test() {
    }
}