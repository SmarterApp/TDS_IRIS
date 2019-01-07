/*******************************************************************************
 * Educational Online Test Delivery System Copyright (c) 2014 American
 * Institutes for Research
 *
 * Distributed under the AIR Open Source License, Version 1.0 See accompanying
 * file AIR-License-1_0.txt or at http://www.smarterapp.org/documents/
 * American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
/**
 *
 */
package tds.irisshared.content;

import AIR.Common.Helpers.CaseInsensitiveMap;
import AIR.Common.Utilities.Path;
import AIR.Common.Utilities.SpringApplicationContext;
import AIR.Common.Web.Session.Server;
import AIR.Common.collections.IGrouping;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tds.blackbox.ContentRequestException;
import tds.irisshared.models.ItsItemIdUtil;
import tds.itempreview.*;
import tds.itemrenderer.ITSDocumentFactory;
import tds.itemrenderer.data.AccLookup;
import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ITSDocument;
import tds.itemrenderer.data.ITSTypes.ITSEntityType;
import tds.irisshared.models.IrisITSDocument;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Shiva BEHERA [sbehera@air.org]
 *
 */
// / <summary>
// / Builds a json config based on file path.
// / </summary>
public class ConfigBuilder
{
    private static final Logger          _logger = LoggerFactory.getLogger (ConfigBuilder.class);
    private URI                          _docBaseUri;
    private final String                 _contentPath;
    private String                       _filterFormat;
    private String                       _filterResponseType;
    private Map<String, IrisITSDocument> _documentLookup;
    private Exception                    _error;
    private Config                       _config;

    private IContentLocation _contentLocation;

    public String getFilterFormat() {
        return _filterFormat;
    }

    public void setFilterFormat(String value) {
        _filterFormat = value;
    }

    public String getFilterResponseType() {
        return _filterResponseType;
    }

    public void setFilterResponseType(String value) {
        _filterResponseType = value;
    }

    public Config getConfig() {
        return _config;
    }

    public ConfigBuilder(String contentPath) throws URISyntaxException {
        _contentLocation = SpringApplicationContext.getBean("iContentLocation", IContentLocation.class);

        _contentPath = contentPath;
        _docBaseUri = new URI ("file:///" + StringUtils.replace (Server.getDocBasePath(), "\\", "/"));

        try {
            List<ITSGroups> itsGroupsList = new ArrayList<ITSGroups>();
            _config = buildConfigPages(itsGroupsList);
            _documentLookup = new HashMap<String, IrisITSDocument>();

        } catch (Exception exp) {
            _logger.error ("Error loading IRiS content.", exp);
            _error = exp;
            throw exp;
        }
    }


    public IrisITSDocument getDocumentRepresentation(String id) throws ContentRequestException {
        if (_error != null) {
            throw new ContentRequestException ("Content not loaded properly.", _error);
        }
        if (_documentLookup.containsKey(id)) {
            return _documentLookup.get(id);
        }
        else {
            String path = _contentPath + ItsItemIdUtil.translateItemId(id) + "/";
            reloadContent(getITSDocuments(path));
            return _documentLookup.get(id.toLowerCase());
        }
    }

    public Config addFile(String filePath) {
        // refactor to only grab specfic items inside directory

        Collection<IrisITSDocument> itsDocuments = _documentLookup.values();
        Collection<IrisITSDocument> newDocs = getITSDocuments(filePath);
        newDocs.addAll(itsDocuments);

        return reloadContent(newDocs);
    }

    // throws requires this method is wrapped in a try catch.
    public Config removeFile(String fileName) throws Exception {
        Collection<IrisITSDocument> itsDocuments = _documentLookup.values();
        String key = getRemoveKey(fileName);
        IrisITSDocument documentRemove = _documentLookup.get(key);

        Collection<IrisITSDocument> newDocs = new ArrayList<>();
        newDocs.addAll(itsDocuments);
        newDocs.remove(documentRemove);

        return reloadContent(newDocs);
    }

