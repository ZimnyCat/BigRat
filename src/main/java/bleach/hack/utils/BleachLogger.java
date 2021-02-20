/*
 * This file is part of the BleachHack distribution (https://github.com/BleachDrinker420/bleachhack-1.14/).
 * Copyright (c) 2019 Bleach.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bleach.hack.utils;

import bleach.hack.BleachHack;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.MessageTime;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BleachLogger {

    public static void infoMessage(String s) {
        try {
            MinecraftClient.getInstance().inGameHud.getChatHud()
                    .addMessage(new LiteralText(getBHText(Formatting.GRAY) + " " + s));
        } catch (Exception e) {
            System.out.println("[" + BleachHack.NAME + "] INFO: " + s);
        }
    }

    public static void warningMessage(String s) {
        try {
            MinecraftClient.getInstance().inGameHud.getChatHud()
                    .addMessage(new LiteralText(getBHText(Formatting.GRAY) + " " + s));
        } catch (Exception e) {
            System.out.println("[" + BleachHack.NAME + "] WARN: " + s);
        }
    }

    public static void errorMessage(String s) {
        try {
            MinecraftClient.getInstance().inGameHud.getChatHud()
                    .addMessage(new LiteralText(getBHText(Formatting.GRAY) + " " + s));
        } catch (Exception e) {
            System.out.println("[" + BleachHack.NAME + "] ERROR: " + s);
        }
    }

    public static void noPrefixMessage(String s) {
        try {
            Module mt = ModuleManager.getModule(MessageTime.class);
            String time = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("HH:mm" + (mt.getSetting(0).asToggle().state ? ":ss" : "")));
            String msg = (mt.isToggled() ? "\u00a73[\u00a7f" + time + "\u00a73] \u00a7f" : "") + s;
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText(msg));
        } catch (Exception e) {
            System.out.println(s);
        }
    }

    public static void noPrefixMessage(Text text) {
        try {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
        } catch (Exception e) {
            System.out.println(text.asString());
        }
    }

    public static void actionBarMessage(String s) {
        try {
            MinecraftClient.getInstance().player.sendMessage(new LiteralText(getBHText(Formatting.GRAY) + " " + s),
                    true);
        } catch (Exception e) {
            System.out.println("[" + BleachHack.NAME + "] INFO: " + s);
        }
    }

    private static String getBHText(Formatting color) {
        return color + "\u00a7f[\u00A73" + BleachHack.NAME + "\u00a7f]";
    }
}
