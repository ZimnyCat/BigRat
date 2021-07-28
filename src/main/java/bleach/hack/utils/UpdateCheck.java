package bleach.hack.utils;

import bleach.hack.BleachHack;
import bleach.hack.event.events.EventReadPacket;
import bleach.hack.bleacheventbus.BleachSubscribe;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.util.Formatting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class UpdateCheck {

    @BleachSubscribe
    public void gameJoin(EventReadPacket e) {
        if (!(e.getPacket() instanceof GameJoinS2CPacket) || BleachHack.NAME != "BigRat") return;

        String latestVer = "";
        try {
            URL url = new URL("https://raw.githubusercontent.com/ZimnyCat/bigrat-site/main/latest.txt");
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = (HttpURLConnection) urlConnection;
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String currentLine;
            while ((currentLine = in.readLine()) != null) {
                latestVer += currentLine;
            }
        } catch (Exception ignored) { }
        if (!latestVer.equals(BleachHack.VERSION)) BleachLogger.infoMessage("You are running an outdated version of BigRat! Download the latest version on "
                + Formatting.DARK_AQUA + "https://bigrat.site/");
    }

}
