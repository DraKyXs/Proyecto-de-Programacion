class  ResourceHolder { 
    @Override 
    protected  void  finalize () { 
        System.out.println( "Finalizando objeto y liberando recursos" ); 
    } 
} 

public  class  FinalizeExample { 
    public  static  void  main (String[] args) { 
        new  ResourceHolder (); 
        System.gc(); // Solicitar recolección de basura
     } 
}