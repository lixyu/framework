package com.vcredit.framework.util;

import org.springframework.util.PathMatcher;
import org.springframework.util.AntPathMatcher;

/**
 * 权限URL拦截
 * @author sk_mao
 *
 */
public class AntUrlPathMatcher implements UrlMatcher {
    private boolean requiresLowerCaseUrl = true;
    private PathMatcher pathMatcher = new AntPathMatcher();

    public AntUrlPathMatcher() {
        this(true);
    }

    public AntUrlPathMatcher(boolean requiresLowerCaseUrl) {
        this.requiresLowerCaseUrl = requiresLowerCaseUrl;
    }

    public Object compile(String path) {
        if (requiresLowerCaseUrl) {
            return path.toLowerCase();
        }

        return path;
    }

    public void setRequiresLowerCaseUrl(boolean requiresLowerCaseUrl) {
        this.requiresLowerCaseUrl = requiresLowerCaseUrl;
    }

    public boolean pathMatchesUrl(Object path, String url) {
    	if(path==null)
    		return true;
        return pathMatcher.match((String)path, url);
    }

    public String getUniversalMatchPattern() {
        return "/**";
    }

    public boolean requiresLowerCaseUrl() {
        return requiresLowerCaseUrl;
    }

    public String toString() {
        return getClass().getName() + "[requiresLowerCase='" + requiresLowerCaseUrl + "']";
    }
}
