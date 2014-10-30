/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import scripturefinder.Entry;

/**
 *
 * @author Admin
 */
public interface Updater {
    public void update(Entry input);
    public void resetCounts();
    public void displayDone();
}
