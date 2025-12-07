/*
 * Copyright (c) 2025 unknowIfGuestInDream.
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

package com.tlcsdm.qe.tools;

import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.qe.QeSample;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * QE lighting APN mdf Parse
 *
 * @author unknowIfGuestInDream
 */
public class APNMdfParse extends QeSample {
    @Override
    public String getSampleId() {
        return "apnMdfParse";
    }

    @Override
    public String getSampleName() {
        return "APN mdf Parse";
    }

    @Override
    public String getSampleVersion() {
        return "1.0.2";
    }

    @Override
    public Node getPanel(Stage stage) {
        return null;
    }

    @Override
    public String getOrderKey() {
        return "mdfParse";
    }

    //    @Override
    //    public String getSampleDescription() {
    //        return "";
    //    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/qe/static/icon/parse.png"));
    }

    @Override
    public void initializeBindings() {
        super.initializeBindings();
        //        BooleanBinding outputValidation = new TextInputControlEmptyBinding(outputField).build();
        //        BooleanBinding emptyValidation = new MultiTextInputControlEmptyBinding(excelField, outputField, macroLengthField, startCellField, generalFileCellField, endCellColumnField).build();
        //        BooleanBinding generalBinding = Bindings.createBooleanBinding(() ->
        //            onlyGenerateCheck.isSelected() || (!onlyGenerateCheck.isSelected() && generalField.getText().isEmpty()), generalField.textProperty(), onlyGenerateCheck.selectedProperty());
        //        diff.disabledProperty().bind(emptyValidation.and(generalBinding));
        //        openOutDir.disabledProperty().bind(outputValidation);
        //        mergeResultCheck.disableProperty().bindBidirectional(onlyGenerateCheck.selectedProperty());
        //        generalButton.disableProperty().bindBidirectional(onlyGenerateCheck.selectedProperty());
        //        generalField.disableProperty().bindBidirectional(onlyGenerateCheck.selectedProperty());
        //        FileChooserUtil.setOnDrag(excelField, FileChooserUtil.FileType.FILE);
        //        FileChooserUtil.setOnDrag(outputField, FileChooserUtil.FileType.FOLDER);
        //        FileChooserUtil.setOnDrag(generalField, FileChooserUtil.FileType.FOLDER);
    }

    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
        //        userData.put("mergeResult", mergeResultCheck);
        //        userData.put("onlyGenerate", onlyGenerateCheck);
        //        userData.put("excel", excelField);
        //        userData.put("excelFileChooser", excelFileChooser);
        //        userData.put("general", generalField);
        //        userData.put("generalChooser", generalChooser);
        //        userData.put("output", outputField);
        //        userData.put("outputChooser", outputChooser);
        //        userData.put("macroLength", macroLengthField);
        //        userData.put("ignoreSheet", ignoreSheetField);
        //        userData.put("markSheet", markSheetField);
        //        userData.put("startCell", startCellField);
        //        userData.put("generalFileCell", generalFileCellField);
        //        userData.put("endCellColumn", endCellColumnField);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
