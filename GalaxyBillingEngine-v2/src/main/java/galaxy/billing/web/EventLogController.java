package galaxy.billing.web;

import galaxy.billing.EventLog;
import galaxy.billing.Gbot;
import galaxy.billing.GbotEventListener;
import galaxy.billing.GbotPrice;

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

@RequestMapping("/eventlogs")
@Controller
public class EventLogController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid EventLog eventLog, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, eventLog);
            return "eventlogs/create";
        }
        uiModel.asMap().clear();
        eventLog.persist();
        return "redirect:/eventlogs/" + encodeUrlPathSegment(eventLog.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new EventLog());
        return "eventlogs/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Integer id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("eventlog", EventLog.findEventLog(id));
        uiModel.addAttribute("itemId", id);
        return "eventlogs/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
       
        ////////////////////////////////////////////////////////////////////
        

        
        ////////////////////////////////////////////////////////////////////
		if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("eventlogs", EventLog.findEventLogEntries(firstResult, sizeNo));
            float nrOfPages = (float) EventLog.countEventLogs() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("eventlogs", EventLog.findAllEventLogs());
        }
        addDateTimeFormatPatterns(uiModel);
        

        return "eventlogs/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid EventLog eventLog, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, eventLog);
            return "eventlogs/update";
        }
        uiModel.asMap().clear();
        eventLog.merge();
        return "redirect:/eventlogs/" + encodeUrlPathSegment(eventLog.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
        populateEditForm(uiModel, EventLog.findEventLog(id));
        return "eventlogs/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        EventLog eventLog = EventLog.findEventLog(id);
        eventLog.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/eventlogs";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("eventLog_publishdate_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, EventLog eventLog) {
        uiModel.addAttribute("eventLog", eventLog);
        addDateTimeFormatPatterns(uiModel);
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
