package MMO;

/**
 *
 * @author Suyash Mohan
 */
public class Rect {
    //Origin of Rectangle
    public VectorF Origin;
    //Size of rectangle
    public VectorF Size;
    
    /*
     * Initialize the rectangle with all values = 0
     */
    public Rect()
    {
        Origin = new VectorF();
        Size = new VectorF();
    }
    
    /*
     * Initialize the rectangle with Size (width, height) and position (x,y)
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Rect(float x, float y, float width, float height)
    {
        Origin = new VectorF();
        Size = new VectorF();
        
        Origin.x = x;
        Origin.y = y;
        Size.x = width;
        Size.y = height;
    }
}
