package com.jbl.t24.rest.api.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HostIpHandle {
	
	public static Map<String, String> hostIp(HttpServletRequest httpServletRequest){
		String remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
        String host = httpServletRequest.getRemoteHost();

        if (remoteAddr == null) {
            remoteAddr = httpServletRequest.getRemoteAddr();
        }
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("remoteAddr", remoteAddr);
        map.put("host", host);
        return map;
	}

}
