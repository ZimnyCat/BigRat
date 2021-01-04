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
package bleach.hack.command.commands;

import bleach.hack.BleachHack;
import bleach.hack.command.Command;
import bleach.hack.utils.BleachLogger;

import java.util.Arrays;
import java.util.List;

public class CmdCredits extends Command {

    @Override
    public String getAlias() {
        return "credits";
    }

    @Override
    public String getDescription() {
        return "credits to people";
    }

    @Override
    public String getSyntax() {
        return "credits";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        String cmd = null;
        try {
            cmd = args[0];
        } catch (Exception e) {
        }
        List<String> credits = Arrays.asList(
                "\u00A73-=\u00A7f+\u00A73=- " + BleachHack.CLIENT + " credits -=\u00A7f+\u00A73=-",
                "\u00A73bleach \u00A7fmade the base and like 60% of the modules",
                "\u00A73epearl \u00A7fadded ~30 modules and changed client's UI",
                "\u00a73ZimnyCat \u00A7fmade this skid of skid"
        );
        for (String s : credits) BleachLogger.noPrefixMessage(s);
    }

}
