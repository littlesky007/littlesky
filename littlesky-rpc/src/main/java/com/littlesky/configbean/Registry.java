package com.littlesky.configbean;

import java.io.Serializable;

public class Registry implements Serializable{
    
   

	/**
	 * 
	 */
	private static final long serialVersionUID = 4907156495457294048L;

	private String protocol;

    private String address;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
