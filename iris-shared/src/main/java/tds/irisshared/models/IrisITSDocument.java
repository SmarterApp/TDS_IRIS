package tds.irisshared.models;

import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ITSContent;
import tds.itemrenderer.data.ITSDocument;

import java.util.List;

/**
 * @author mskhan
 *
 */
public class IrisITSDocument extends ITSDocument
{

    private String _realPath;
    private String _revisionKey = "";



    private long _itemKey;
    private long _stimulusKey;




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

    public void setRealPath (String value) {
        this._realPath = value;
    }

    public void setRevisionKey(String s) { _revisionKey = s; }

    public void setItemKey(long itemKey) {
        this._itemKey = itemKey;
    }

    public void setStimulusKey(long stimulusKey) {
        this._stimulusKey = stimulusKey;
    }

    @Override
    public long getItemKey() {
        return _itemKey;
    }

    @Override
    public long getStimulusKey() {
        return _stimulusKey;
    }

}