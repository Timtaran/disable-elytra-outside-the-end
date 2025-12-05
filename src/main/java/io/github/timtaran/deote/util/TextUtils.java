package io.github.timtaran.deote.util;


import io.github.timtaran.deote.DisableElytraOutsideTheEnd;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextUtils {
    public static Component translatable(String key) {
        return Component.translatable(DisableElytraOutsideTheEnd.MOD_ID + "." + key);
    }

    public static String formatColors(String input) {
        return input.replaceAll("&([0-9a-fk-or])", "§$1");
    }

    public static String listToString(List<String> list) {
        return list == null ? "" : String.join(",", list);
    }

    public static ArrayList<String> stringToList(String s) {
        if (s == null || s.isBlank()) return new ArrayList<>();
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(x -> !x.isEmpty()).collect(Collectors.toCollection(ArrayList::new));
    }

    public static Style styleCommandText(Style style, String commandText, String shownText) {
        //? if <1.21.7 {
        return style.withClickEvent(
                        new ClickEvent(
                                ClickEvent.Action.SUGGEST_COMMAND,
                                commandText
                        ))
                .withHoverEvent(
                        new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Component.literal(shownText))
                );
        //?} else {
        /*return style.withClickEvent(
                        new ClickEvent.SuggestCommand(
                                commandText
                        ))
                .withHoverEvent(
                        new HoverEvent.ShowText(
                                Component.literal(shownText)));
        *///?}
    }
}
