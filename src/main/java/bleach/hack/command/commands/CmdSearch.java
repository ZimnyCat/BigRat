package bleach.hack.command.commands;

import bleach.hack.command.Command;
import bleach.hack.utils.BleachLogger;
import bleach.hack.utils.file.BleachFileMang;

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
            try {
                BleachFileMang.appendFile(args[1], "SearchBlocks.txt");
            } catch (Exception e) {
                BleachLogger.infoMessage("Invalid block name!");
            }
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
