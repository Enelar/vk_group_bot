package im.mctop.bot;

import im.mctop.bot.vk_api.ApiRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author enelar
 */
public class WallMonitor {
    private List<WallMessage> messages = new ArrayList<>();
    private String group;
    
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
            
}
