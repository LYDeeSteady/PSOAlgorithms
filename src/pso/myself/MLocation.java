package pso.myself;
public class MLocation {
    private double location[];
    public MLocation(double[] loc){
        super();
        location = loc;
        
    }
    
    public double[] getLocationArray(){
        return this.location;
    }
    
    public void setLocation(double[] loc){
        location = loc;
    }
}