    public IITSDocument getRenderableDocument (String id) throws ContentRequestException {
        IrisITSDocument documentRepresentation = getDocumentRepresentation (id);
        // In the below code there is no way to set accommodations.
        // We need to provide a way as this is common code also used by ItemPreview.
        return correctBaseUri (ITSDocumentFactory.load (documentRepresentation.getRealPath (), "ENU", true));
    }

    public IITSDocument getRenderableDocument(String id, AccLookup accLookup) throws ContentRequestException {
        IrisITSDocument documentRepresentation = getDocumentRepresentation(id);

        return correctBaseUri(ITSDocumentFactory.load(documentRepresentation.getRealPath(), accLookup, true));
    }

    private Config reloadContent(Collection<IrisITSDocument> itsDocuments) {
        try {
            List<ITSGroups> itsGroupsList = ItsGroupsList(itsDocuments);

            for (Map.Entry<String, IrisITSDocument> doc : IrisItsDocumentmap(itsDocuments).entrySet()) {
                 _documentLookup.put(doc.getKey(),doc.getValue());
            }
            _config = buildConfigPages(itsGroupsList);

            return _config;
        } catch (Exception var8) {
            _logger.error("Error loading IRiS content.", var8);
            _error = var8;
            throw var8;
        }
    }

    private List<ITSGroups> ItsGroupsList(Collection<IrisITSDocument> itsDocuments){
        if(itsDocuments != null) {
            itsDocuments = Collections.unmodifiableCollection(itsDocuments);
        }

        Collection<IGrouping<String, IrisITSDocument>> groupDocumentsByFolders = this.groupDocumentsByParentFolder(itsDocuments);
        List<ITSGroups> itsGroupsList = new ArrayList();

        for (IGrouping<String, IrisITSDocument> i : groupDocumentsByFolders) {
            ITSGroups groups = createITSGroups(i);
            itsGroupsList.add(groups);
        }

        return itsGroupsList;
    }

    private Map<String, IrisITSDocument> IrisItsDocumentmap(Collection<IrisITSDocument> itsDocuments)
    {
        Map<String, IrisITSDocument> documentsMap = new CaseInsensitiveMap();

        for(IrisITSDocument doc : itsDocuments) {
            String id;

            if (doc.getRevisionKey() != "") {
                id = String.format("%s-%s", ItsItemIdUtil.getItsDocumentId(doc), doc.getRevisionKey());
            } else {
                id = String.format("%s", ItsItemIdUtil.getItsDocumentId(doc));
            }
            documentsMap.put(id, doc);
        }
        return documentsMap;
    }

    private boolean validFileName(String file) {
        return file.toLowerCase().matches("(item|stim)-[0-9]+-[0-9]+(-[0-9a-zA-Z]+)?");
    }

    //parse the file name in order to remove the correct file
    private String getRemoveKey(String fileName) throws Exception {
        final String FILE_NAME_FORMAT = "%s-%s-%s";


        String[] parts = fileName.split("-");
        boolean validFile = validFileName(fileName);

        if(!(parts.length >= 3) || !validFile){
            throw new Exception("invalid key");
        }

        String prefix = "";
        if(fileName.contains("Item")) {
            prefix = "i";
        } else if(fileName.contains("stim")) {
            prefix = "p";
        }

        // account for revision key.
        if (parts.length == 4) {
            return String.format("%s-%s-%s-%s", prefix,  parts[1],  parts[2],parts[3]);
        }

        return String.format(FILE_NAME_FORMAT, prefix,  parts[1],  parts[2]);
    }

    // / <summary>
    // / Takes an array of xml files and parses into passage/item documents.
    // / </summary>
    private Collection<IrisITSDocument> getITSDocuments(String contentPath) {
        // get all xml files in the content path
        Collection<File> xmlFiles = Path.getFilesMatchingExtensions(contentPath, new String[]{ "xml" });

        return _generateITSDocuments(xmlFiles);
    }

