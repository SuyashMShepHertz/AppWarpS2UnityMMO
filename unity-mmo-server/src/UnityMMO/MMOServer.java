/**
 *
 * @author Suyash Mohan
 */
package UnityMMO;

import com.shephertz.app42.server.idomain.BaseServerAdaptor;
import com.shephertz.app42.server.idomain.IZone;

public class MMOServer extends BaseServerAdaptor{
    
    @Override
    public void onZoneCreated(IZone zone)
    {             
        System.out.println("Zone Created " + zone.getName());
        zone.setAdaptor(new MMOZone());
        /*for(IRoom iroom:zone.getRooms()){
            iroom.setAdaptor(new RoomAdaptor(iroom));
        }*/
        
    }
}
