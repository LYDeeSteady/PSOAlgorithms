package pso.myself;

public class MPSOUtillity {
    public static int getMinPsoIndex(double[] list){
        double min=list[0];
        int index=0;
        
        for(int i=1;i<list.length;i++){
            if(min>list[i]){
                index = i;
                min = list[i];
            }
        }
        return index;
    }
}
