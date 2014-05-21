
import java.util.Arrays;
import static java.util.Collections.list;



/**
 * @author Jack
 */
public class main implements Runnable {
    
    //public static void main(String[] args) throws Exception {
        /*
        infoRetriever ret = new infoRetriever();
        if (ret.isPlayerInGame("PentaPes", "eune")) {
            String output = ret.generateSpectateCommand("PentaPes", "eune");
            Robot r = new Robot();
            StringSelection selection = new StringSelection(output);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            r.keyPress(KeyEvent.VK_WINDOWS);
            r.keyPress(KeyEvent.VK_R);
            r.keyRelease(KeyEvent.VK_WINDOWS);
            r.keyRelease(KeyEvent.VK_R);
            Thread.sleep(100);
            r.keyPress(KeyEvent.VK_CONTROL);
            r.keyPress(KeyEvent.VK_V);
            r.keyRelease(KeyEvent.VK_CONTROL);
            r.keyRelease(KeyEvent.VK_V);
            r.keyPress(KeyEvent.VK_ENTER);
            r.keyRelease(KeyEvent.VK_ENTER);
        } else {
            System.out.println("Summoner not in game.");
        }
        */
    //}
    
    public void run() {
        while (true) {
            try {
                for(int x = 0; x < gui.model.size(); x++) {
                    String s = gui.model.getElementAt(x).toString();
                    infoRetriever.getGameStatus(s.substring(0, s.lastIndexOf("(") - 1), s.substring(s.lastIndexOf("(") + 1, s.lastIndexOf(")")));
                }
                Thread.sleep(60000);
            } catch (Exception e) {
                System.err.println(e.getCause().toString());
            }
        }
    }

}