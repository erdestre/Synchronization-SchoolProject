package sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;



public class Test {
    public static void main(String [] args) {


        ExecutorService executorService = Executors.newCachedThreadPool();
        ReadWriteLock RW = new ReadWriteLock();

        executorService.execute(new Writer(RW));
        executorService.execute(new Writer(RW));
        executorService.execute(new Writer(RW));
        executorService.execute(new Writer(RW));

        executorService.execute(new Reader(RW));
        executorService.execute(new Reader(RW));
        executorService.execute(new Reader(RW));
        executorService.execute(new Reader(RW));

    }
}
class ReadWriteLock{
    private Semaphore S=new Semaphore(1);

boolean accessibility = true;
int i = 0;
public void readLock() {

        try {
            while (!accessibility){
                System.out.println("Can't Read");
            }
            System.out.println("Somebody reading");
            S.acquire(0);
            i++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void writeLock() {
        try {
            while (!accessibility || i!=0){
                System.out.println("Can't write");
            }
            accessibility = false;
            System.out.println("Somebody is writing...");
            S.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void readUnLock() {
        S.release(0);
        i--;
        System.out.println(i);
    }
    public void writeUnLock() {
        accessibility = true;
        S.release();
        System.out.println("Writing is done");
    }
}

class Writer implements Runnable
{
    private ReadWriteLock RW_lock;


    public Writer(ReadWriteLock rw) {
        RW_lock = rw;
    }

    public void run() {
        while (true){
            RW_lock.writeLock();

            RW_lock.writeUnLock();

        }
    }
}

class Reader implements Runnable
{
    private ReadWriteLock RW_lock;

    public Reader(ReadWriteLock rw) {
        RW_lock = rw;
    }

    public void run() {
        while (true){
            RW_lock.readLock();

            RW_lock.readUnLock();

        }
    }
}