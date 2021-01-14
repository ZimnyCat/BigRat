package bleach.hack.command.commands;

import bleach.hack.command.Command;
import bleach.hack.utils.BleachLogger;

public class CmdFakeText extends Command {
    @Override
    public String getAlias() { return "faketext"; }

    @Override
    public String getDescription() { return "Sends a fake clientside message in chat"; }

    @Override
    public String getSyntax() { return "faketext [Text]"; }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        String msg = "";
        for (int nigga = 0; nigga < args.length; nigga++) msg += args[nigga].replace("&", "\u00A7") + " ";
        BleachLogger.noPrefixMessage(msg);
    }
}
