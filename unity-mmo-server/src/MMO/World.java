package MMO;

import com.shephertz.app42.server.idomain.IRoom;
import com.shephertz.app42.server.idomain.IUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Suyash Mohan
 */
public class World {
    //Reference to IRoom
    private IRoom m_room;
    //2D List of all regions
    private Region m_regions[][];
    //Map of IUser and Player
    private HashMap<IUser,Player> m_players;
    //TopLeft Corner of World
    private VectorF m_tl;
    //BottomRight Corner of World
    private VectorF m_br;
    //Number of regions the world is divided into
    private VectorI m_subdivisions;
    //Size of one division
    private VectorF m_divisionArea;
    
    /*
     * Initialises the World
     * @param room          IRoom to which the world is attached to
     * @param TopLeft       This represents the coordinates of Top Left point
     * @param BottomRight   This represents the coordinates of Bottom Right point
     * @param SubDivisions  This is the nummber of regions the world has to be divided into
     */
    public World(IRoom room,VectorF TopLeft, VectorF BottomRight, VectorI SubDivisions)
    {
        m_room = room;
        m_tl = TopLeft;
        m_br = BottomRight;
        m_subdivisions = SubDivisions;
        m_regions = new Region[(int)m_subdivisions.x][(int)m_subdivisions.y];
        m_players = new HashMap<>();
        
        m_divisionArea = new VectorF();
        m_divisionArea.x = Math.abs((m_br.x - m_tl.x) / m_subdivisions.x); 
        m_divisionArea.y = Math.abs((m_tl.y - m_br.y) / m_subdivisions.y);
        
        int i = 0, j;
        for(float x = m_tl.x; x < m_br.x; x+=m_divisionArea.x)
        {
            j = 0;
            for(float y = m_tl.y; y < m_br.y; y+=m_divisionArea.y)
            {
                Region rgn = new Region(x, y, m_divisionArea.x, m_divisionArea.y);
                m_regions[i][j] = rgn;
                j = j + 1;
            }
            i = i + 1;
        }
    }
    
    /*
     * Returns a region that contains the (x,y) point
     * @param x
     * @param y
     */
    public Region findRegion(float x, float y)
    {
        int i = (int)Math.floor((x - m_tl.x)/m_divisionArea.x);
        int j = (int)Math.floor((y - m_tl.y)/m_divisionArea.y);
        
        return m_regions[i][j];
    }
    
    /*
     * Adds a player to the world
     * @param p Player that is being added to world
     */
    public void addPlayer(Player p)
    {
        m_players.put(p.getIUser(), p);
        updatePlayer(p,null,null,null);
    } 
    
