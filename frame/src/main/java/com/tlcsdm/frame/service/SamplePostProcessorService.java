package com.tlcsdm.frame.service;

import java.util.ArrayList;
import java.util.List;

import com.tlcsdm.frame.Sample;

import javafx.scene.control.TreeItem;

/**
 * SampleFactory后置处理
 */
public interface SamplePostProcessorService {
    public static List<TreeItem<Sample>> Samples = new ArrayList<>();

    /**
     * Samples 后置处理
     */
    void postProcessBeanFactory();
}
