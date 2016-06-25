package cn.jlook.jfinal.ext3.kit;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import cn.jlook.jfinal.ext3.plugin.seconddomain.SecondDomainPlugin.SecondDomain;

/**
 * 二级域名控制器,单例模式
 * 
 * @author yanglinghui
 */
public class SecondDomainKit {
    private static Map<String, String> domain;

    public static void setSecondDomains(List<SecondDomain> secondDomains) {
        if (domain == null) domain = Maps.newHashMap();
        for (SecondDomain secondDomain : secondDomains) {
            domain.put(secondDomain.getDomainKey(), secondDomain.getControllerKey());
        }

    }

    public static String contains(String d) {
        if (domain.containsKey(d)) {
            return domain.get(d);
        } else
            return null;
    }

}