    /*
     * Removes a player from the world
     * @param p Player that has to be removed
     */
    public void removePlayer(Player p)
    {
        try
        {
            Region curRgn = p.getRegion();
            if(curRgn != null)
            {
                curRgn.RemovePlayer(p);
            }
            for(Region rgn:p.getPublishers())
            {
                rgn.RemoveSubscriber(p);
            }
            m_players.remove(p.getIUser());
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
    }
    
    /*
     * Get the player reference from the IUser reference
     * @param user IUser reference to the user
     */
    public Player getPlayer(IUser user)
    {
        if(m_players.containsKey(user) == true)
            return m_players.get(user);
        
        return null;
    }
    
    /*
     * Updates the player instance in the world
     * @param p                 Player that has to be updated in the world
     * @param removedForPlayers Reference to the list, which will contain the list of all players from whom's interest area the Player p has been removed
     * @param removedPlayer     Reference to the list, which will contain the list of all player that has left the player p's interest area
     * @param addedPlayer       Reference to the list, which will contain the list of all player that has entered the player p's interest area
     * @return void
     */
    public void updatePlayer(Player p, List<Player> removedForPlayers, List<Player> removedPlayers, List<Player> addedPlayers)
    {
        try
        {
            List<Region> rgns = new ArrayList<>(p.getPublishers());
            List<Region> rgns_copy = new ArrayList<>(rgns);
            
            p.getPublishers().clear();
            updateInterestRegions(p);
            
            Region newRgn = findRegion(p.getPosition().x, p.getPosition().y);
            Region prevRgn = p.getRegion();
            p.setRegion(newRgn);
            if(prevRgn != null)
                prevRgn.RemovePlayer(p);
            newRgn.AddPlayer(p);
            if(removedForPlayers != null)
            {
                if(newRgn != prevRgn && prevRgn != null)
                {
                    removedForPlayers.addAll(removedFromRegion(prevRgn.getSubscribers(), newRgn));
                }
            }
            
            rgns.retainAll(p.getPublishers());
            rgns_copy.removeAll(rgns);
            for(Region r : rgns_copy)
            {
                if(removedPlayers != null)
                    removedPlayers.addAll(r.getPlayers());
                r.RemoveSubscriber(p);
            }
            
            if(addedPlayers != null)
            {
                List<Region> newRgns_copy = new ArrayList<>();
                newRgns_copy.addAll(p.getPublishers());
                newRgns_copy.removeAll(rgns);
                for(Region nr: newRgns_copy)
                {
                    addedPlayers.addAll(nr.getPlayers());
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    /*
     * Updates the list of region that fall in the player's area of interest
     * @param p     Player 
     */
    private void updateInterestRegions(Player p)
    {
        VectorF aoi = p.getInterestAreaSize();
        VectorF pos = p.getPosition();
            
        if (aoi != null) 
        {
            int x1 = (int) Math.floor((pos.x - aoi.x / 2 - m_tl.x) / m_divisionArea.x);
            int x2 = (int) Math.floor((pos.x + aoi.x / 2 - m_tl.x) / m_divisionArea.x);
            int y1 = (int) Math.floor((pos.y - aoi.y / 2 - m_tl.y) / m_divisionArea.y);
            int y2 = (int) Math.floor((pos.y + aoi.y / 2 - m_tl.y) / m_divisionArea.y);

            x1 = x1 < 0 ? 0 : x1;
            x2 = x2 >= m_subdivisions.x ? m_subdivisions.x - 1 : x2;
            y1 = y1 < 0 ? 0 : y1;
            y2 = y2 >= m_subdivisions.y ? m_subdivisions.y - 1 : y2;

            for (int i = x1; i <= x2; ++i) {
                for (int j = y1; j <= y2; ++j) {
                    m_regions[i][j].AddSubscriber(p);
                    p.getPublishers().add(m_regions[i][j]);
                }
            }
        }
    }
    
    /*
     * Send Message to all the players subscribed to the Player p's current region
     * @param p     Player who is sending the message
     * @param msg   Message to be sent
     */
    public void multicastMessage(Player p, byte[] msg)
    {
        Region rgn = p.getRegion();
        List<Player> remotePlayers = rgn.getSubscribers();
        
        for(Player rp: remotePlayers)
        {
            rp.getIUser().SendUpdatePeersNotification(msg, false);
        }
    }
    
    /*
     * Send Message to all the players subscribed to the Player p's current region
     * @param p     Player who is sending the message
     * @param msg   Message to be sent
     */
    public void multicastMessage(Player p, String msg)
    {
        IUser user = p.getIUser();
        Region rgn = p.getRegion();
        List<Player> remotePlayers = rgn.getSubscribers();
        
        for(Player rp: remotePlayers)
        {
            rp.getIUser().SendChatNotification(user.getName(), msg, m_room);
        }
    }
    
    /*
     * Send a messaged to a list of players
     * @param sender        Player who is sending the message
     * @param receivers     List of players who will be receiving the message
     * @param msg           Message to be sent
     * @return void
     */
    public void multicastMessage(Player sender, List<Player> receivers, String msg)
    {
        IUser user = sender.getIUser();
        for(Player rp: receivers)
        {
            rp.getIUser().SendChatNotification(user.getName(), msg, m_room);
        }
    }
    
    /*
     * Send a message to a player
     * @param receiver      Player who will recieve the message
     * @param message       Message to be sent
     * @param sender        Player who is sending the message
     */
    public void sendMessage(Player receiver, String message, Player sender)
    {
        if(sender == null)
            sender = receiver;
        
        receiver.getIUser().SendChatNotification(sender.getIUser().getName(), message, m_room);
    }
    
    /*
     * Send a message to a player
     * @param receiver      Player who will recieve the message
     * @param message       Message to be sent
     */
    public void sendMessage(Player receiver, byte[] message)
    {        
        receiver.getIUser().SendUpdatePeersNotification(message, false);
    }
    
    /*
     * Returns the list of players who has left the region rgn 
     */
    private List<Player> removedFromRegion(List<Player> players, Region rgn)
    {
        List<Player> removedPlayers = new ArrayList<>();
        for(Player p : players)
        {
            if(p.getPublishers().contains(rgn) == false)
            {
                removedPlayers.add(p);
            }
        }
        
        return removedPlayers;
    }
}
