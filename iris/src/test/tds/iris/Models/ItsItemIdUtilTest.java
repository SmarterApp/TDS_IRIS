package tds.iris.Models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ITSContent;
import tds.itemrenderer.data.ITSTypes;

import java.util.List;

import static org.junit.Assert.*;

public class ItsItemIdUtilTest {

    /**
     * Testing interface for abstract IITSDocument
     */
    public class TestDocument extends IITSDocument {
        @Override
        public List<String> getMediaFiles() {
            return null;
        }

        @Override
        public ITSContent getContent(String language) {
            return null;
        }

        @Override
        public ITSContent getContentDefault() {
            return null;
        }

        // Wrapper for accessing setType protected function.
        public void setItemType(ITSTypes.ITSEntityType val) {
            this.setType(val);
        }
    }

    @Test
    public void getItsDocumentIdTest1() {
        Assert.assertEquals("I-187-1645",ItsItemIdUtil.getItsDocumentId(187,1645, ITSTypes.ITSEntityType.Item));
        Assert.assertEquals("P-187-1645",ItsItemIdUtil.getItsDocumentId(187,1645, ITSTypes.ITSEntityType.Passage));
    }

    @Test
    public void getItsDocumentIdTest2() {
        TestDocument doc1 = new TestDocument();
        doc1.setBankKey(187);
        doc1.setItemKey((long) 1645);
        doc1.setItemType(ITSTypes.ITSEntityType.Item);

        TestDocument doc2 = new TestDocument();
        doc2.setBankKey(187);
        doc2.setItemKey((long) 1645);
        doc2.setItemType(ITSTypes.ITSEntityType.Passage);

        Assert.assertEquals("I-187-1645",ItsItemIdUtil.getItsDocumentId(doc1));
        Assert.assertEquals("P-187-1645",ItsItemIdUtil.getItsDocumentId(doc2));
    }

    @Test
    public void getItsDocumentIdTest3() {
        TestDocument doc1 = new TestDocument();
        doc1.setBankKey(187);
        doc1.setItemKey((long) 1645);
        doc1.setItemType(ITSTypes.ITSEntityType.Item);

        TestDocument doc2 = new TestDocument();
        doc2.setBankKey(187);
        doc2.setItemKey((long) 1645);
        doc2.setItemType(ITSTypes.ITSEntityType.Passage);

        Assert.assertEquals("I-187-1645-1A",ItsItemIdUtil.getItsDocumentId(doc1,"1A"));
        Assert.assertEquals("P-187-1645-1A",ItsItemIdUtil.getItsDocumentId(doc2,"1A"));
    }

    @Test
    public void translateItemIdTest() {
        Assert.assertEquals("Item-187-1645",ItsItemIdUtil.translateItemId("i-187-1645"));
        Assert.assertEquals("stim-187-1645",ItsItemIdUtil.translateItemId("p-187-1645"));

        Assert.assertEquals("Item-187-1645-1",ItsItemIdUtil.translateItemId("i-187-1645-1"));
        Assert.assertEquals("stim-187-1645-2b",ItsItemIdUtil.translateItemId("p-187-1645-2b"));

        Assert.assertEquals("Item-187-1645-1a",ItsItemIdUtil.translateItemId("i-187-1645-1A"));
        Assert.assertEquals("stim-187-1645-2b",ItsItemIdUtil.translateItemId("p-187-1645-2B"));
    }

    @Test
    public void validItemIdTest() {
        Assert.assertEquals(false,ItsItemIdUtil.validItemId(""));
        Assert.assertEquals(true,ItsItemIdUtil.validItemId("i-187-1645"));
        Assert.assertEquals(true,ItsItemIdUtil.validItemId("p-187-1645"));
        Assert.assertEquals(true,ItsItemIdUtil.validItemId("i-187-1645-1A"));
        Assert.assertEquals(true,ItsItemIdUtil.validItemId("p-187-1645-2"));
    }
}