    private Collection<IrisITSDocument> _generateITSDocuments(Collection<File> xmlFiles) {
        Collection<IrisITSDocument> returnList = new ArrayList<IrisITSDocument>();

        for(File file : xmlFiles) {
            String xmlFile = file.getAbsolutePath();
            String revKey = "";
             for (String f : xmlFile.split(String.valueOf(File.separatorChar == 92  ? "\\\\" : File.separatorChar ))) {
                 // format itemType-bankKey-ItemKey-RevisionKey
                 if (validFileName(f) && f.split("-").length == 4) {
                     revKey = f.split("-")[3];
                     break;
                 }
             }

            try {
                IITSDocument itsDocument = correctBaseUri(ITSDocumentFactory.loadUri2 (xmlFile, AccLookup.getNone(), false));
                IrisITSDocument irisDocument = new IrisITSDocument(itsDocument, xmlFile);
                irisDocument.setRevisionKey(revKey);
                returnList.add(irisDocument);
            } catch(Exception exp) {
                exp.printStackTrace();
            }
        }

        return returnList;
    }

    private Collection<IGrouping<String, IrisITSDocument>> groupDocumentsByParentFolder (Collection<IrisITSDocument> itsDocuments) {
        Transformer groupTransformer = new Transformer()
        {
            @Override
            public Object transform(Object itsDocument) {
                return((IITSDocument) itsDocument).getFolderName();
            }
        };

        List<IGrouping<String, IrisITSDocument>> returnList = IGrouping.<String, IrisITSDocument> createGroups (itsDocuments, groupTransformer);

        Collections.sort(returnList, new Comparator<IGrouping<String, IrisITSDocument>>()
        {
            @Override
            public int compare(IGrouping<String, IrisITSDocument> o1, IGrouping<String, IrisITSDocument> o2) {
                return o1.getKey ().compareTo (o2.getKey());
            }
        });

        return returnList;
    }

    private ITSGroups createITSGroups (Collection<IrisITSDocument> itsDocuments) {
        boolean ignorePassages = ItemPreviewSettings.getIgnorePassages().getValue();

        List<IrisITSDocument> itsPassages = new ArrayList<IrisITSDocument> ();
        List<IrisITSDocument> itsItems = new ArrayList<IrisITSDocument> ();

        // split up passages and items
        for(IrisITSDocument itsDocument : itsDocuments) {
            if(itsDocument.getType() == ITSEntityType.Passage && ignorePassages) {
                itsPassages.add (itsDocument);
            } else if (itsDocument.getType () == ITSEntityType.Item) {
                itsItems.add (itsDocument);
            }
        }

        Map<String, ITSGroup> groupLookup = new HashMap<String, ITSGroup> ();

        // create groups out of the passages
        for(ITSDocument itsPassage : itsPassages) {
            String groupID = itsPassage.getGroupID ();
            if (groupLookup.containsKey(groupID)) {
                continue;
            }

            ITSGroup itsGroup = new ITSGroup(groupID);
            itsGroup.setPassage (itsPassage);
            groupLookup.put (groupID, itsGroup);
        }

        for(ITSDocument item : itsItems) {
            String itemGroupID = item.getGroupID ();

            if(ignorePassages && item.getStimulusKey () > 0) {
                itemGroupID = item.getID ();
            }

            ITSGroup group = null;
            if(groupLookup.containsKey (itemGroupID)) {
                group = groupLookup.get(itemGroupID);
            }

            if(group == null) {
                group = new ITSGroup(itemGroupID);
                groupLookup.put (itemGroupID, group);
            }

            if(group.getItems() == null) {
                group.setItems(new ArrayList<IITSDocument>());
            }
            group.getItems().add(item);
        }

        ITSGroups itsGroups = new ITSGroups();
        for(ITSGroup itsGroup : groupLookup.values()) {
            // if there is a filter ignore empty groups
            // TODO: Refactor
            if (getFilterFormat() != null || getFilterResponseType() != null) {
                if (itsGroup.getItems() == null || itsGroup.getItems().size() == 0) {
                    continue;
                }

                itsGroups.add(itsGroup);
            }
        }
        return itsGroups;
    }

