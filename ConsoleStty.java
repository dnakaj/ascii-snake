// Source: https://www.darkcoding.net/software/non-blocking-console-io-is-not-possible/
import java.io.*;


public class ConsoleStty {

    private static String ttyConfig;

    public static String getConfig() {
        return ttyConfig;
    }

    public static void setTerminalToCBreak() throws IOException, InterruptedException {

        ttyConfig = stty("-g");

        // set the console to be character-buffered instead of line-buffered
        stty("-icanon min 1");

        // disable character echoing
        stty("-echo");
    }

    /**
    *  Execute the stty command with the specified arguments
    *  against the current active terminal.
    */
    public static String stty(final String args)
    throws IOException, InterruptedException {
        String cmd = "stty " + args + " < /dev/tty";

        return exec(new String[] {
            "sh",
            "-c",
            cmd
        });
    }

    /**
    *  Execute the specified command and return the output
    *  (both stdout and stderr).
    */
    private static String exec(final String[] cmd)
    throws IOException, InterruptedException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        Process p = Runtime.getRuntime().exec(cmd);
        int c;
        InputStream in = p.getInputStream();

        while ((c = in.read()) != -1) {
            bout.write(c);
        }

        in = p.getErrorStream();

        while ((c = in.read()) != -1) {
            bout.write(c);
        }

        p.waitFor();

        String result = new String(bout.toByteArray());
        return result;
    }

}
