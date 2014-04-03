package cz.sio2.crowler.configurations.npu;

import java.util.Map;

public class MonumnetConfiguration {

    public final static String MONUMNET_PREFIX = "monumnet-";
    public final static String MONUMNET_URL = "http://monumnet.npu.cz/";

    protected Integer loadInteger(final String name, final Map<String,String> properties, final int defaultValue) {
        final String sValue = properties.get(name);
        int value = (sValue == null ? defaultValue : Integer.parseInt(sValue));
        return value;
    }
}
