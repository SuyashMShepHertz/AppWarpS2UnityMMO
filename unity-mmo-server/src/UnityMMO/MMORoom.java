/**
 *
 * @author Suyash Mohan
 */
package UnityMMO;

import MMO.Player;
import MMO.VectorF;
import MMO.VectorI;
import MMO.World;
import com.shephertz.app42.server.idomain.BaseRoomAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class MMORoom extends BaseRoomAdaptor{
    
    private IRoom m_room;
    private World m_world;

    public MMORoom(IRoom room) {
        m_room = room;
        m_world = new World(m_room, new VectorF(-100,-100), new VectorF(100,100), new VectorI(100,100));
    }

    @Override
    public void handleUserJoinRequest(IUser user, HandlingResult result){
        System.out.println("User Joined " + user.getName());
    }
    
    @Override
    public void onUserLeaveRequest(IUser user){
        Player p = m_world.getPlayer(user);
        m_world.removePlayer(p);
    }

    @Override
    public void handleChatRequest(IUser sender, String message, HandlingResult result)
    {
        //System.out.println(sender.getName() + " says " + message);
    }
    
    @Override
    public void handleUpdatePeersRequest(IUser sender, byte[] update, HandlingResult result) {
        ByteBuffer buffer = ByteBuffer.wrap(update).order(ByteOrder.BIG_ENDIAN);
        float x = buffer.getFloat();
        float y = buffer.getFloat();
        float z = buffer.getFloat();
        float rx = buffer.getFloat();
        float ry = buffer.getFloat();
        float rz = buffer.getFloat();
        
        String name = "";
        while(buffer.remaining() > 0)
        {
            name += buffer.getChar();
        }
        
        //System.out.println(name + " : " + x + "x"+ y + "x" + z + " : " + rx + "x"+ ry + "x" + rz);
        //System.out.format("%fx%fx%f - %fx%fx%f", x,y,z,rx,ry,rz);
        
        result.sendNotification = false;
        
        List<Player> removedForPlayers = new ArrayList<>();
        List<Player> removedPlayers = new ArrayList<>();
        List<Player> addedPlayers = new ArrayList<>();
        
        Player p = m_world.getPlayer(sender);
        if(p == null)
        {
            p = new Player(sender, x, z);
            p.setInterestAreaSize(20.0f, 20.0f);
            m_world.addPlayer(p);
        }
        else
        {
            p.setPosition(x, z);
            m_world.updatePlayer(p, removedForPlayers, removedPlayers, addedPlayers);
        }
        
        m_world.multicastMessage(p, update);
        m_world.multicastMessage(p, removedForPlayers, buildLeaveRegionMsg(p.getName()));
        for(Player rp:removedPlayers)
        {
            m_world.sendMessage(p, buildLeaveRegionMsg(rp.getName()), rp);
        }
        for(Player ap:addedPlayers)
        {
            m_world.sendMessage(p, buildMovePlayerMsg(ap));
        }
    }

    @Override
    public void onTimerTick(long time) {
        
    }

    private String buildLeaveRegionMsg(String userName)
    {
        JSONObject tobeSent = new JSONObject();
        try
        {
            tobeSent.put("name", userName);
            tobeSent.put("type", 2);
        }
        catch(JSONException ex)
        {
        }

        return tobeSent.toString();
    }    
    
    private byte[] buildMovePlayerMsg(Player p)
    {
        int floatSize = 4;
        int charSize = 2;
        int data_len = 6*floatSize + p.getName().length()*charSize;
        ByteBuffer data = ByteBuffer.allocate(data_len);
        data.putFloat(p.getPosition().x);
        data.putFloat(0.0f);
        data.putFloat(p.getPosition().y);
        data.putFloat(0.0f);
        data.putFloat(0.0f);
        data.putFloat(0.0f);
        
        for(char c: p.getName().toCharArray())
        {
            data.putChar(c);
        }
        
        data.order(ByteOrder.BIG_ENDIAN);
        
        return data.array();
    }    
}
