package com.flash.framework.dubbo.env;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubboConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.ContextIdApplicationContextInitializer;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The lowest precedence {@link EnvironmentPostProcessor} processes
 * {@link SpringApplication#setDefaultProperties(Properties) Spring Boot default properties} for Dubbo
 * as late as possible before {@link ConfigurableApplicationContext#refresh() application context refresh}.
 */
public class DubboDefaultPropertiesEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    /**
     * The name of default {@link PropertySource} defined in SpringApplication#configurePropertySources method.
     */
    private static final String PROPERTY_SOURCE_NAME = "defaultProperties";

    /**
     * The property name of Spring Application
     *
     * @see ContextIdApplicationContextInitializer
     */
    private static final String SPRING_APPLICATION_NAME_PROPERTY = "spring.application.name";

    /**
     * The property name of {@link ApplicationConfig}
     *
     * @see EnableDubboConfig
     */
    private static final String DUBBO_APPLICATION_NAME_PROPERTY = "dubbo.application.name";

    /**
     * The property name of {@link EnableDubboConfig#multiple() @EnableDubboConfig.multiple()}
     */
    private static final String DUBBO_CONFIG_MULTIPLE_PROPERTY = "dubbo.config.multiple";

    /**
     * The property name of {@link ApplicationConfig#getQosEnable() application's QOS enable}
     */
    private static final String DUBBO_APPLICATION_QOS_ENABLE_PROPERTY = "dubbo.application.qos-enable";

    private static final String DUBBO_PROVIDER_SERVER = "dubbo.provider.server";

    private static final String DUBBO_CONSUMER_CLIENT = "dubbo.consumer.client";

    private static final String DUBBO_PROTOCOL_SERIALIZATION = "dubbo.protocol.serialization";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> defaultProperties = createDefaultProperties(environment);
        if (!CollectionUtils.isEmpty(defaultProperties)) {
            addOrReplace(propertySources, defaultProperties);
        }
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    private Map<String, Object> createDefaultProperties(ConfigurableEnvironment environment) {
        Map<String, Object> defaultProperties = new HashMap<>();
        setDubboApplicationNameProperty(environment, defaultProperties);
        setDubboConfigMultipleProperty(environment, defaultProperties);
        setDubboApplicationQosEnableProperty(environment, defaultProperties);
        setDubboProviderServerProperty(environment, defaultProperties);
        setDubboConsumerClientProperty(environment, defaultProperties);
        setDubboProtocolSerializationProperty(environment, defaultProperties);
        return defaultProperties;
    }

    private void setDubboApplicationNameProperty(Environment environment, Map<String, Object> defaultProperties) {
        String springApplicationName = environment.getProperty(SPRING_APPLICATION_NAME_PROPERTY);
        if (StringUtils.hasLength(springApplicationName)
                && !environment.containsProperty(DUBBO_APPLICATION_NAME_PROPERTY)) {
            defaultProperties.put(DUBBO_APPLICATION_NAME_PROPERTY, springApplicationName);
        }
    }

    private void setDubboConfigMultipleProperty(Environment environment, Map<String, Object> defaultProperties) {
        if (!environment.containsProperty(DUBBO_CONFIG_MULTIPLE_PROPERTY)) {
            defaultProperties.put(DUBBO_CONFIG_MULTIPLE_PROPERTY, Boolean.FALSE.toString());
        }
    }

    private void setDubboApplicationQosEnableProperty(Environment environment, Map<String, Object> defaultProperties) {
        if (!environment.containsProperty(DUBBO_APPLICATION_QOS_ENABLE_PROPERTY)) {
            defaultProperties.put(DUBBO_APPLICATION_QOS_ENABLE_PROPERTY, Boolean.FALSE.toString());
        }
    }

    private void setDubboProviderServerProperty(Environment environment, Map<String, Object> defaultProperties) {
        if (!environment.containsProperty(DUBBO_PROVIDER_SERVER)) {
            defaultProperties.put(DUBBO_PROVIDER_SERVER, "netty4");
        }
    }

    private void setDubboConsumerClientProperty(Environment environment, Map<String, Object> defaultProperties) {
        if (!environment.containsProperty(DUBBO_CONSUMER_CLIENT)) {
            defaultProperties.put(DUBBO_CONSUMER_CLIENT, "netty4");
        }
    }

    private void setDubboProtocolSerializationProperty(Environment environment, Map<String, Object> defaultProperties) {
        if (!environment.containsProperty(DUBBO_PROTOCOL_SERIALIZATION)) {
            defaultProperties.put(DUBBO_PROTOCOL_SERIALIZATION, "kryo");
        }
    }

    /**
     * Copy from BusEnvironmentPostProcessor#addOrReplace(MutablePropertySources, Map)
     *
     * @param propertySources {@link MutablePropertySources}
     * @param map             Default Dubbo Properties
     */
    private void addOrReplace(MutablePropertySources propertySources,
                              Map<String, Object> map) {
        MapPropertySource target = null;
        if (propertySources.contains(PROPERTY_SOURCE_NAME)) {
            PropertySource<?> source = propertySources.get(PROPERTY_SOURCE_NAME);
            if (source instanceof MapPropertySource) {
                target = (MapPropertySource) source;
                for (String key : map.keySet()) {
                    if (!target.containsProperty(key)) {
                        target.getSource().put(key, map.get(key));
                    }
                }
            }
        }
        if (target == null) {
            target = new MapPropertySource(PROPERTY_SOURCE_NAME, map);
        }
        if (!propertySources.contains(PROPERTY_SOURCE_NAME)) {
            propertySources.addLast(target);
        }
    }
}