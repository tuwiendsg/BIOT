package galaxy.billing;




import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.infosys.event.Event;
import at.ac.tuwien.infosys.event.EventListener;
import at.ac.tuwien.infosys.event.EventPublisher;
import at.ac.tuwien.infosys.event.EventPublisherException;
import at.ac.tuwien.infosys.event.EventSubscriber;
import at.ac.tuwien.infosys.event.EventSubscriberException;
import at.ac.tuwien.infosys.event.GbotEvent;
import at.ac.tuwien.infosys.event.GbotEvent.GbotAction;
import at.ac.tuwien.infosys.event.GbotEvent.GbotType;
import at.ac.tuwien.infosys.event.JMSEventPublisher;
import at.ac.tuwien.infosys.event.JMSEventSubscriber;
import at.ac.tuwien.infosys.event.JMSUtil;

public class GbotEventListener implements EventListener {

	// Interface of the EventSubscriber
	private EventSubscriber subscriber;
	
	private static int second_usageCount=0;
	private static int event_usageCount=0;
	
	private static Date usageStarted;
	private static Date usageStopped;
	
	private Gbot lightBot;
	private Gbot temperBot;
	private boolean lightBotStopped=false;
	private boolean temperBotStopped=false;
	
	public static boolean isStarted=false;

	public GbotEventListener() {
		// create the subscriber to listen for events in the topic.
		// subscriber requires a class which implements the EventListener
		// interface. When the subscriber receives an event it will notify the
		// Listener, by invoking the "processEvent(Event event)" method.
		subscriber = new JMSEventSubscriber(this);
	}

	public void start() throws EventSubscriberException {
		// adds a subscription to the "Infosys_SOA_Event_Topic" which is hosted
		// on a server in our lab environment
		isStarted=true;
		subscriber.subscribe(JMSUtil.connectionString, JMSUtil.topicName);
	}

	public void stop() throws EventSubscriberException {
		// removes the subscription
		isStarted=false;
		subscriber.unsubsribe();
	}

	public  void check() {

		System.out.println("Starting Subscriber .....");

		GbotEventListener sample = new GbotEventListener();

		try {
  
			sample.start();

			

		} catch (EventSubscriberException e) {
			e.printStackTrace();
		} finally {
			/*try {
				sample.stop();
			} catch (EventSubscriberException e) {
				e.printStackTrace();
			}*/
		}

		
	}
	

	// This method has to be implemented to get notified about incoming events!
	@Override
	@Transactional
	public void processEvent(Event event) {
		
		GbotEvent gEvent=(GbotEvent)event;
		int gbotId=Integer.parseInt(gEvent.getId()); 


		int lightUsage;
		
		Gbot gbot=new Gbot();
		gbot = Gbot.findGbot(gbotId);
		
		if (gbot.getName().equals("Light"))
			lightBot=gbot;
		else if (gbot.getName().equals("Temperature"))
			temperBot=gbot;
		
		
		/*******************Store eventLog in DB **************************/
		EventLog eventLog = new EventLog();

				 	
		eventLog.setGbotsId(gbot);		
		eventLog.setInstanceId(gEvent.getInstanceId());	
		eventLog.setPublishDate( gEvent.getTimestamp());
		eventLog.setCustomerId(gEvent.getCustomerId());
		eventLog.setProviderId(gEvent.getProviderId());
		eventLog.setGbotType(gEvent.getType().name());
		eventLog.setGbotAction(gEvent.getAction().name());
		
		eventLog.persist(); //store in DB
		
		/******************************************************************/
		
		//if( gbotId==3) 
		if(gbot.getName().equals("Temperature"))//gbot.getMeteringUnit().contains("Usage")) 
		{
			if (gEvent.getAction()==GbotAction.starting)
				event_usageCount=0;
			else if (gEvent.getAction()==GbotAction.consuming_resources)
				event_usageCount++;	
			else if (gEvent.getAction()==GbotAction.stopping)
				temperBotStopped=true;
			
		}
		
		
		else if( gbot.getName().equals("Light"))//gbot.getMeteringUnit().contains("Time")) 
		{
			if (gEvent.getAction()==GbotAction.starting){
				second_usageCount=0;
				usageStarted=gEvent.getTimestamp();
			}
			else if (gEvent.getAction()==GbotAction.consuming_resources)
				second_usageCount++;
			else if (gEvent.getAction()==GbotAction.stopping){
				lightBotStopped=true;
				usageStopped=gEvent.getTimestamp();
				
			}
				
		}
		
		
		
		//System.out.println("event_usageCount: " + event_usageCount);
		
		if(lightBotStopped && temperBotStopped)
		{
			lightUsage=Math.round( (usageStopped.getTime()-usageStarted.getTime())/1000);
			System.out.println("LightUsage: " + lightUsage);
			System.out.println("TemperUsage: " + event_usageCount);
			
		double lightBot_PriceUnit=	lightBot.getGbotPriceId().getPrice();
		double temperBot_PriceUnit=	temperBot.getGbotPriceId().getPrice();
		
		double lightBot_totalCost=lightBot_PriceUnit * lightUsage;
		double temperBot_totalCost=temperBot_PriceUnit * event_usageCount;
		
		System.out.println("lightBot_totalCost: " + lightBot_totalCost);
		System.out.println("temperBot_totalCost: " + temperBot_totalCost);
		
		double totalCost= lightBot_totalCost + temperBot_totalCost;
		
		System.out.println("totalCost: " + totalCost);
		
			/********************** Store Bill in DB **************************/
			Bill totalBill=new Bill();
			totalBill.setCreateDatetime(new java.util.Date());
			totalBill.setCurrency(gbot.getGbotPriceId().getCurrency());
			totalBill.setCustomer(gEvent.getCustomerId());
			totalBill.setStatus("Not Paid");
			totalBill.setTotal(BigDecimal.valueOf(totalCost));
			totalBill.setDescription("This bill is generated for gbot customer based on gbot usage events");
			
			totalBill.persist();
		
		
			/********************** Store BillLine for lightBot in DB **************************/
			BillLine lightBillLine=new BillLine();
			lightBillLine.setBillId(totalBill);
			lightBillLine.setAmount(BigDecimal.valueOf(lightBot_totalCost));
			lightBillLine.setGbotsId(lightBot);
			lightBillLine.setQuantity(lightUsage);
			lightBillLine.persist();
			  
			/********************** Store BillLine for TemperBot in DB **************************/
			BillLine temperBillLine=new BillLine();
			temperBillLine.setBillId(totalBill);
			temperBillLine.setAmount(BigDecimal.valueOf(temperBot_totalCost));
			temperBillLine.setGbotsId(temperBot);
			temperBillLine.setQuantity(event_usageCount);
			temperBillLine.persist();
			
			/********************** Store Bill Revenue for current Bill in DB **************************/
			double revenueAmount=totalCost*5/100;
			BillingRevenue revenue=new BillingRevenue();
			revenue.setBillId(totalBill);
			revenue.setBillingAmount(BigDecimal.valueOf(totalCost));
			revenue.setBillingDate(totalBill.getCreateDatetime());
			
			revenue.setRevenueAmount(BigDecimal.valueOf(revenueAmount));
			revenue.persist();
			
		
		
			
			lightBotStopped=false;
			temperBotStopped=false;
			
		
		}
		System.out.println(event);
	}

}
