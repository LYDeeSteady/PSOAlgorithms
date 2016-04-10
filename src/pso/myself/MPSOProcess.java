package pso.myself;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MPSOProcess {
    private Vector<MParticle> swam = new Vector();
    private double pBest[] = new double[MPSOConstants.SWAM_SIZE];
    private Vector<MLocation> pBestLocation = new Vector<MLocation>();
    private double gBest;
    private MLocation gBestLocation;
    private double[] fitnessValueArray = new double[MPSOConstants.SWAM_SIZE];
    Random random = new Random();
    
    public void start(){
        
        initializeSwarm();
        updateFitnessValueArry();
        for(int i=0;i<MPSOConstants.SWAM_SIZE;i++){
            pBest[i] = fitnessValueArray[i];
            pBestLocation.add(swam.get(i).getLocation());
        }
        int t=1;
        //inertia weight
        double w;
        double c1,c2;
        double err = 9999;
        
        while(t < MPSOConstants.MAX_Iteration && err > MProblemSet.ERR_TOLERANCE){
//            update pBest
            for(int i=0;i<swam.size();i++){
                if(fitnessValueArray[i]<pBest[i]){
                    pBest[i] = fitnessValueArray[i];
                    pBestLocation.set(i, swam.get(i).getLocation());
                }
            }
            
//            update gBest
            int bestParticleIndex = MPSOUtillity.getMinPsoIndex(fitnessValueArray);
            if(t == 1 || fitnessValueArray[bestParticleIndex] < gBest){
                gBest = fitnessValueArray[bestParticleIndex];
                gBestLocation = swam.get(bestParticleIndex).getLocation();
            }
            
//            print value
            show(t);
            
//            w = wstar – (wstar - wend )/ Tmax × t 
            w = MPSOConstants.W_UP - ((MPSOConstants.W_UP - MPSOConstants.W_LW) / (double)(MPSOConstants.MAX_Iteration) * (double)t);
            
//            c1 = c1s + (c1e - c1s ) / Tmax × t 
            c1 = MPSOConstants.C1_UP + ((MPSOConstants.C1_LW - MPSOConstants.C1_UP) / (double)(MPSOConstants.MAX_Iteration) * (double)t);
            c2 = MPSOConstants.C2_UP + (MPSOConstants.C1_LW - MPSOConstants.C2_UP) / (double)MPSOConstants.MAX_Iteration *(double)t;
            
//            c1,c2 = 2
//            c1 = MPSOConstants.C1_UP;
//            c2 = MPSOConstants.C2_UP;
            
            
            for(int i=0;i<MPSOConstants.SWAM_SIZE;i++){
                MParticle p = swam.get(i);
//                r1和r2 是介於 [0, 1]之間的隨機值，用來保持群體的多樣性。
                double r1 = random.nextDouble();
                double r2 = random.nextDouble();
                
//                update Velocity
                double[] newVel = new double[MPSOConstants.PROBELM_DIMENSION];
                for(int x=0;x<MPSOConstants.PROBELM_DIMENSION;x++){
//                    vit+1 = w vit +c1 r1 (pit - xit)+ c2 r2 (pgt - xit)
                    newVel[x] = (w * p.getVelocity().getVelocity()[x])
                                    +(c1 * r1 )* (pBestLocation.get(i).getLocationArray()[x] - p.getLocation().getLocationArray()[x])
                                    +(c2 * r2 )* (gBestLocation.getLocationArray()[x] - p.getLocation().getLocationArray()[x]);
                    if(newVel[x] > MProblemSet.VEL_UPPER ){
                        newVel[x] = MProblemSet.VEL_UPPER;
                    }else if(newVel[x] < MProblemSet.VEL_LOW){
                        newVel[x]=MProblemSet.VEL_LOW;
                    }
                }
                MVelocity vel = new MVelocity(newVel);
                p.setVelocity(vel);

//                update Location 
                double[] newLoc = new double[MPSOConstants.PROBELM_DIMENSION];
                for(int x=0;x<MPSOConstants.PROBELM_DIMENSION;x++){
//                    xit+1 = xit + vit+1 
                    newLoc[x] = p.getLocation().getLocationArray()[x] + newVel[x];
                }
                MLocation loc = new MLocation(newLoc);
                p.setLocation(loc);
            }

//            Compute difference
            err = MProblemSet.evaluate_Rosenbrock(gBestLocation)-0;
            
            t++;
            updateFitnessValueArry();
        }
        
        System.out.println("\nSolution found at iteration " + (t) + ", the solutions is:");		
        for(int x=0;x<MPSOConstants.PROBELM_DIMENSION;x++){
            System.out.print("X"+x+" = "+gBestLocation.getLocationArray()[x]+"\t");
        }
        
        System.out.println("\n"+"Gbest Value："+MProblemSet.evaluate_Rosenbrock(gBestLocation));
        try {
            BufferedWriter bufw = new BufferedWriter(new FileWriter("11.txt",true));
            bufw.append("Value\t"+gBest+"\n");
            bufw.flush();
        } catch (IOException ex) {
            Logger.getLogger(MPSOProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    initialization 
    public void initializeSwarm(){
        MParticle p;
        for(int i=0;i<MPSOConstants.SWAM_SIZE;i++){
            p = new MParticle();
            double loc[] = new double[MPSOConstants.PROBELM_DIMENSION];
            double vel[] = new double[MPSOConstants.PROBELM_DIMENSION];
            for(int num=0;num<MPSOConstants.PROBELM_DIMENSION;num++){
                loc[num] = MProblemSet.LOC_LOW + random.nextDouble()*(MProblemSet.LOC_UPPER-MProblemSet.LOC_LOW);
                vel[num] = MProblemSet.VEL_LOW + random.nextDouble()*(MProblemSet.VEL_UPPER-MProblemSet.VEL_LOW);
            }
            MLocation location = new MLocation(loc);
            MVelocity velocity = new MVelocity(vel);
            p.setLocation(location);
            p.setVelocity(velocity);
            swam.add(p);
        }
    }
    
    public void updateFitnessValueArry(){
        for(int i=0;i<MPSOConstants.SWAM_SIZE;i++){
            fitnessValueArray[i] = swam.get(i).getFitnessValue();
        }
    }
    

    public void show(int t){
        String formatStr = "%.17f";
        
        System.out.println("The ITERATION : "+t);
            System.out.println("Best Location：");
            for(int x=0;x<MPSOConstants.PROBELM_DIMENSION;x++){
                if(x%3 == 0){
                    System.out.println();
                }
                String formatAns = String.format(formatStr, gBestLocation.getLocationArray()[x]);
                System.out.print("X"+x+" = "+formatAns+"\t\t");
                
            }
            System.out.println("\n"+"Best value："+MProblemSet.evaluate_Rosenbrock(gBestLocation));
            System.out.println("-----------------------------------------------------------");
    }
}

