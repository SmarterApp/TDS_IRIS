package tds.iris.Models;

import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ITSContent;

import java.util.List;

/**
 * @author mskhan
 *
 */
public class IrisITSDocument extends IITSDocument
{

    private String _realPath;
    private String _revisionKey = "";

    public IrisITSDocument (IITSDocument itsDocument, String realPath) {
        this.setItemKey (itsDocument.getItemKey ());
        this.setBankKey (itsDocument.getBankKey ());
        this.setBaseUri (itsDocument.getBaseUri ());
        this.setFormat (itsDocument.getFormat ());
        this.setType (itsDocument.getType ());
        this.setStimulusKey (itsDocument.getStimulusKey ());
        this.setRealPath (realPath);
    }

    public String getRealPath () {
        return _realPath;
    }

    public String getRevisionKey() { return _revisionKey ; }

    @Override
    public ITSContent getContent (String language) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ITSContent getContentDefault () {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getMediaFiles () {
        // TODO Auto-generated method stub
        return null;
    }

    private void setRealPath (String value) {
        this._realPath = value;
    }

    public void setRevisionKey(String s) { _revisionKey = s; }
}