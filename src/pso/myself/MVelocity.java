package pso.myself;
public class MVelocity {
    private double velocity[];
    public MVelocity(double[] vel){
        super();
        this.velocity = vel;
    }
    
    public double[] getVelocity(){
        return velocity;
    }
    
    public void setVelocity(double[] vel){
        this.velocity = vel;
    }
}
