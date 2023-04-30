/*
 * Copyright (c) 2023 unknowIfGuestInDream
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.smc.config;

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.model.AbstractTreeViewCellFactory;
import com.tlcsdm.frame.model.EmptySample;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;

public class SmcTreeViewCellFactory extends AbstractTreeViewCellFactory {

    @Override
    public void setupImageBinding(TreeCell<Sample> treeCell) {
        treeCell.graphicProperty().bind(Bindings.createObjectBinding(() -> {
            final Sample item = treeCell.getItem();
            if (item == null) {
                return null;
            }
            if (item instanceof EmptySample emptySample) {
                return switch (item.getSampleName()) {
                    case "Smc", "Common" ->
                        LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/folder.png"));
                    case "CodeDev" ->
                        LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/code.png"));
                    case "Tools" ->
                        LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/tools.png"));
                    case "UnitDesign" ->
                        LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/design.png"));
                    case "UnitTest" ->
                        LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/test.png"));
                    case "Ecm" -> LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/ecm.png"));
                    default -> emptySample.getSampleImageIcon();
                };
            }
            return item.getSampleImageIcon();
        }, treeCell.itemProperty()));
    }

    @Override
    public void setupContextMenu(TreeCell<Sample> treeCell, TreeView<Sample> classes) {
//        final ContextMenu classesContextMenu = new ContextMenu();
//
//        final MenuItem scopedExport = createScopedExport(treeCell, classes);
//        final MenuItem exportClasses = createExportClasses(classes);
//        final MenuItem reloadClasses = createReloadClasses();
//        final MenuItem scopedReplace = createScopedReplace(treeCell, classes);
//        final MenuItem replaceClasses = createReplaceClasses(classes);
//        final MenuItem includeClassLoader = createShowClassLoader(reloadClasses);
//        final MenuItem executeCode = createExecuteCode(treeCell, classes);
//
//        treeCell.itemProperty().addListener((obs, old, newv) -> {
//            classesContextMenu.getItems().clear();
//            if (newv != null) {
//                classesContextMenu.getItems().addAll(scopedExport, scopedReplace, new SeparatorMenuItem());
//            }
//            classesContextMenu.getItems()
//                .addAll(executeCode,
//                    new SeparatorMenuItem(),
//                    exportClasses,
//                    replaceClasses,
//                    reloadClasses,
//                    new SeparatorMenuItem(),
//                    includeClassLoader);
//        });
//
//        treeCell.setContextMenu(classesContextMenu);
    }

//    private MenuItem createScopedExport(TreeCell<ClassTreeNode> treeCell, TreeView<ClassTreeNode> classes) {
//        final MenuItem scopedExport = new MenuItem();
//        scopedExport.textProperty().bind(Bindings.createStringBinding(() -> {
//            final ClassTreeNode classTreeNode = treeCell.getItem();
//            if (classTreeNode == null) {
//                return "";
//            }
//            switch (classTreeNode.getType()) {
//                case CLASSLOADER:
//                    return "Export Class Loader";
//                case PACKAGE:
//                    return "Export Package";
//                case CLASS:
//                    return "Export Class";
//            }
//            log.warn("Unknown type: {}", classTreeNode.getType());
//            return "";
//        }, treeCell.itemProperty()));
//        scopedExport.setOnAction(e -> {
//            final RunningJvm activeJvm = currentJvm.get();
//            if (activeJvm == null) {
//                return;
//            }
//            final ClassTreeNode classTreeNode = treeCell.getItem();
//            if (classTreeNode == null) {
//                return;
//            }
//            switch (classTreeNode.getType()) {
//                case CLASSLOADER:
//                    final File exportClassLoader = selectExportJarFile(classTreeNode.getPackageSegment(),
//                        classes.getScene().getWindow());
//                    if (exportClassLoader == null) {
//                        return;
//                    }
//                    final ClassLoaderDescriptor classLoaderDescriptor = classTreeNode.getClassLoaderDescriptor();
//                    log.debug("Exporting class loader: {}", classLoaderDescriptor);
//                    final List<LoadedClass> classesInClassLoader = classTreeHelper.getClassesInPackage(classesTreeRoot,
//                        "",
//                        classLoaderDescriptor);
//                    executorService.submit(() -> export(exportClassLoader, classesInClassLoader, activeJvm));
//                    break;
//                case PACKAGE:
//                    final File exportPackage = selectExportJarFile(classTreeNode.getPackageSegment(),
//                        classes.getScene().getWindow());
//                    if (exportPackage == null) {
//                        return;
//                    }
//                    final String fullPackageName = classTreeHelper.getPackageName(treeCell.getTreeItem());
//                    final ClassLoaderDescriptor packageClassLoader = this.settings.getShowClassLoader().get()
//                        ?
//                        classTreeHelper.getNodeClassLoader(treeCell.getTreeItem())
//                        : null;
//                    log.debug("Exporting package: {} in classloader: {}", fullPackageName, packageClassLoader);
//                    final List<LoadedClass> classesInPackage = classTreeHelper.getClassesInPackage(classesTreeRoot,
//                        fullPackageName,
//                        packageClassLoader);
//                    executorService.submit(() -> export(exportPackage, classesInPackage, activeJvm));
//                    break;
//                case CLASS:
//                    final LoadedClass loadedClass = classTreeNode.getLoadedClass();
//                    final File selectedFile = selectExportClassFile(loadedClass.getSimpleName() + ".class",
//                        classes.getScene().getWindow());
//                    if (selectedFile == null) {
//                        return;
//                    }
//                    log.debug("Exporting class: {}", loadedClass);
//                    executorService.submit(() -> export(selectedFile, loadedClass, activeJvm));
//                    break;
//            }
//        });
//        return scopedExport;
//    }

//    private MenuItem createExportClasses(TreeView<ClassTreeNode> classes) {
//        final MenuItem exportClasses = new MenuItem("Export Classes");
//        exportClasses.setOnAction(e -> {
//            final RunningJvm activeJvm = currentJvm.get();
//            if (activeJvm == null) {
//                return;
//            }
//            final File selectedFile = selectExportJarFile(activeJvm.getName(), classes.getScene().getWindow());
//            if (selectedFile == null) {
//                return;
//            }
//            final List<LoadedClass> loadedClasses = classesTreeRoot.streamVisible()
//                .map(ClassTreeNode::getLoadedClass)
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//            executorService.submit(() -> export(selectedFile, loadedClasses, activeJvm));
//        });
//        return exportClasses;
//    }

}
