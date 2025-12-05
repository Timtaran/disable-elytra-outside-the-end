package io.github.timtaran.deote.commands;

import dev.isxander.yacl3.config.v2.api.SerialEntry;
import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import io.github.timtaran.deote.config.DeoteConfig;
import io.github.timtaran.deote.platform.PlatformEntrypoint;
import io.github.timtaran.deote.util.TextUtils;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public final class ConfigProvider {
    private static final Map<String, Field> PARAMS = scanParams();

    private static DeoteConfig config;

    public static void setConfigInstance(DeoteConfig cfg) {
        config = cfg;
    }

    /**
     * Retrieves the current instance of the DeoteConfig class.
     * This method should be used only when you need to work with List param.
     * Otherwise, use {@link ConfigProvider#getFieldByName(String)} and {@link ConfigProvider#setParamValue(MinecraftServer, String, Object)}.
     *
     * @return the current DeoteConfig instance
     */
    public static DeoteConfig getConfigInstance() {
        return config;
    }

    private ConfigProvider() {
    }

    private static Map<String, Field> scanParams() {
        Map<String, Field> map = new LinkedHashMap<>();
        for (Field f : DeoteConfig.class.getDeclaredFields()) {
            if (f.isAnnotationPresent(SerialEntry.class) && !List.class.isAssignableFrom(f.getType())) {
                map.put(f.getName(), f);
            }
        }
        return map;
    }

    public static Collection<String> getAllParams() {
        return new ArrayList<>(PARAMS.keySet());
    }

    public static Collection<String> getParamValues(String param) {
        if (param == null) return Collections.emptyList();
        Field field = getFieldByName(param);
        if (field == null) return Collections.emptyList();

        Class<?> type = field.getType();
        if (type.isEnum()) {
            return Arrays.stream(type.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        if (type == boolean.class || type == Boolean.class) {
            return List.of("true", "false");
        }
        if (type == String.class) {
            try {
                Object value = field.get(config);
                if (value != null) {
                    return List.of(value.toString().replace('§', '&'));
                }
            } catch (IllegalAccessException e) {
                DisableElytraOutsideTheEnd.LOGGER.error(e.getMessage());
            }
        }
        return List.of();
    }

    private static Object convertValue(Object value, Class<?> targetType) {
        if (value == null) return null;

        if (targetType == String.class) {
            return TextUtils.formatColors(value.toString());
        }

        if (targetType.isInstance(value)) {
            return value;
        }

        if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value.toString());
        }
        if (targetType == long.class || targetType == Long.class) {
            return Long.parseLong(value.toString());
        }
        if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value.toString());
        }
        if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value.toString());
        }

        if (targetType.isEnum()) {
            // Iterate through the enum constants to find the match
            for (Object constant : targetType.getEnumConstants()) {
                if (constant.toString().equals(value.toString())) {
                    return constant;
                }
            }
            throw new IllegalArgumentException("No enum constant " + targetType.getCanonicalName() + "." + value);
        }

        throw new IllegalArgumentException(
                "Unsupported type: " + targetType.getName()
        );
    }

    public static void setParamValue(MinecraftServer server, String param, Object value)
            throws IllegalAccessException {

        Field field = getFieldByName(param);
        if (field == null) {
            throw new IllegalArgumentException("Unknown param: " + param);
        }

        Object convertedValue = convertValue(value, field.getType());
        field.set(config, convertedValue);
        DisableElytraOutsideTheEnd.LOGGER.debug(convertedValue.toString());
        DisableElytraOutsideTheEnd.LOGGER.debug(convertedValue.getClass().getTypeName());
        save();
        reload(server);
    }

    public static void save() {
        DeoteConfig.save();
    }

    public static void reload(MinecraftServer server) {
        DeoteConfig.load();
        PlatformEntrypoint.resendConfig(server);
    }

    public static Field getFieldByName(String paramName) {
        return PARAMS.get(paramName);
    }
}
