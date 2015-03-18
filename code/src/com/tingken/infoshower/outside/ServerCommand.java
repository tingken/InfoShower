/**
 * 
 */
package com.tingken.infoshower.outside;

/**
 * @author tingken.com
 * 
 */
public enum ServerCommand {
	SCREEN_CAPTURE("screen-capture"), RESTART("restart"), UPGRADE("upgrade"), NONE("none"), CONNECTION_FAILED(
	        "connection_failed");
	String command;

	ServerCommand(String command) {
		this.command = command;
	}

	public String toString() {
		return command;
	}
}
