package com.tlcsdm.core.javafx.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import org.controlsfx.control.CheckModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/4/5 16:31
 */
public class CheckTreeView<T> extends TreeView<T> {

    /**************************************************************************
     *
     * Private fields
     *
     **************************************************************************/

    /**************************************************************************
     *
     * Constructors
     *
     **************************************************************************/

    /**
     * Creates a new CheckTreeView instance with an empty tree of choices.
     */
    public CheckTreeView() {
        this(null);
    }

    /**
     * Creates a new CheckTreeView instance with the given CheckBoxTreeItem set
     * as the tree root.
     *
     * @param root The root tree item to display in the CheckTreeView.
     */
    public CheckTreeView(final CheckBoxTreeItem<T> root) {
        super(root);
        rootProperty().addListener(o -> updateCheckModel());

        updateCheckModel();

        setCellFactory(CheckBoxTreeCell.<T>forTreeView());
    }

    protected void updateCheckModel() {
        if (getRoot() != null) {
            setCheckModel(new CheckTreeViewCheckModel<T>(this));
        }
    }

    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    /**
     * Returns the {@link BooleanProperty} for a given item index in the
     * CheckTreeView. This is useful if you want to bind to the property.
     */
    public BooleanProperty getItemBooleanProperty(int index) {
        CheckBoxTreeItem<T> treeItem = (CheckBoxTreeItem<T>) getTreeItem(index);
        return treeItem.selectedProperty();
    }

    /**************************************************************************
     *
     * Properties
     *
     **************************************************************************/

    // --- Check Model
    private ObjectProperty<CheckModel<TreeItem<T>>> checkModel =
            new SimpleObjectProperty<>(this, "checkModel"); //$NON-NLS-1$

    /**
     * Sets the 'check model' to be used in the CheckTreeView - this is the
     * code that is responsible for representing the selected state of each
     * {@link CheckModel} - that is, whether each {@link CheckModel} is checked or
     * not (and not to be confused with the
     * selection model concept, which is used in the TreeView control to
     * represent the selection state of each row)..
     */
    public final void setCheckModel(CheckModel<TreeItem<T>> value) {
        checkModelProperty().set(value);
    }

    /**
     * Returns the currently installed check model.
     */
    public final CheckModel<TreeItem<T>> getCheckModel() {
        return checkModel == null ? null : checkModel.get();
    }

    /**
     * The check model provides the API through which it is possible
     * to check single or multiple items within a CheckTreeView, as  well as inspect
     * which items have been checked by the user. Note that it has a generic
     * type that must match the type of the CheckTreeView itself.
     */
    public final ObjectProperty<CheckModel<TreeItem<T>>> checkModelProperty() {
        return checkModel;
    }

    /**************************************************************************
     *
     * Implementation
     *
     **************************************************************************/

    /**************************************************************************
     *
     * Support classes
     *
     **************************************************************************/

    private static class CheckTreeViewCheckModel<T> implements CheckModel<TreeItem<T>> {// extends CheckBitSetModelBase<TreeItem<T>> {

        /***********************************************************************
         *                                                                     *
         * Internal properties                                                 *
         *                                                                     *
         **********************************************************************/

        private final CheckTreeView<T> treeView;
        private final TreeItem<T> root;

        private ObservableList<TreeItem<T>> checkedItems = FXCollections.observableArrayList();

        /***********************************************************************
         *                                                                     *
         * Constructors                                                        *
         *                                                                     *
         **********************************************************************/

        CheckTreeViewCheckModel(final CheckTreeView<T> treeView) {
            this.treeView = treeView;
            this.root = treeView.getRoot();
            this.root.addEventHandler(CheckBoxTreeItem.<T>checkBoxSelectionChangedEvent(), e -> {
                CheckBoxTreeItem<T> treeItem = e.getTreeItem();

                if (treeItem.isSelected()) { // && ! treeItem.isIndeterminate()) {
                    check(treeItem);
                } else {
                    clearCheck(treeItem);
                }
            });

            // we should reset the check model and then update the checked items
            // based on the currently checked items in the tree view
            clearChecks();
            for (int i = 0; i < treeView.getExpandedItemCount(); i++) {
                CheckBoxTreeItem<T> treeItem = (CheckBoxTreeItem<T>) treeView.getTreeItem(i);
                if (treeItem.isSelected() && !treeItem.isIndeterminate()) {
                    check(treeItem);
                }
            }
        }

        /***********************************************************************
         *                                                                     *
         * Implementing abstract API                                           *
         *                                                                     *
         **********************************************************************/

        @Override
        public int getItemCount() {
            return treeView.getExpandedItemCount();
        }

        // TODO make read-only
        @Override
        public ObservableList<TreeItem<T>> getCheckedItems() {
            return checkedItems;
        }

        @Override
        public void checkAll() {
            iterateOverTree(this::check);
        }

        @Override
        public void clearCheck(TreeItem<T> item) {
            if (item instanceof CheckBoxTreeItem) {
                ((CheckBoxTreeItem<T>) item).setSelected(false);
            }
            checkedItems.remove(item);
        }

        @Override
        public void clearChecks() {
            List<TreeItem<T>> items = new ArrayList<>(checkedItems);
            for (TreeItem<T> item : items) {
                clearCheck(item);
            }
        }

        @Override
        public boolean isEmpty() {
            return checkedItems.isEmpty();
        }

        @Override
        public boolean isChecked(TreeItem<T> item) {
            return checkedItems.contains(item);
        }

        @Override
        public void check(TreeItem<T> item) {
            if (item instanceof CheckBoxTreeItem) {
                ((CheckBoxTreeItem<T>) item).setSelected(true);
            }
            if (!checkedItems.contains(item)) {
                checkedItems.add(item);
            }
        }

        @Override
        public void toggleCheckState(TreeItem<T> item) {
            if (isChecked(item)) {
                clearCheck(item);
            } else {
                check(item);
            }
        }

        /***********************************************************************
         *                                                                     *
         * Private Implementation                                              *
         *                                                                     *
         **********************************************************************/

        private void iterateOverTree(Consumer<TreeItem<T>> consumer) {
            processNode(consumer, root);
        }

        private void processNode(Consumer<TreeItem<T>> consumer, TreeItem<T> node) {
            if (node == null) {
                return;
            }
            consumer.accept(node);
            processChildren(consumer, node.getChildren());
        }

        private void processChildren(Consumer<TreeItem<T>> consumer, List<TreeItem<T>> children) {
            if (children == null) {
                return;
            }
            for (TreeItem<T> child : children) {
                processNode(consumer, child);
            }
        }
    }
}
