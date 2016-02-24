package com.redis.thd.rmi;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author dogcome
 *         <p/>
 *         <p>获取系统运行所需要的配置信息，对应的配置文件名称为config.properties</p>
 */
public class Configur {
    private static final String BUNDLE_NAME = "test.rmi.config";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    private Configur() {
    }

    /**
     * <p>通过key名称获得配置文件的相关信息</p>
     *
     * @param key key名称
     * @return String 配置文件信息
     */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}

