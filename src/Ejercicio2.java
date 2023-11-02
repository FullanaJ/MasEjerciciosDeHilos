class Mesa{

    public static synchronized boolean usandose(Cubierto c1, Cubierto c2){
        if (c1.enUso || c2.enUso){
            return true;
        }else{
            c1.usar();
            c2.usar();
            return false;
        }
    }
    public void comer(String quien,Cubierto c1, Cubierto c2) {
        while(usandose(c1, c2)){
            esperar();
        }
        System.out.println(quien+" comiendo con " + c1.id + " y " + c2.id);
        dejarDeUsar(c1, c2);
    }

    private synchronized static void dejarDeUsar(Cubierto c1, Cubierto c2) {
        c1.dejarDeUsar();
        c2.dejarDeUsar();
    }

    public synchronized void notificar(){
        notifyAll();
    }
    public synchronized void esperar(){
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Cubierto {
    public String id;
    public boolean enUso = false;

    public void usar(){
        enUso = true;
    }
    public void dejarDeUsar(){
        enUso = false;
    }

    public Cubierto(String s) {
        this.id = s;
    }

    public static void main(String[] args) {
        Cubierto c1 = new Cubierto("cubierto 1");
        Cubierto c2 = new Cubierto("cubierto 2");
        Cubierto c3 = new Cubierto("cubierto 3");
        Mesa mesa = new Mesa();
        Filososfo f1 = new Filososfo(c1, c2, "Aristoteles",mesa);
        Filososfo f2 = new Filososfo(c2, c3, "Platon",mesa);
        Filososfo f3 = new Filososfo(c3, c1, "Socrates",mesa);
        f1.start();
        f2.start();
        f3.start();

        try {
            f1.join();
            f2.join();
            f3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
class Filososfo extends Thread {
    Mesa mesa ;
    Cubierto c1;
    Cubierto c2;
    String id;
    public Filososfo(Cubierto c1, Cubierto c2, String id, Mesa mesa) {
        this.c1 = c1;
        this.c2 = c2;
        this.id = id;
        this.mesa = mesa;
    }
    @Override
    public void run() {
        System.out.println(id + " iniciando");
        for(int i = 0; i < 10; i++){
            mesa.comer(id,c1, c2);
            mesa.notificar();
            try {
                System.out.println(i + "->" + id+" pensando");
                Thread.sleep((int) (Math.random()* 5)*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
