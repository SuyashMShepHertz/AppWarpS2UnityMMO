package MMO;

import com.shephertz.app42.server.idomain.IUser;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Suyash Mohan
 */
public class Player {
    //Position of Player
    private VectorF m_position;
    //Reference to IUSer
    private IUser m_user;
    //Dimensions of area of interest
    private VectorF m_aoi;
    //Current Region
    private Region m_region;
    //List of players to whom the player is subscribed to
    private List<Region> m_publishers;
    
    /*
     * Initialise the Player with position as (0,0)
     * @param user      Reference to IUser
     */
    public Player(IUser user)
    {
        m_user = user;
        m_position = new VectorF(0, 0);
        m_aoi = null;
        m_region = null;
        m_publishers = new ArrayList<>();
    }
    
    /*
     * Initialise the Player with position as (x,y)
     * @param user      Reference to IUser
     * @param x
     * @param y
     */
    public Player(IUser user,float x, float y)
    {
        m_user = user;
        m_position = new VectorF(x,y);
        m_aoi = null;
        m_region = null;
        m_publishers = new ArrayList<>();
    }
    
    /*
     * Initialise the Player with position as pos
     * @param user      Reference to IUser
     * @param pos       Vector representing the position
     */
    public Player(IUser user,VectorF pos)
    {
        m_user = user;
        m_position = pos;
        m_aoi = null;
        m_region = null;
        m_publishers = new ArrayList<>();
    }
    
    /*
     * Sets the dimensions of interest area of player
     * @param rc        Dimensions of the rectangle representing area of interest
     */
    public void setInterestAreaSize(VectorF rc)
    {
        m_aoi = rc;
    }
    
    /*
     * Sets the dimensiotn of interest area of player
     * @param width     Width of the rectangle representing area of interest
     * @param height    Height of the rectangle representing area of interest
     */
    public void setInterestAreaSize(float width, float height)
    {
        m_aoi = new VectorF();
        m_aoi.x = width;
        m_aoi.y = height;
    }
    
    /*
     * Returns the area of interest size
     */
    public VectorF getInterestAreaSize()
    {
        return m_aoi;    
    }
    
    /*
     * Returns the IUser reference of player
     */
    public IUser getIUser()
    {
        return m_user;
    }
    
    /*
     * Return the position of player
     */
    public VectorF getPosition()
    {
        return m_position;
    }
    
    /*
     * Sets the position of player
     * @param x
     * @param y
     */
    public void setPosition(float x, float y)
    {
        m_position.x = x;
        m_position.y = y;
    }
    
    /*
     * Returns the reference to the current region where player is present
     */
    public Region getRegion()
    {
        return m_region;
    }
    
    /*
     * Set the reference to current region
     */
    public void setRegion(Region rgn)
    {
        m_region = rgn;
    }
    
    /*
     * Returns the list of all regions who will publish updates to the player
     */
    public List<Region> getPublishers()
    {
        return m_publishers;
    }
    
    /*
     * Returns the name of player
     */
    public String getName()
    {
        return m_user.getName();
    }
}
