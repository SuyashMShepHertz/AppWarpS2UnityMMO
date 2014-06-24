/**
 *
 * @author Suyash Mohan
 */
package UnityMMO;

import com.shephertz.app42.server.idomain.BaseZoneAdaptor;
import com.shephertz.app42.server.idomain.HandlingResult;
import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;
import java.util.HashMap;

public class MMOZone extends BaseZoneAdaptor {
    
    private HashMap<IRoom, MMORoom> mmo_rooms;
    
    public MMOZone()
    {
        mmo_rooms = new HashMap<>();
    }
    
    @Override
    public void onAdminRoomAdded(IRoom room)
    {
        System.out.println("Room Creatd " + room.getName());
        MMORoom mRoom = new MMORoom(room);
        room.setAdaptor(mRoom);
        mmo_rooms.put(room, mRoom);
    } 
    
    @Override
    public void handleAddUserRequest(IUser user, String authString, HandlingResult result)
    {
        System.out.println("UserRequest " + user.getName());
    }   
    
    @Override
    public void onUserRemoved(IUser user) {
        System.out.println("User Removed " + user.getName());
        mmo_rooms.get(user.getLocation()).onUserLeaveRequest(user);
    }
}
