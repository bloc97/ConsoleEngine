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
    
    public final static class RunnerControl {
        private volatile int milisecondsPerTick;
        private volatile float allowedError;

        public RunnerControl() {
            setTicksPerSecond(60f);
        }
        
        public RunnerControl(int ticksPerSecond) {
            setTicksPerSecond(ticksPerSecond);
            setAllowedError(0f);
        }
        
        public RunnerControl(float ticksPerSecond, float allowedError) {
            setTicksPerSecond(ticksPerSecond);
            setAllowedError(allowedError);
        }

        
        public float getTicksPerSecond() {
            return 1000f / milisecondsPerTick;
        }
        
        public void setTicksPerSecond(float ticksPerSecond) {
            if (ticksPerSecond <= 0) {
                this.milisecondsPerTick = Integer.MAX_VALUE;
            } else {
                this.milisecondsPerTick = (int)(1000f/ticksPerSecond);
                if (milisecondsPerTick < 0) {
                    milisecondsPerTick = Integer.MAX_VALUE;
                }
            }
        }

        public void setMilisecondsPerTick(int milisecondsPerTick) {
            if (milisecondsPerTick < 0) {
                milisecondsPerTick = Integer.MAX_VALUE;
            }
            this.milisecondsPerTick = milisecondsPerTick;
        }

        public int getMilisecondsPerTick() {
            return milisecondsPerTick;
        }

        public float getAllowedError() {
            return allowedError;
        }

        public void setAllowedError(float allowedError) {
            if (allowedError < 0f) {
                allowedError = 0f;
            } else if (allowedError > 22f) {
                allowedError = 2f;
            }
            this.allowedError = allowedError;
        }
        
    }
    
    public static boolean runAt(Runnable runnable, Executor executor, final RunnerControl control) {
        
        if (control == null || runnable == null || executor == null) {
            return false;
        }
        executor.execute(() -> {
            long lastTime = System.currentTimeMillis() - control.getMilisecondsPerTick();
            while (true) {
                try {
                    final long currentTime = System.currentTimeMillis();
                    if (currentTime - lastTime >= control.getMilisecondsPerTick()) {
                        lastTime = currentTime;
                        runnable.run();
                    }
                    if (control.getAllowedError() > 0f) {
                        Thread.sleep((int)(control.getMilisecondsPerTick() * control.getAllowedError()));
                    }
                } catch (Exception ex) {
                    //ex.printStackTrace();
                }
            }
        });
        return true;
    }
    
}
