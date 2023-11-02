import java.util.ArrayList;

class Tienda {

    public static void main(String[] args) {
        Tienda tienda = new Tienda();
        cliente cliente;
        ArrayList<cliente> clientes = new ArrayList<>();
        for(int i = 0; i < 200; i++){
            cliente = new cliente(tienda, "Cliente " + i);
            cliente.start();
            clientes.add(cliente);
        }
        tienda.notificar();
        for (cliente cli:clientes) {
            try {
                cli.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private int cantidadPlayStation5 = 20;
    private boolean vendiendo = false;
    public void comprarPlayStation5(String nombre) {
        if (!getVendiendo()) {
            System.out.println(nombre + " compra una PlayStation 5");
            vende();
        } else {
            System.out.println(nombre + " no pudo entrar a la tienda.");
        }
    }
    public synchronized void notificar() {
        notifyAll();
    }
    public synchronized void esperar() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public int getCantidadPlayStation5() {
        return cantidadPlayStation5;
    }

    public synchronized boolean getVendiendo() {
        if (!vendiendo){
            vendiendo = true;
            return false;
        }
        return vendiendo;
    }
    public synchronized void vende() {
        cantidadPlayStation5--;
        vendiendo = false;
    }
}

class cliente extends Thread{
    Tienda tienda;
    String nombre;
    boolean perdioLaPaciencia = true;

    public cliente(Tienda tienda, String nombre) {
        this.tienda = tienda;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        tienda.esperar();
        for(int i = 0; i < 10; i++){
            try {
                sleep((int) (Math.random()* 10)*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (tienda.getCantidadPlayStation5() > 0) {
                tienda.comprarPlayStation5(nombre);
            }else {
                System.out.println(nombre + " no pudo comprar una PlayStation 5");
                perdioLaPaciencia = false;
                break;
            }
        }
        if (perdioLaPaciencia){
            System.out.println(nombre + " perdio la paciencia.");
        }

    }
}