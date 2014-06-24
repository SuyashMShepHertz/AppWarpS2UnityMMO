package MMO;

/**
 *
 * @author Suyash Mohan
 */
public class VectorI {
    //x coordinate
    public int x;
    //y coordinate
    public int y;
    
    /*
     * Initialize with (0,0)
     */
    public VectorI()
    {
        x = y = 0;
    }
    
    /*
     * Initialize with (p_x,p_y)
     * @param p_x
     * @param p_y
     */
    public VectorI(int p_x, int p_y)
    {
        x = p_x;
        y = p_y;
    }
}
