package edu.iu.nwb.thingie.testing1;

import java.lang.reflect.Field;

public class SecretReceiver {
	public void findOutTheSecret(Cover cover) {
		try {
		Class clazz = cover.getClass();
		Class superClazz = clazz.asSubclass(Class.forName("edu.iu.nwb.thingie.testing2.LookInMe"));
		Field field = (Field) superClazz.getDeclaredField("secret");
		field.setAccessible(true);
		System.out.println(field.get(cover));
		} catch (NoSuchFieldException e1) {
			System.out.println("WHOOPS!");
		} catch (IllegalAccessException e2) {
			System.out.println("WHOOPS 2!");
		} catch (ClassNotFoundException e3) {
			System.out.println("WHOOPS CLASS NOT FOUND!");
		}
	}
}
