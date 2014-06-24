package MMO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Suyash Mohan
 */
public class Region {
    //m_bound represents the rectangle that form the Rectangle inside the world
    private Rect m_bound;
    //m_subscribers is the list of all the player who have subscribed to the region
    private List<Player> m_subscribers;
    //m_players is the list of all player that are inside the region
    private List<Player> m_players;
    
    /*
     * Initialises the Region
     * @param x         x-coordinate of position
     * @param y         y-coordinate of position
     * @param width     width of the Region
     * @paaram height   height of the Region
     */
    public Region(float x, float y, float width, float height)
    {
        m_bound = new Rect();
        m_bound.Size.x = width;
        m_bound.Size.y = height;
        m_bound.Origin.x = x;
        m_bound.Origin.y = y;
        
        m_subscribers = new ArrayList<Player>();
        m_players = new ArrayList<Player>();
    }
    
    /*
     * Adds a subscriber to the region
     * A subscriber may not present inside the region
     * Subscriber is the one who wants to listen to the region
     * @param p Player that has to be added
     */
    public void AddSubscriber(Player p)
    {
        if(m_subscribers.contains(p) == false)
            m_subscribers.add(p);
    }
    
    /*
     * Unsubscribe a player from the region by removing him 
     * @param p Player that has to be removed from subscribtion
     */
    public void RemoveSubscriber(Player p)
    {
        if(m_subscribers.contains(p) == true)
            m_subscribers.remove(p);
    }
    
    /*
     * Add a player inside the region
     * @param p Player that has to be added
     */
    public void AddPlayer(Player p)
    {
        if(m_players.contains(p) == false)
            m_players.add(p);
    }
    
    /*
     * Remove the player from the region
     * @param p Player that has to be removed
     */
    public void RemovePlayer(Player p)
    {
        if(m_players.contains(p) == true)
            m_players.remove(p);
    }
    
    /*
     * Returns the list of all Players
     */
    public List<Player> getPlayers()
    {
        return m_players;
    }
    
    /*
     * Returns the list of all the subscribers
     */
    public List<Player> getSubscribers()
    {
        return m_subscribers;
    }
    
    //Returns the rectangle representing the region
    public Rect getRect()
    {
        return m_bound;
    }
}
