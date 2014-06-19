package at.ac.tuwien.infosys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

public class Sample implements EventListener {

	// Interface of the EventSubscriber
/*	private EventSubscriber subscriber;

	public Sample() {
		// create the subscriber to listen for events in the topic.
		// subscriber requires a class which implements the EventListener
		// interface. When the subscriber receives an event it will notify the
		// Listener, by invoking the "processEvent(Event event)" method.
		subscriber = new JMSEventSubscriber(this);
	}

	public void start() throws EventSubscriberException {
		// adds a subscription to the "Infosys_SOA_Event_Topic" which is hosted
		// on a server in our lab environment
		subscriber.subscribe(JMSUtil.connectionString, JMSUtil.topicName);
	}

	public void stop() throws EventSubscriberException {
		// removes the subscription
		subscriber.unsubsribe();
	}*/

	public static void main(String[] args) {

	//	System.out.println("Starting Subscriber .....");

	//	Sample sample = new Sample();

		//try {

		//sample.start();

			// add a GbotEvent to the topic, just to demonstrate functionality!
			// The following can be ignored for the Subscriber!!!
			// ------------------------------------------------------------------
			try {
				
				System.out
						.println("-- Publisher sends an event to demonstrate functionality! --");
				
				for(int i=0;i<10;i++){
				EventPublisher publisher = new JMSEventPublisher();
				publisher.start(JMSUtil.connectionString, JMSUtil.topicName);
				publisher.publishEvent(new GbotEvent("temp", "01", "cust01",
						"prov01", GbotType.Maintenance, GbotAction.starting));
				publisher.stop();
				
				}
				System.out.println("-- Publisher stopped! --");
			} catch (EventPublisherException e1) {
				e1.printStackTrace();
			}
			// ------------------------------------------------------------------
			// The code above is not important for the Subscriber!!!

/*			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));

			System.out.println("Hit <Enter> to stop Subscriber!");

			try {
				reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			*/

/*		} catch (EventSubscriberException e) {
			e.printStackTrace();
		} finally {
			try {
				sample.stop();
			} catch (EventSubscriberException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Subscriber stopped .....");*/
	}

	// This method has to be implemented to get notified about incoming events!
	@Override
	public void processEvent(Event event) {
		System.out.println("Got event pub: " + event);
	}

}
