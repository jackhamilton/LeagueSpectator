
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Jack
 */
public class infoRetriever {

    private static String summonername = "";
    private static String Server = "";
    private static String spectateInfo = "";
    private static String summonerID = "";
    
    public static String getSpectateInfo(String summonerName, String server) throws Exception {
        if (summonername.toLowerCase().equals(summonerName.toLowerCase()) && Server.toLowerCase().equals(server.toLowerCase()) && !spectateInfo.equals("")) {
            return spectateInfo;
        } else {
            summonerName = summonerName.replaceAll(" ", "%20");
            URL url = new URL("https://community-league-of-legends.p.mashape.com/api/v1.0/" + server + "/summoner/retrieveInProgressSpectatorGameInfo/" + summonerName);
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("X-Mashape-Authorization", "I6dDtJXtpzNWd19HW0kwuZ99jG7ZP12X");
            InputStream is = uc.getInputStream();
            String s = "";
            int i;
            while((i = is.read()) != -1) {
                s += (char)i;
            }
            summonername = summonerName;
            Server = server;
            summonerID = "";
            spectateInfo = s;
            return s;
        }
    } 
    
    public static String getSummonerIDByName(String summonerName, String server) throws Exception {
        if (summonername.toLowerCase().equals(summonerName.toLowerCase()) && Server.toLowerCase().equals(server.toLowerCase()) && !summonerID.equals("")) {
            return summonerID;
        } else {
            summonerName = summonerName.replaceAll(" ", "%20");
            URL url = new URL("https://community-league-of-legends.p.mashape.com/api/v1.0/" + server + "/summoner/getSummonerByName/" + summonerName);
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("X-Mashape-Authorization", "I6dDtJXtpzNWd19HW0kwuZ99jG7ZP12X");
            InputStream is = uc.getInputStream();
            String s = "";
            int i;
            while((i = is.read()) != -1) {
                s += (char)i;
            }
            if (!summonername.toLowerCase().equals(summonerName.toLowerCase()) || !Server.toLowerCase().equals(server.toLowerCase())) {
                summonername = summonerName;
                Server = server;
                summonerID = s.substring(s.indexOf("\"summonerId\": ") + 14, s.indexOf(",", s.indexOf("\"summonerId\": ") + 14));
                spectateInfo = "";
            } else {
                summonerID = s.substring(s.indexOf("\"summonerId\": ") + 14, s.indexOf(",", s.indexOf("\"summonerId\": ") + 14));
            }
            return s;
        }
    } 
    
    public static String getSummonerLeaguesByName(String summonerName, String server) throws Exception {
        getSummonerIDByName(summonerName, server);
        String id = summonerID;
        try {
            URL url = new URL("https://community-league-of-legends.p.mashape.com/api/v1.0/" + server + "/summoner/getLeagueForPlayer/" + id);
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("X-Mashape-Authorization", "I6dDtJXtpzNWd19HW0kwuZ99jG7ZP12X");
            InputStream is = uc.getInputStream();
            String toParse = "";
            int i;
            while((i = is.read()) != -1) {
                toParse += (char)i;
            }
            int solo5Index = toParse.indexOf("RANKED_SOLO_5x5");
            String solo5Tier = toParse.substring(toParse.indexOf("\"tier\": ", solo5Index) + 9, toParse.indexOf(",", toParse.indexOf("\"tier\": ", solo5Index) + 8) - 1);
            String solo5Rank = toParse.substring(toParse.indexOf("\"requestorsRank\": \"", solo5Index) + 19, toParse.indexOf(",", toParse.indexOf("\"requestorsRank\": \"", solo5Index) + 19) - 1);
            return solo5Tier + " " + solo5Rank;
        } catch (Exception e) {
            return "SEEDING";
        }
    }
    
    public static String generateSpectateCommand(String summonerName, String server) throws Exception {
        String spectateCommand = "\"Z:\\Program Files\\Riot Games\\League of Legends\\RADS\\solutions\\lol_game_client_sln\\releases\\0.0.1.3\\deploy\\League of Legends.exe\" \"8394\" \"LoLLauncher.exe\" \"\" \"spectator ";
        String toParse = getSpectateInfo(summonerName, server);
        String encryptionKey = toParse.substring(toParse.indexOf("\"observerEncryptionKey\": \"") + 26, toParse.indexOf(",", toParse.indexOf("\"observerEncryptionKey\": \"")) - 1);
        String serverIP = toParse.substring(toParse.indexOf("\"observerServerIp\": \"") + 21, toParse.indexOf(",", toParse.indexOf("\"observerServerIp\": \"")) - 1);
        String serverPort = toParse.substring(toParse.indexOf("\"observerServerPort\": ") + 22, toParse.indexOf(",", toParse.indexOf("\"observerServerPort\": ")));
        String gameID = toParse.substring(toParse.indexOf("\"gameId\": ") + 10, toParse.indexOf(",", toParse.indexOf("\"gameId\": ")));
        String platformID = "";
        switch(server.toLowerCase()) {
            case "na":
                platformID = "NA1";
                break;
            case "euw":
                platformID = "EUW1";
                break;
            case "eune":
                platformID = "EUN1";
                break;
            case "br":
                platformID = "BR1";
                break;
            case "tr":
                platformID = "TR1";
                break;
            case "ru":
                platformID = "RU";
                break;
            case "lan":
                platformID = "LA1";
                break;
            case "las":
                platformID = "LA2";
                break;
            case "oce":
                platformID = "OC1";
                break;
            default:
                System.err.println("Region not found.");
                System.exit(0);
                break;
                
        }
        spectateCommand += serverIP + ":" + serverPort + " " + encryptionKey + " " + gameID + " " + platformID + "\"";
        return spectateCommand;
    }
    
    public static boolean isPlayerInGame(String summonerName, String server) throws Exception {
        String toParse = getSpectateInfo(summonerName, server);
        if(toParse.substring(0, 28).equals("{\"success\": \"false\", \"error\"")) {
            return false;
        } else {
            return true;
        }
    }
    
    //0 - not in game, 1 - not yet started, 2 - in game
    public static int getGameStatus(String summonerName, String server) throws Exception {
        String toParse = getSpectateInfo(summonerName, server);
        if(toParse.lastIndexOf("Game has not started") != -1) {
            return 1;
        } else if (toParse.lastIndexOf("No Game for player") != -1){
            return 0;
        } else {
            return 2;
        }
        
    }
    
    public static boolean doesSummonerExist(String summonerName, String server) {
        try {
            String toParse = getSummonerIDByName(summonerName, server);
            if(toParse.substring(0, 28).equals("Invalid JSON Object:")) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.err.println("Unreported exception.");
            System.err.println(e.getMessage());
            return false;
        }
    }

}