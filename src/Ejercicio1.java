import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

class Buffer {

    List<Integer> buffer = new ArrayList<>();

    public static void main(String[] args) {

        Buffer buffer = new Buffer();
        Consumidor consumidor = new Consumidor(buffer, "1");
        Consumidor consumidor2 = new Consumidor(buffer, "2");
        Productor productor = new Productor(buffer, "1");
        Productor productor2 = new Productor(buffer, "2");
        Productor productor3 = new Productor(buffer, "3");

        productor3.start();
        productor2.start();
        consumidor2.start();
        consumidor.start();
        productor.start();

    }
    public synchronized Integer consumir(){
        while(buffer.size() <= 0){
            try {
                sleep((int) (Math.random()* 2)*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                wait();
                System.out.println("Buffer vacio");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        notifyAll();
        return buffer.remove(0);
    }

    public synchronized void producir(Integer valor){
        while (buffer.size() >= 3){
            try {
                try {
                    sleep((int) (Math.random()* 2)*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wait();
                System.out.println("Buffer lleno");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        buffer.add(valor);
        notifyAll();
    }

}
class Consumidor extends Thread{

    Buffer buffer;
    String id;

    public Consumidor(Buffer buffer, String id) {
        this.buffer = buffer;
        this.id = id;
    }

    @Override
    public void run() {
        for(int i = 0; i < 45; i++){
            Integer valor = buffer.consumir();
            System.out.println("Consumidor "+ id +" Consume: " + valor);
        }
    }
}
class Productor extends Thread{
    Buffer buffer;
    String id;

    public Productor(Buffer buffer, String id) {
        this.buffer = buffer;
        this.id = id;
    }

    @Override
    public void run() {
        for(int i = 0; i < 30; i++){
            buffer.producir(i);
            System.out.println("Productor "+id+" produce: " + i);
        }
    }
}