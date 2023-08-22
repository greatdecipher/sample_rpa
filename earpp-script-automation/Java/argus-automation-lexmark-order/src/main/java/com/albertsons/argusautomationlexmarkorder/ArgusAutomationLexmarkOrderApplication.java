package com.albertsons.argusautomationlexmarkorder;

import com.albertsons.argusautomationlexmarkorder.dto.OrderSingleton;
import com.albertsons.argusautomationlexmarkorder.dto.Store;
import com.albertsons.argusautomationlexmarkorder.dto.UnitOrder;
import com.albertsons.argusautomationlexmarkorder.services.AutomationProcessService;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ArgusAutomationLexmarkOrderApplication implements CommandLineRunner {


	@Autowired
	private AutomationProcessService automationProcessService;
	
	private static Logger LOG = LoggerFactory.getLogger(ArgusAutomationLexmarkOrderApplication.class);
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ArgusAutomationLexmarkOrderApplication.class, args);
		LOG.info("Lexmark App started...");
		System.exit(SpringApplication.exit(context, () -> 0));
	}

	@Override
	public void run(String... args) throws Exception {
		// //"10.189.34.135~True~Imaging:2;Toner:1;Tray Cover:1;Paper:2"
		String input = args[0];
		String[] vars= input.split("~");
		String[] items = vars[2].split(";");
		List<UnitOrder> listUnitOrders = new ArrayList<UnitOrder>();

		for(int i =0;i<items.length;i++) {
			String[] unitOrders = items[i].split(":");
			UnitOrder unitOrder = new UnitOrder();
			unitOrder.setType(unitOrders[0]);
			unitOrder.setQuantity(Integer.parseInt(unitOrders[1]));
			listUnitOrders.add(unitOrder);
		}
		
		String ipaddress = vars[0];
		OrderSingleton orderSingleton = OrderSingleton.getInstance();
		Store store = new Store();
		store.setIpaddress(ipaddress);
		orderSingleton.setStore(store);
		orderSingleton.setUnitOrderList(listUnitOrders);
		try {
			automationProcessService.initiatePlaceOrders(orderSingleton);
		}catch(NullPointerException e) {
			LOG.error("NullPointerException occurred at run method.");
			LOG.error(e.getMessage());
		}catch(RuntimeException e) {
			LOG.error("RuntimeException occurred at run method.");
			LOG.error(e.getMessage());
		}catch(Exception e) {
			LOG.error("Exception occurred at run method.");
			LOG.error(e.getMessage());
		}

		
		
	}
}
