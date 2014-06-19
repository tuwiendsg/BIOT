package galaxy.billing.web;

import galaxy.billing.Bill;
import galaxy.billing.BillingRevenue;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/billingrevenues")
@Controller
public class BillingRevenueController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid BillingRevenue billingRevenue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, billingRevenue);
            return "billingrevenues/create";
        }
        uiModel.asMap().clear();
        billingRevenue.persist();
        return "redirect:/billingrevenues/" + encodeUrlPathSegment(billingRevenue.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new BillingRevenue());
        return "billingrevenues/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Integer id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("billingrevenue", BillingRevenue.findBillingRevenue(id));
        uiModel.addAttribute("itemId", id);
        return "billingrevenues/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("billingrevenues", BillingRevenue.findBillingRevenueEntries(firstResult, sizeNo));
            float nrOfPages = (float) BillingRevenue.countBillingRevenues() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("billingrevenues", BillingRevenue.findAllBillingRevenues());
        }
        addDateTimeFormatPatterns(uiModel);
        return "billingrevenues/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid BillingRevenue billingRevenue, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, billingRevenue);
            return "billingrevenues/update";
        }
        uiModel.asMap().clear();
        billingRevenue.merge();
        return "redirect:/billingrevenues/" + encodeUrlPathSegment(billingRevenue.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
        populateEditForm(uiModel, BillingRevenue.findBillingRevenue(id));
        return "billingrevenues/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        BillingRevenue billingRevenue = BillingRevenue.findBillingRevenue(id);
        billingRevenue.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/billingrevenues";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("billingRevenue_billingdate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, BillingRevenue billingRevenue) {
        uiModel.addAttribute("billingRevenue", billingRevenue);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("bills", Bill.findAllBills());
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
