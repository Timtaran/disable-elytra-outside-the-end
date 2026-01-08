/*
 * This file is part of Disable Elytra Outside The End.
 * Licensed under LGPL 3.0.
 *
 * Copyright (c) 2025 timtaran
 */
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

/**
 * A provider class for accessing and modifying DeoteConfig parameters.
 *
 * @author timtaran
 */
public final class ConfigProvider {
    private static final Map<String, Field> PARAMS = scanParams();

    private static DeoteConfig config;

    private ConfigProvider() {
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

    /**
     * Sets the current instance of the DeoteConfig class.
     *
     * @param cfg the DeoteConfig instance to set
     */
    public static void setConfigInstance(DeoteConfig cfg) {
        config = cfg;
    }

    /**
     * Scans the DeoteConfig class for fields annotated with @SerialEntry
     * and returns a map of parameter names to their corresponding Field objects.
     *
     * @return a map of parameter names to Field objects
     */
    private static Map<String, Field> scanParams() {
        Map<String, Field> map = new LinkedHashMap<>();
        for (Field f : DeoteConfig.class.getDeclaredFields()) {
            if (f.isAnnotationPresent(SerialEntry.class) && !List.class.isAssignableFrom(f.getType())) {
                map.put(f.getName(), f);
            }
        }
        return map;
    }

    /**
     * Retrieves a collection of all parameter names in the DeoteConfig class.
     *
     * @return a collection of parameter names
     */
    public static Collection<String> getAllParams() {
        return new ArrayList<>(PARAMS.keySet());
    }

    /**
     * Retrieves possible values for a given parameter in the DeoteConfig class.
     *
     * @param param the name of the parameter
     * @return a collection of possible values for the parameter
     */
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

    /**
     * Sets the value of a given parameter in the DeoteConfig class.
     *
     * @param server the Minecraft server instance
     * @param param  the name of the parameter to set
     * @param value  the new value for the parameter
     * @throws IllegalAccessException if the field cannot be accessed
     */
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

    /**
     * Saves the current DeoteConfig instance to persistent storage.
     */
    public static void save() {
        DeoteConfig.save();
    }

    /**
     * Reloads the DeoteConfig from persistent storage and
     * resends the updated configuration to all players on the server.
     *
     * @param server the Minecraft server instance
     */
    public static void reload(MinecraftServer server) {
        DeoteConfig.load();
        PlatformEntrypoint.resendConfig(server);
    }

    /**
     * Retrieves the Field object for a given parameter name in the DeoteConfig class.
     *
     * @param paramName the name of the parameter
     * @return the Field object for the parameter, or null if not found
     */
    public static Field getFieldByName(String paramName) {
        return PARAMS.get(paramName);
    }
}
