package pso.myself;

public class MParticle {
    private MLocation location;
    private MVelocity velocity;
    private double fitnessValue;
    public MParticle(){
        super();
    }
    public MParticle(double fitnessValue,MLocation location,MVelocity velocity){
        this.fitnessValue = fitnessValue;
        this.location = location;
        this.velocity = velocity;
    }
    
    public MLocation getLocation(){
        return location;
    }
    
    public MVelocity getVelocity(){
        return velocity;
    }
    
    public void setLocation(MLocation location){
        this.location = location;
    }
    
    public void setVelocity(MVelocity velocity){
        this.velocity = velocity;
    }
    
    public double getFitnessValue(){
        return MProblemSet.evaluate_Rosenbrock(this.location);
    }
}
