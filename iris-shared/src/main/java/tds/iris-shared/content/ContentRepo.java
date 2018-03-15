package tds.iris.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import tds.blackbox.ContentRequestException;
import tds.iris.abstractions.repository.IContentHelper;
import tds.iris.repository.ContentHelper;
import tds.itemrenderer.data.AccLookup;
import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.xml.wordlist.Itemrelease;
import tds.itemrenderer.repository.ContentRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@Component
@Scope("singleton")
public class ContentRepo implements ContentRepository{
    @Autowired
    private IContentHelper _contentHelper;


    public IITSDocument findItemDocument(
            final String itemPath,
            final AccLookup accommodations,
            final String contextPath,
            final boolean oggAudioSupport) throws ContentRequestException {
        //TODO: Add methhod to itemrender to accept string instead of item id
        return _contentHelper.getITSDocument("I-187-3477", accommodations);

    }

    public InputStream findResource(final String resourcePath) throws IOException {
        return new FileInputStream(resourcePath);
    }

    public Itemrelease findWordListItem(
            final String itemPath,
            final String contextPath,
            final boolean oggAudioSupport) throws ContentRequestException {
        return null;
    }


}
