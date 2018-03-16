package tds.iris.content;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConfigBuilderTest {
    final String CONTENT_PATH = "/home/tomcat7/content/";
    ConfigBuilder documentLookup = null;

    @Before
    public void setUp() throws Exception {
        documentLookup = new ConfigBuilder(CONTENT_PATH);
    }

    @Test
    public void getContentPathSubDirTest() {
        Assert.assertEquals("Items/",documentLookup.getContentPathSubDir("i-123-1234-1A"));
        Assert.assertEquals("Stimuli/",documentLookup.getContentPathSubDir("p-123-1234-1A"));
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