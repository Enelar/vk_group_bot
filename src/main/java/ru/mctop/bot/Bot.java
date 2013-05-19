package ru.mctop.bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class Bot {

    public static void main(String[] args) {
        Bot b = new Bot();
        b.UserStats = new HashMap<Integer, Integer>();
        b.ToDelete = new HashMap<Integer, Integer>();
        b.Workflow();
    }
    Map<Integer, Integer> UserStats;
    Map<Integer, Integer> ToDelete;
    static final int count = 100;

    public void Workflow() {
        int offset = 70000, deleted = 0;
        int n = 0;
        while (true) {
            deleted += SearchAndDelete(offset - deleted, count);
            offset += count;
            System.out.print("Offset " + offset + ", Deleted " + deleted + "\n");
            //n++;
            if (n > 10) {
                SearchAndDelete(0, count);
                n = 0;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            //MakeBan();
        }
    }

    public void MakeBan() {
        ApiRequest p = new ApiRequest();
        for (Map.Entry<Integer, Integer> entry : UserStats.entrySet()) {
            Integer val = entry.getValue();
            if (val > 10) {
                System.out.print("Ban " + entry.getKey() + " for " + val + "\n");
                p.Ban(23893530, entry.getKey(), p.MakeAuth());
                if (val > count / 10) {
                    ToDelete.put(entry.getKey(), 1);
                }
                val -= count / 3;
            }
            val -= count / 10 + 1;
            if (val < 0) {
                val = 0;
            }
            UserStats.put(entry.getKey(), val);
        }
    }

    public int SearchAndDelete(int offset, int count) {
        ApiRequest p = new ApiRequest();

        String auth = p.MakeAuth();
        WallMessage[] a = p.GetWall("mctop", offset, count, "others");
        ArrayList<WallMessage> ids = new ArrayList<WallMessage>();
        int current_user = 0, combo = 0;
        int bad = 0;
        for (int i = 0; i < a.length; ++i) {
            WallMessage wm = a[i];
            assert wm != null;
            assert wm.text != null;
            if (wm == null) {
                continue;
            }
            /*
             if (!UserStats.containsKey(wm.author))
             UserStats.put(wm.author, 1);
             else
             UserStats.put(wm.author, 
             UserStats.get(wm.author) + 1);            
             */
            if (current_user != wm.author) {
                if (combo > 1) {
                    p.Ban(23893530, current_user, p.MakeAuth());
                    for (int ii = 0; ii < ids.size(); ii++) {
                        p.DeleteWallMessage(wm.owner, ids.get(ii).id, auth);
                    }
                    bad += ids.size();
                    System.out.print("Ban " + current_user + " spam " + combo + "\n");
                }
                ids.clear();
                combo = 0;
                current_user = wm.author;
            }
            //combo++;
            if (true || IsBad(wm.text) || ToDelete.containsKey(wm.author)) {
                if (p.DeleteWallMessage(wm.owner, wm.id, auth)) {
                    bad++;
                }
            } else {
                ids.add(wm);
            }

        }
        return bad;
    }
    public ArrayList<String> bad_words;

    public ArrayList<String> GetBadWords() {
        if (bad_words != null) {
            return bad_words;
        }
        bad_words = new ArrayList<String>();
        bad_words.add("слот");
        bad_words.add("спавн");
        bad_words.add("админ");
        bad_words.add("aдекватн");
        bad_words.add("плагин");
        bad_words.add("верс");
        bad_words.add(":255");
        bad_words.add("заход");
        bad_words.add("lendytubbies");
        bad_words.add("сервер");
        bad_words.add("клиент");
        bad_words.add("кит");
        return bad_words;
    }

    public Boolean IsBad(String a) {
        ArrayList<String> r = GetBadWords();
        a = a.toLowerCase();
        for (int i = 0; i < r.size(); ++i) {
            if (a.contains(r.get(i))) {
                return true;
            }
        }
        return false;
    }
}
