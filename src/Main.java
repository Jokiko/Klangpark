import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    static int parkUnit;
    static int x;
    static int y;
    static int z;
    static int bird;
    static int insect;
    static final Semaphore configured = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ConfigWindow cw = new ConfigWindow();
        //only go on if configurations are set
        configured.acquire();

        Park.createPark(x*parkUnit, y*parkUnit , z*parkUnit, bird, insect);

        ParkWindow pw = new ParkWindow("", x*parkUnit, z*parkUnit);
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<?>> futures = new ArrayList<Future<?>>();

        while(true) {
            long timeStart = System.currentTimeMillis();
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    for (int k = 0; k < z; k++) {
                        ThreadVolume tV = new ThreadVolume(i * parkUnit, (i + 1) * parkUnit,
                                j * parkUnit, (j + 1) * parkUnit,
                                k * parkUnit, (k + 1) * parkUnit);
                        Runnable runnable = new ParkTask(tV);
                        Future<?> f = executorService.submit(runnable);
                        futures.add(f);
                    }
                }
            }

            for(Future<?> future : futures)
                future.get();

            long timeEnd = System.currentTimeMillis();
            System.out.println(timeEnd - timeStart);
        }

    }

}
