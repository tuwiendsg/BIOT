package galaxy.billing.web;

import galaxy.billing.BillLine;
import galaxy.billing.EventLog;
import galaxy.billing.Gbot;
import galaxy.billing.GbotPrice;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/gbots")
@Controller
public class GbotController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Gbot gbot, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, gbot);
            return "gbots/create";
        }
        uiModel.asMap().clear();
        gbot.persist();
        return "redirect:/gbots/" + encodeUrlPathSegment(gbot.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Gbot());
        return "gbots/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Integer id, Model uiModel) {
        uiModel.addAttribute("gbot", Gbot.findGbot(id));
        uiModel.addAttribute("itemId", id);
        return "gbots/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("gbots", Gbot.findGbotEntries(firstResult, sizeNo));
            float nrOfPages = (float) Gbot.countGbots() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("gbots", Gbot.findAllGbots());
        }
        return "gbots/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Gbot gbot, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, gbot);
            return "gbots/update";
        }
        uiModel.asMap().clear();
        gbot.merge();
        return "redirect:/gbots/" + encodeUrlPathSegment(gbot.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
        populateEditForm(uiModel, Gbot.findGbot(id));
        return "gbots/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Gbot gbot = Gbot.findGbot(id);
        gbot.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/gbots";
    }

	void populateEditForm(Model uiModel, Gbot gbot) {
        uiModel.addAttribute("gbot", gbot);
        uiModel.addAttribute("billlines", BillLine.findAllBillLines());
        uiModel.addAttribute("eventlogs", EventLog.findAllEventLogs());
        uiModel.addAttribute("gbotprices", GbotPrice.findAllGbotPrices());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
