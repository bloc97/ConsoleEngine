/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.event.handler;

import engine.ui.NativeWindow;

/**
 *
 * @author bowen
 */
public interface NativeHandler {
    public NativeWindow getNativeWindow();
    public void setNativeWindow(NativeWindow nativeWindow);
}
