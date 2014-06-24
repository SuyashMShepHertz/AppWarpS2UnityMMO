package MMO;

/**
 *
 * @author Suyash Mohan
 */
public class VectorF {
    //x coordinate
    public float x;
    //y coordinate
    public float y;
    
    /*
     * Initialize with (0.0,0.0)
     */
    public VectorF()
    {
        x = y = 0.0f;
    }
    
    /*
     * Initialize with (p_x,p_y)
     */
    public VectorF(float p_x, float p_y)
    {
        x = p_x;
        y = p_y;
    }
}
