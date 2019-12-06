package de.derklaro.privateservers.cloudsystems;

import com.google.common.base.Preconditions;
import de.derklaro.privateservers.cloudsystems.helper.TypeHelper;
import de.derklaro.privateservers.cloudsystems.utility.CloudSystem;
import de.derklaro.privateservers.utility.CollectionUtils;

public final class CloudFinder {

    private CloudFinder() {
        throw new UnsupportedOperationException();
    }

    private static Class<? extends CloudSystem> findCloud(String cloudPrefix) {
        TypeHelper cloudTypes = CollectionUtils.filter(TypeHelper.values(),
                cloudType -> cloudType.getClazz().getSimpleName().equals(cloudPrefix));
        if (cloudTypes == null) {
            throw new IllegalStateException("Cannot find cloud system by given name");
        }

        if (!cloudTypes.isAvailable()) {
            throw new IllegalStateException("Cannot use this cloud type (\"" + cloudPrefix + "\"), not implemented yet");
        }

        return cloudTypes.getClazz();
    }

    public static CloudSystem findCloudSystem(String cloudPrefix) {
        if (cloudPrefix == null) {
            TypeHelper type = find();
            Preconditions.checkNotNull(type, "No cloud system detected on the current instance");

            try {
                return type.getClazz().newInstance();
            } catch (final InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        try {
            return findCloud(cloudPrefix).newInstance();
        } catch (final InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static TypeHelper find() {
        for (TypeHelper value : TypeHelper.values()) {
            try {
                Class.forName(value.getMarkClass());
                return value;
            } catch (final ClassNotFoundException ignored) {
            }
        }

        return null;
    }
}
