package tds.iris.Models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ITSTypes;

import static org.junit.Assert.*;

public class ItsItemIdUtilTest {

    @Test
    public void getItsDocumentIdTest() {
        Assert.assertEquals("I-187-1645",ItsItemIdUtil.getItsDocumentId(187,1645, ITSTypes.ITSEntityType.Item));
        Assert.assertEquals("P-187-1645",ItsItemIdUtil.getItsDocumentId(187,1645, ITSTypes.ITSEntityType.Passage));
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