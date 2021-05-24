package bleach.hack.command.commands;

import bleach.hack.command.Command;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.file.BleachFileMang;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class CmdSearch extends Command {

    @Override
    public String getAlias() { return "search"; }

    @Override
    public String getDescription() { return "Manages Search blocks"; }

    @Override
    public String getSyntax() { return "search | search add | search remove | search clear"; }

    @Override
    public void onCommand(String command, String[] args) throws Exception {

        if (args[0].equalsIgnoreCase("")) BleachLogger.infoMessage(getSyntax());
        else if (args[0].equalsIgnoreCase("add")) {
            // mojang moment
            if (Registry.BLOCK.get(new Identifier(args[1])) == Blocks.AIR) {
                BleachLogger.infoMessage("Invalid block!");
                return;
            }
            for (String line : BleachFileMang.readFileLines("SearchBlocks.txt")) {
                if (line.equalsIgnoreCase(args[1])) {
                    BleachLogger.errorMessage(args[1] + " has already been added");
                    return;
                }
            }
            BleachFileMang.appendFile(args[1], "SearchBlocks.txt");
        }
        else if (args[0].equalsIgnoreCase("remove")) {
            List<String> lines = BleachFileMang.readFileLines("SearchBlocks.txt");
            if (lines.isEmpty()) return;
            lines.remove(args[1]);
            BleachFileMang.deleteFile("SearchBlocks.txt");
            BleachFileMang.createEmptyFile("SearchBlocks.txt");
            for (String s : lines) BleachFileMang.appendFile(s, "SearchBlocks.txt");
        }
        else if (args[0].equalsIgnoreCase("clear")) {
            BleachFileMang.deleteFile("SearchBlocks.txt");
            BleachFileMang.createEmptyFile("SearchBlocks.txt");
        }
    }
}
