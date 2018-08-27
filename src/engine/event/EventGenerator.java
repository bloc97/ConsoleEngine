/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.event;

import java.util.function.Consumer;

/**
 *
 * @author bowen
 */
public interface EventGenerator<T> {
    public boolean attachListener(Object listener, Consumer<T> onEvent);
    public boolean removeListener(Object listener);
}
