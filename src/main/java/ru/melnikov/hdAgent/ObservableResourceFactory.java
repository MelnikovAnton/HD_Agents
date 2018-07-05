package ru.melnikov.hdAgent;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.log4j.Logger;

import java.util.ResourceBundle;

public class ObservableResourceFactory {

    private static final Logger log = Logger.getLogger(ObservableResourceFactory.class);

    private final ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();
    private ObjectProperty<ResourceBundle> resourcesProperty() {
        return resources ;
    }
    public final ResourceBundle getResources() {
        return resourcesProperty().get();
    }
    public final void setResources(ResourceBundle resources) {
        log.info("Language changed to " + resources.getLocale().toString());
        resourcesProperty().set(resources);
    }

    public StringBinding getStringBinding(String key) {
        return new StringBinding() {
            { bind(resourcesProperty()); }
            @Override
            public String computeValue() {
                return getResources().getString(key);
            }
        };
    }
}
