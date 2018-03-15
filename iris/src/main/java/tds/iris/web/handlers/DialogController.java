package tds.iris.web.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import tds.irisshared.repository.IContentHelper;
import tds.itemrenderer.data.ItemRenderGroup;
import tds.itemrenderer.webcontrols.PageLayout;
import tds.itemrenderer.webcontrols.rendererservlet.RendererServlet;

@Scope("prototype")
@Controller
public class DialogController {

    private static final Logger _logger = LoggerFactory.getLogger (DialogController.class);

    @Autowired
    private IContentHelper _contentHelper;

    @Autowired
    private PageLayout _pageLayout;

    @RequestMapping(value = "/content/dialog", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView getContent(
            @RequestParam(value = "bankKey",
                    required = false)
                    long bankKey,
            @RequestParam(value = "itemKey",
                    required = false)
                    long itemKey,
            @RequestParam(value = "language",
                    required = false)
                    String contentLanguage
    ) {

        ModelAndView model = new ModelAndView();

        ItemRenderGroup itemRenderGroup = _contentHelper.loadTutorial(bankKey, itemKey, contentLanguage);
        _pageLayout.setItemRenderGroup(itemRenderGroup);
        RendererServlet.getRenderedOutput (_pageLayout);

        String content =  _pageLayout.getRenderToString ();
        model.setViewName("dialog");
        model.addObject("content", content);
        return model;
    }



}
