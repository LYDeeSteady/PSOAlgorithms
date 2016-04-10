package pso.myself;
        

public class MProblemSet {
    public static final double LOC_UPPER = 100;
    public static final double LOC_LOW = -100;
    public static final double VEL_UPPER = 5;
    public static final double VEL_LOW = -5;
    
    public static final double ERR_TOLERANCE = 1E-20;
    
    public static double evaluate_sphere(MLocation location){
        double result=0;
        for(int i=0;i<MPSOConstants.PROBELM_DIMENSION;i++){
            double x =location.getLocationArray()[i];
            result = result+Math.pow(x, 2);
        }
        return result;
    }
    
    public static double evaluate_Rosenbrock(MLocation location){
        double result=0;
        for(int i=0;i<MPSOConstants.PROBELM_DIMENSION-1;i++){
            double x =location.getLocationArray()[i];
            double next_x =location.getLocationArray()[i+1];
            result = Math.pow( 100 * (next_x - Math.pow(x, 2)), 2) + Math.pow(x-1, 2);
        }
        return result;
    }
}
