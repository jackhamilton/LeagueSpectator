
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * @author Jack
 */
public class iconListRenderer extends DefaultListCellRenderer {
        
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        String toParse = value.toString();
        Icon icon = getStatusIcon(toParse.substring(0, toParse.lastIndexOf("(") - 1), toParse.substring(toParse.lastIndexOf("(") + 1, toParse.lastIndexOf(")")));
        label.setIcon(icon);
        return label;
    }
    
    public ImageIcon getStatusIcon(String summonername, String server) {
        try {
            switch(infoRetriever.getGameStatus(summonername, server)) {
                case 0:
                    return new ImageIcon(getClass().getResource("/img/status/offline.png"));
                case 1:
                    return new ImageIcon(getClass().getResource("/img/status/pending.png"));
                case 2:
                    return new ImageIcon(getClass().getResource("/img/status/ingame.png"));
                default:
                    return new ImageIcon(getClass().getResource("/img/status/offline.png"));
            }
        } catch (Exception e) {
            return new ImageIcon(getClass().getResource("/img/status/offline.png"));
        }
    }

}