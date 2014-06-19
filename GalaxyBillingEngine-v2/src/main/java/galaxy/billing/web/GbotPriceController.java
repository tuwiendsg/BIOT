package galaxy.billing.web;

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

@RequestMapping("/gbotprices")
@Controller
public class GbotPriceController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid GbotPrice gbotPrice, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, gbotPrice);
            return "gbotprices/create";
        }
        uiModel.asMap().clear();
        gbotPrice.persist();
        return "redirect:/gbotprices/" + encodeUrlPathSegment(gbotPrice.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new GbotPrice());
        return "gbotprices/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Integer id, Model uiModel) {
        uiModel.addAttribute("gbotprice", GbotPrice.findGbotPrice(id));
        uiModel.addAttribute("itemId", id);
        return "gbotprices/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("gbotprices", GbotPrice.findGbotPriceEntries(firstResult, sizeNo));
            float nrOfPages = (float) GbotPrice.countGbotPrices() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("gbotprices", GbotPrice.findAllGbotPrices());
        }
        return "gbotprices/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid GbotPrice gbotPrice, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, gbotPrice);
            return "gbotprices/update";
        }
        uiModel.asMap().clear();
        gbotPrice.merge();
        return "redirect:/gbotprices/" + encodeUrlPathSegment(gbotPrice.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
        populateEditForm(uiModel, GbotPrice.findGbotPrice(id));
        return "gbotprices/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        GbotPrice gbotPrice = GbotPrice.findGbotPrice(id);
        gbotPrice.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/gbotprices";
    }

	void populateEditForm(Model uiModel, GbotPrice gbotPrice) {
        uiModel.addAttribute("gbotPrice", gbotPrice);
        uiModel.addAttribute("gbots", Gbot.findAllGbots());
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
