package im.mctop.bot;

import im.mctop.bot.vk_api.ApiRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author enelar
 */
public class WallMonitor {
    private List<WallMessage> messages = new ArrayList<>();
    private String group;
    private Integer min_wall_delay;
    
    public WallMonitor( String _group ) {
        group = _group;
    }
    
    public WallMessage[] LoadNewMessages( ApiRequest api ) {
        WallMessage []res = api.GetWall(group, 0, 100, "others");
        int i = res.length - 1;
        for (; i >= 0; i--)
            if (!IsMessageKnown(res[i].id, 100))
                break;
        int copy = i;
        for (; i >= 0; i--)
            messages.add(res[i]);
        return Arrays.copyOfRange(res, 0, copy);
    }    
    
    private Boolean IsMessageKnown( int id, int max_offset ) {
        for (int i = 0; i < messages.size() && i < max_offset; i++)
          if (messages.get(i).id == id)
              return true;
        return false;            
    }
    
    public Integer[] SearchFlooder( WallMessage[] m ) {
        SortedMap<Integer, Boolean> ret = new TreeMap<Integer, Boolean>();
        for (int i = 0; i < m.length; i++) {
            WallMessage cur = m[i];
            int next_id = SearchMessageByOwner(cur.id, 1);
            if (next_id == -1)
                continue;
            WallMessage next = messages.get(next_id);
            if (cur.date - next.date < min_wall_delay)
              ret.put(cur.author, Boolean.TRUE);
        }
        return (Integer[]) ret.keySet().toArray();            
    }
    
    private int SearchMessageByOwner( int owner, int offset ) {
        for (int i = 0; i < messages.size(); i++)
            if (messages.get(i).owner == owner)
                if (offset-- == 0)
                    return i;
        return -1;
    }
            
}
