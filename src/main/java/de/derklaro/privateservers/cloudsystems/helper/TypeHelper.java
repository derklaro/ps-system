package de.derklaro.privateservers.cloudsystems.helper;

import de.derklaro.privateservers.cloudsystems.cloudnet.v2.CloudNetV2;
import de.derklaro.privateservers.cloudsystems.cloudnet.v3.CloudNetV3;
import de.derklaro.privateservers.cloudsystems.reformcloud.v1.ReformCloudV1;
import de.derklaro.privateservers.cloudsystems.reformcloud.v2.ReformCloudV2;
import de.derklaro.privateservers.cloudsystems.utility.CloudSystem;

import java.io.Serializable;

public enum TypeHelper implements Serializable {

    CLOUDNET2(CloudNetV2.class, "de.dytanic.cloudnet.api.CloudAPI", true),

    CLOUDNET3(CloudNetV3.class, "de.dytanic.cloudnet.driver.CloudNetDriver", true),

    REFORMCLOUD(ReformCloudV1.class, "systems.reformcloud.ReformCloudAPISpigot", true),

    REFORMCLOUD_2(ReformCloudV2.class, "systems.reformcloud.reformcloud2.executor.api.common.ExecutorAPI", true),

    TIMOCLOUD(null, null, false),

    CAVECLOUD(null, null, false);

    TypeHelper(Class<? extends CloudSystem> clazz, String markClass, boolean independent) {
        this.clazz = clazz;
        this.markClass = markClass;
        this.independent = independent;
    }

    private final boolean independent;

    private final String markClass;

    private final Class<? extends CloudSystem> clazz;

    public Class<? extends CloudSystem> getClazz() {
        return clazz;
    }

    public String getMarkClass() {
        return markClass;
    }

    public boolean isAvailable() {
        return independent;
    }
}
