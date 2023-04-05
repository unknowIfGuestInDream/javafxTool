package com.tlcsdm.smc.controller;

import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import org.controlsfx.control.CheckTreeView;

public class CheckTreeViewBuilderFactory implements BuilderFactory {

    private final JavaFXBuilderFactory javaFXBuilderFactory = new JavaFXBuilderFactory();

    @Override
    public Builder<?> getBuilder(Class<?> aClass) {
        return (aClass == CheckTreeView.class) ? new CheckTreeViewBuilder() : javaFXBuilderFactory.getBuilder(aClass);
    }

    public static class CheckTreeViewBuilder implements Builder<CheckTreeView> {

        @Override
        public CheckTreeView build() {
            return new CheckTreeView();
        }
    }
}
