package tds.iris.Models;

import tds.itemrenderer.data.IITSDocument;
import tds.itemrenderer.data.ITSTypes.ITSEntityType;

public class ItsItemIdUtil
{
    final static String PASSAGE_ID_FORMAT = "P-%s-%s";
    final static String ITEM_ID_FORMAT = "I-%s-%s";
    final static String UNKNOWN_ID_FORMAT = "U-%s-%s";
    final static String REVISION_FORMAT = "%s-%s";

    public static String getItsDocumentId (IITSDocument document) {
        return getItsDocumentId (document.getBankKey (), document.getItemKey (), document.getType ());
    }

    public static String getItsDocumentId (IITSDocument document, String revisionKey) {
        return String.format(
                REVISION_FORMAT,
                getItsDocumentId(document.getBankKey (), document.getItemKey (), document.getType ()),
                revisionKey
        );
    }

    public static String getItsDocumentId (long bankKey, long itemKey, ITSEntityType type) {
        String formatString = null;
        if (type == ITSEntityType.Item) {
            formatString = ITEM_ID_FORMAT;
        }
        else if (type == ITSEntityType.Passage) {
            formatString = PASSAGE_ID_FORMAT;
        }
        else {
            // TODO what should we do here?
            formatString = UNKNOWN_ID_FORMAT;
        }
        return String.format (formatString, bankKey, itemKey);
    }

    public static String translateItemId(String item) throws IllegalArgumentException {
        String translatedStr = item.toLowerCase();
        if(!validItemId(translatedStr)) {
            throw new IllegalArgumentException();
        }

        translatedStr = translatedStr.replace("i","Item");
        translatedStr = translatedStr.replace("p","stim");

        return translatedStr;
    }

    public static boolean validItemId(String item) {
        //move regex string to private final var later.
        return item.matches("[ip]-[0-9]+-[0-9]+(-[0-9a-zA-Z]+)?");
    }
}
