package galaxy.billing.web;

import galaxy.billing.Bill;
import galaxy.billing.BillLine;
import galaxy.billing.BillingRevenue;
import galaxy.billing.EventLog;
import galaxy.billing.Gbot;
import galaxy.billing.GbotPrice;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	public Converter<Bill, String> getBillToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<galaxy.billing.Bill, java.lang.String>() {
            public String convert(Bill bill) {
                return new StringBuilder().append(bill.getCreateDatetime()).append(" ").append(bill.getCustomer()).append(" ").append(bill.getStatus()).append(" ").append(bill.getTotal()).toString();
            }
        };
    }

	public Converter<Integer, Bill> getIdToBillConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, galaxy.billing.Bill>() {
            public galaxy.billing.Bill convert(java.lang.Integer id) {
                return Bill.findBill(id);
            }
        };
    }

	public Converter<String, Bill> getStringToBillConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, galaxy.billing.Bill>() {
            public galaxy.billing.Bill convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Bill.class);
            }
        };
    }

	public Converter<BillLine, String> getBillLineToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<galaxy.billing.BillLine, java.lang.String>() {
            public String convert(BillLine billLine) {
                return new StringBuilder().append(billLine.getAmount()).append(" ").append(billLine.getQuantity()).toString();
            }
        };
    }

	public Converter<Integer, BillLine> getIdToBillLineConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, galaxy.billing.BillLine>() {
            public galaxy.billing.BillLine convert(java.lang.Integer id) {
                return BillLine.findBillLine(id);
            }
        };
    }

	public Converter<String, BillLine> getStringToBillLineConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, galaxy.billing.BillLine>() {
            public galaxy.billing.BillLine convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), BillLine.class);
            }
        };
    }

	public Converter<BillingRevenue, String> getBillingRevenueToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<galaxy.billing.BillingRevenue, java.lang.String>() {
            public String convert(BillingRevenue billingRevenue) {
                return new StringBuilder().append(billingRevenue.getBillingDate()).append(" ").append(billingRevenue.getBillingAmount()).append(" ").append(billingRevenue.getRevenueAmount()).toString();
            }
        };
    }

	public Converter<Integer, BillingRevenue> getIdToBillingRevenueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, galaxy.billing.BillingRevenue>() {
            public galaxy.billing.BillingRevenue convert(java.lang.Integer id) {
                return BillingRevenue.findBillingRevenue(id);
            }
        };
    }

	public Converter<String, BillingRevenue> getStringToBillingRevenueConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, galaxy.billing.BillingRevenue>() {
            public galaxy.billing.BillingRevenue convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), BillingRevenue.class);
            }
        };
    }

	public Converter<EventLog, String> getEventLogToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<galaxy.billing.EventLog, java.lang.String>() {
            public String convert(EventLog eventLog) {
                return new StringBuilder().append(eventLog.getInstanceId()).append(" ").append(eventLog.getPublishDate()).append(" ").append(eventLog.getCustomerId()).append(" ").append(eventLog.getProviderId()).toString();
            }
        };
    }

	public Converter<Integer, EventLog> getIdToEventLogConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, galaxy.billing.EventLog>() {
            public galaxy.billing.EventLog convert(java.lang.Integer id) {
                return EventLog.findEventLog(id);
            }
        };
    }

	public Converter<String, EventLog> getStringToEventLogConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, galaxy.billing.EventLog>() {
            public galaxy.billing.EventLog convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), EventLog.class);
            }
        };
    }

	public Converter<Gbot, String> getGbotToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<galaxy.billing.Gbot, java.lang.String>() {
            public String convert(Gbot gbot) {
                return new StringBuilder().append(gbot.getName()).append(" ").append(gbot.getType()).append(" ").append(gbot.getMeteringUnit()).toString();
            }
        };
    }

	public Converter<Integer, Gbot> getIdToGbotConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, galaxy.billing.Gbot>() {
            public galaxy.billing.Gbot convert(java.lang.Integer id) {
                return Gbot.findGbot(id);
            }
        };
    }

	public Converter<String, Gbot> getStringToGbotConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, galaxy.billing.Gbot>() {
            public galaxy.billing.Gbot convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), Gbot.class);
            }
        };
    }

	public Converter<GbotPrice, String> getGbotPriceToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<galaxy.billing.GbotPrice, java.lang.String>() {
            public String convert(GbotPrice gbotPrice) {
                return new StringBuilder().append(gbotPrice.getCurrency()).append(" ").append(gbotPrice.getPrice()).append(" ").append(gbotPrice.getPriceList()).toString();
            }
        };
    }

	public Converter<Integer, GbotPrice> getIdToGbotPriceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Integer, galaxy.billing.GbotPrice>() {
            public galaxy.billing.GbotPrice convert(java.lang.Integer id) {
                return GbotPrice.findGbotPrice(id);
            }
        };
    }

	public Converter<String, GbotPrice> getStringToGbotPriceConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, galaxy.billing.GbotPrice>() {
            public galaxy.billing.GbotPrice convert(String id) {
                return getObject().convert(getObject().convert(id, Integer.class), GbotPrice.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getBillToStringConverter());
        registry.addConverter(getIdToBillConverter());
        registry.addConverter(getStringToBillConverter());
        registry.addConverter(getBillLineToStringConverter());
        registry.addConverter(getIdToBillLineConverter());
        registry.addConverter(getStringToBillLineConverter());
        registry.addConverter(getBillingRevenueToStringConverter());
        registry.addConverter(getIdToBillingRevenueConverter());
        registry.addConverter(getStringToBillingRevenueConverter());
        registry.addConverter(getEventLogToStringConverter());
        registry.addConverter(getIdToEventLogConverter());
        registry.addConverter(getStringToEventLogConverter());
        registry.addConverter(getGbotToStringConverter());
        registry.addConverter(getIdToGbotConverter());
        registry.addConverter(getStringToGbotConverter());
        registry.addConverter(getGbotPriceToStringConverter());
        registry.addConverter(getIdToGbotPriceConverter());
        registry.addConverter(getStringToGbotPriceConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
