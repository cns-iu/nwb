package edu.iu.epic.modelbuilder.gui.utility;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class NotificationArea extends JTextArea {
	
	private static final String DEFAULT_NOTIFICATION_SEPERATOR = "\n";
	private String notificationSeparator;
	private List<String> notifications;
	
	public NotificationArea(int rows) {
        super(rows, 0);
        this.setEditable(false);
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.notificationSeparator = DEFAULT_NOTIFICATION_SEPERATOR;
        this.notifications = new ArrayList<String>();
	}
	
	public NotificationArea(int rows, String notificationSeparator) {
		this(rows);
		this.notificationSeparator = notificationSeparator;
	}
	
    private void refreshNotificationArea() {
    	this.setText("");
    	
    	int notificationCounter = 0;
		for (String notification : notifications) {
			this.append(notification);
			if (notificationCounter != notifications.size() - 1) {
				this.append(notificationSeparator);
			} 
		}
		
    }

	public void addNotification(String notification) {
		/*
		 * Always add at the first position to imitate stack behavior.
		 * */
		notifications.add(0, notification);
		refreshNotificationArea();
	}
	
	private void addUniqueNotification(String notification) {
		/*
		 * Always add at the first position to imitate stack behavior.
		 * Also only add a notification if its not already there. This 
		 * is useful in cases like unbound variable - it is not required
		 * to print the same variable repeatedly.
		 * */
		if (!notifications.contains(notification)) {
			notifications.add(0, notification);
		}
		refreshNotificationArea();
	}

	public void addAllNotifications(Set<String> notificationsToBeAdded) {
		resetNotifications();
		for (String notification : notificationsToBeAdded) {
			addUniqueNotification(notification);
		}
	} 
	
	
	public void resetNotifications() {
			notifications = new ArrayList<String>();
			refreshNotificationArea();
			System.out.println("reseted " + notifications);
	}
	
	public List<String> getNotifications() {
		return notifications;
	}

}
