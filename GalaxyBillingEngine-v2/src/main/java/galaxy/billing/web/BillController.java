package galaxy.billing.web;

import galaxy.billing.Bill;
import galaxy.billing.BillLine;
import galaxy.billing.BillingRevenue;
import galaxy.billing.GbotEventListener;

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

import at.ac.tuwien.infosys.event.EventSubscriberException;

@RequestMapping("/bills")
@Controller
public class BillController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Bill bill, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bill);
            return "bills/create";
        }
        uiModel.asMap().clear();
        bill.persist();
        return "redirect:/bills/" + encodeUrlPathSegment(bill.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Bill());
        return "bills/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Integer id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("bill", Bill.findBill(id));
        uiModel.addAttribute("itemId", id);
        return "bills/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bills", Bill.findBillEntries(firstResult, sizeNo));
            float nrOfPages = (float) Bill.countBills() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bills", Bill.findAllBills());
        }
        addDateTimeFormatPatterns(uiModel);
        
        
        /***************************** Start Listening to Topic *********************************/
        if (!GbotEventListener.isStarted){
	        System.out.println("Starting Subscriber .....");
	        GbotEventListener eListener=new GbotEventListener();
	
	        
	        try {
	        	  
	        	eListener.start();
	
				
	
			} catch (EventSubscriberException e) {
				e.printStackTrace();
			} finally {
				
			}
        }
        
        
        
        
        return "bills/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Bill bill, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bill);
            return "bills/update";
        }
        uiModel.asMap().clear();
        bill.merge();
        return "redirect:/bills/" + encodeUrlPathSegment(bill.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
        populateEditForm(uiModel, Bill.findBill(id));
        return "bills/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Bill bill = Bill.findBill(id);
        bill.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/bills";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("bill_createdatetime_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Bill bill) {
        uiModel.addAttribute("bill", bill);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("billlines", BillLine.findAllBillLines());
        uiModel.addAttribute("billingrevenues", BillingRevenue.findAllBillingRevenues());
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
