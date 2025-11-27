package com.gamza.study.jwt;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Component
public class WhiteListMatcher {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public boolean isWhiteListed(String path, List<String> whiteList) {
        for(String pattern : whiteList) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

}
