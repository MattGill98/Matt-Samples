package uk.me.mattgill.samples.connector.extension.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface MqttOutbound {

    String url() default "tcp://test.mosquitto.org:1883";

    @Nonbinding
    QoS qos() default QoS.AT_LEAST_ONCE;

}