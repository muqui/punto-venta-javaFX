/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import com.fasterxml.jackson.databind.module.SimpleModule;
import javafx.collections.ObservableList;

public class CustomModule extends SimpleModule {
    public CustomModule() {
        addDeserializer(ObservableList.class, new ObservableListDeserializer());
    }
}

