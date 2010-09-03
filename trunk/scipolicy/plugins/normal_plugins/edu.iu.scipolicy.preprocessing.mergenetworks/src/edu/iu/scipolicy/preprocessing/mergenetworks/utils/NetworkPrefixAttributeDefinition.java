package edu.iu.scipolicy.preprocessing.mergenetworks.utils;

import java.util.HashSet;
import java.util.Set;

import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

public class NetworkPrefixAttributeDefinition extends BasicAttributeDefinition {
	
	private static final int MAX_PREFIX_LENGTH = 10;
	private Set<BasicAttributeDefinition> otherPrefixTextboxes = 
			new HashSet<BasicAttributeDefinition>();
	private String currentValue;
	private LogService logger;

	public String getCurrentValue() {
		return currentValue;
	}

	public NetworkPrefixAttributeDefinition(String id, String name,
			String description, int type, String defaultValue, LogService logService) {
		super(id, name, description, type, defaultValue);
		this.logger = logService;
	}
	
	public void addOtherPrefixTextboxes(BasicAttributeDefinition otherPrefixTextbox) {
		otherPrefixTextboxes.add(otherPrefixTextbox);
	}
	
	@Override
	public String validate(String value) {
		/*
		 * We have to maintain state because this is the only way we can access the contents of 
		 * each textbox on the fly.
		 * */
		this.currentValue = value;
		
		for (BasicAttributeDefinition currentPrefixTextbox : otherPrefixTextboxes) {
			String otherPrefixCurrValue = ((NetworkPrefixAttributeDefinition) currentPrefixTextbox)
												.getCurrentValue();
			
			/*
			 * To avoid any nasty NPEs.
			 * */
			if (value != null && otherPrefixCurrValue != null) {
				
				if (StringUtilities.isEmptyOrWhitespace(value.trim())) {
					String errorMessage = "Prefix cannot be empty.";
//					logger.log(LogService.LOG_ERROR, errorMessage);
					return errorMessage;	
				}

				if (value.trim().matches("[\\d]+.*")) {
					String errorMessage = "Prefix cannot start with a number.";
//					logger.log(LogService.LOG_ERROR, errorMessage);
					return errorMessage;	
				}
				
				if (value.trim().length() > MAX_PREFIX_LENGTH) {
					String errorMessage = String.format("Prefix cannot be more than %s in length.", 
														MAX_PREFIX_LENGTH);
//					logger.log(LogService.LOG_ERROR, errorMessage);
					return errorMessage;	
				}
				
				if (value.equalsIgnoreCase(otherPrefixCurrValue)) {
					String errorMessage = "Prefix for different networks cannot be the same.";
//					logger.log(LogService.LOG_ERROR, errorMessage);
					return errorMessage;
				} 				
			}
		}
		return super.validate(value);
	}
}
