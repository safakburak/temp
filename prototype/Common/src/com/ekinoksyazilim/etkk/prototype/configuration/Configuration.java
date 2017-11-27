package com.ekinoksyazilim.etkk.prototype.configuration;

import java.awt.Color;

public class Configuration extends AbstractConfiguration {

	@config("version")
    public String VERSION = "X.X.X";
	
	@config("example.string")
    public String STRING_EXAMPLE = "lorem ipsum";
	
	@config("example.integer")
    public Integer INTEGER_EXAMPLE = 3;
	
	@config("example.color")
    public Color COLOR_EXAMPLE = Color.YELLOW;
	
	public Configuration() {
		
		load();
	}
	
	@Override
	protected String getPath() {
		
		return "config.xml";
	}
}