    private Config buildConfigPages(Collection<ITSGroups> itsGroupsList) {
        boolean ignorePassages = ItemPreviewSettings.getIgnorePassages().getValue();
        String passageLayout = ItemPreviewSettings.getPassageLayout().getValue();
        boolean passageReplace = ignorePassages && !(StringUtils.isEmpty(passageLayout) || StringUtils.isWhitespace(passageLayout));

        Config config = new Config();
        config.setSections (new ArrayList<Section>());
        config.setPages(new ArrayList<Page>());

        for(ITSGroups itsGroups : itsGroupsList) {
            ITSGroup firstGroup = itsGroups == null || itsGroups.size() == 0 ? null : itsGroups.get(0);
            if (firstGroup == null) {
                continue;
            }

            String sectionLbl = firstGroup.getGroupingText();
            String sectionID = StringUtils.replace(sectionLbl, " ", "");

            // create section
            Section configSection = new Section();
            configSection.setId(sectionID);
            configSection.setLabel(sectionLbl);
            config.getSections().add(configSection);

            // create pages
            for(ITSGroup itsGroup : itsGroups) {
                Page configPage = new Page();
                // configPage.ID = itsGroup.GroupID;
                configPage.setLabel(itsGroup.getLabel());
                configPage.setSectionId (configSection.getId());

                // create passage
                if(itsGroup.getPassage() != null) {
                    Content configPassage = new Content();
                    setDocumentInfo(configPassage, itsGroup.getPassage());
                    configPage.setPassage(configPassage);
                }

                // create items
                if(itsGroup.getItems() != null && itsGroup.getItems().size() > 0) {
                    configPage.setItems (new ArrayList<Item>());

                    for (IITSDocument itsItem : itsGroup.getItems()) {
                        Item configItem = new Item();
                        setDocumentInfo(configItem, itsItem);
                        configPage.getItems().add(configItem);

                        // check if replace passage layout
                        if (passageReplace && itsGroup.getItems().size() == 1 && itsItem.getStimulusKey() > 0) {
                            configPage.setLayoutName(passageLayout);
                        }
                    }

                }

                config.getPages().add(configPage);
            }
        }

        return config;
    }

    // / <summary>
    // / Add ITS document info to the config content object.
    // / </summary>
    private void setDocumentInfo(Content configContent, IITSDocument itsDocument) {
        configContent.setFile (itsDocument.getBaseUri());

        if(configContent instanceof Item) {
            Item configItem = (Item) configContent;
            configItem.setBankKey(itsDocument.getBankKey());
            configItem.setItemKey(itsDocument.getItemKey());
        }
    }

    // Shiva: hack! the problem is ITSDocumentFactory.loadUri2 internally calls
    // getUriOriginalString() and that is definitely buggy. Talk to John about
    // that.
    // it drops the protocol. So what we will do is if the uri parameter starts
    // with the uri for current doc base then we will convert into a relative
    // path.
    private IITSDocument correctBaseUri(IITSDocument itsDocument) {
        String itsDocumentBaseUri = itsDocument.getBaseUri();
        int indexOf = itsDocumentBaseUri.indexOf (_docBaseUri.getRawPath());
        if(indexOf == 0) {
            itsDocumentBaseUri = itsDocumentBaseUri.substring(_docBaseUri.getRawPath().length());
            if (itsDocumentBaseUri.startsWith("/"))
                itsDocument.setBaseUri("~" + itsDocumentBaseUri);
            else
                itsDocument.setBaseUri("~/" + itsDocumentBaseUri);
        }
        return itsDocument;
    }
}