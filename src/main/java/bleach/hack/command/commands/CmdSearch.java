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
    public String getSyntax() { return "search | search add | search remove | search clear | search list"; }

    @Override
    public void onCommand(String command, String[] args) throws Exception {

        switch (args[0].toLowerCase()) {
            case "":
                BleachLogger.infoMessage(getSyntax());
                break;
            case "add":
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
                BleachLogger.infoMessage("Added " + args[1]);
                break;
            case "remove":
                List<String> lines = BleachFileMang.readFileLines("SearchBlocks.txt");
                if (!lines.contains(args[1])) {
                    BleachLogger.infoMessage("There is no such block in the block list!");
                    return;
                }
                lines.remove(args[1]);
                BleachFileMang.deleteFile("SearchBlocks.txt");
                BleachFileMang.createEmptyFile("SearchBlocks.txt");
                for (String s : lines) BleachFileMang.appendFile(s, "SearchBlocks.txt");
                BleachLogger.infoMessage("Removed " + args[1]);
                break;
            case "clear":
                BleachFileMang.deleteFile("SearchBlocks.txt");
                BleachFileMang.createEmptyFile("SearchBlocks.txt");
                BleachLogger.infoMessage("Cleared");
                break;
            case "list":
                List<String> blocks = BleachFileMang.readFileLines("SearchBlocks.txt");
                if (blocks.isEmpty()) {
                    BleachLogger.infoMessage("The block list is empty!");
                    return;
                }
                blocks.forEach(BleachLogger::infoMessage);
                break;
        }

    }
}
