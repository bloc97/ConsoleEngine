/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.ui;

import java.util.concurrent.Executor;

/**
 *
 * @author bowen
 */
public interface RunnerUtils {
    
    public static void runAt(Runnable runnable, Executor executor, int ticksPerSecond) {
        
        if (ticksPerSecond == 0 || runnable == null || executor == null) {
            return;
        }
        
        executor.execute(() -> {
            final int milisecondsPerFrame = 1000/ticksPerSecond;
            long lastTime = System.currentTimeMillis() - milisecondsPerFrame;
            while (true) {
                try {
                    final long currentTime = System.currentTimeMillis();
                    if (currentTime - lastTime >= milisecondsPerFrame) {
                        lastTime = currentTime;
                        runnable.run();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
}
