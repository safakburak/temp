package com.ekinoksyazilim.etkk.prototype.tools;

import java.beans.Beans;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;

public class ResourceBundleReplacer {

	public static void replace(BundleContext bc) {
	
		if(Beans.isDesignTime()) {
			
			return;
		}
		
		BundleWiring bw = bc.getBundle().adapt(BundleWiring.class);
		
		Collection<String> resources = bw.listResources("/", "*",
                BundleWiring.LISTRESOURCES_LOCAL | BundleWiring.LISTRESOURCES_RECURSE);
		
		ClassLoader cl = bw.getClassLoader();
		
		for(String resource : resources) {
			
			if (resource.startsWith("com/") && resource.endsWith("Messages.class")
                    && resource.indexOf("$") == -1) {
				
				String className = resource.replaceAll("/", ".");
				className = className.substring(0, className.length() - ".class".length());
				
                 try {
                	 
                	 Class<?> clazz = Class.forName(className, true, cl);
                	 
                     Field bnField = clazz.getDeclaredField("BUNDLE_NAME");
                     bnField.setAccessible(true);
                     String resourceName = (String) bnField.get(null);
                     
                     URLClassLoader urlLoader = new URLClassLoader(new URL[]{new File("resources").toURI().toURL()});
                     ResourceBundle newBundle = ResourceBundle.getBundle(resourceName, Locale.getDefault(), urlLoader);
                     
                     Field rbField = clazz.getDeclaredField("RESOURCE_BUNDLE");
                     rbField.setAccessible(true);
                     
                     Field modifiersField = Field.class.getDeclaredField("modifiers");
                     modifiersField.setAccessible(true);
                     modifiersField.set(rbField, rbField.getModifiers() & ~Modifier.FINAL);
                     
                     rbField.set(null, newBundle);
                     
                 } catch(Exception e) {
                	 
                	 continue;
                 }
			}
		}
	}
}
