module com.tlcsdm.demo {

    requires java.desktop;
    requires org.controlsfx.controls;
    requires com.tlcsdm.frame;
    requires com.tlcsdm.login;

    exports com.tlcsdm.demo.samples to javafx.graphics;
    exports com.tlcsdm.demo.samples.actions to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.button to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.checked to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.dialogs to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.propertysheet to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.tablefilter to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.tableview to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.tableview2 to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.textfields to com.tlcsdm.frame;
    exports com.tlcsdm.demo.samples.spreadsheet to com.tlcsdm.frame;

    opens com.tlcsdm.demo.samples;
    opens com.tlcsdm.demo.samples.dialogs;
    opens com.tlcsdm.demo.samples.actions to org.controlsfx.controls;
    opens com.tlcsdm.demo.samples.tableview to javafx.base;
    opens com.tlcsdm.demo.samples.spreadsheet to javafx.graphics;

    provides com.tlcsdm.frame.FXSamplerProject with com.tlcsdm.demo.ControlsFXSamplerProject;
    provides com.tlcsdm.frame.MenubarConfigration with com.tlcsdm.demo.FXMenubarConfigration;
    provides com.tlcsdm.login.LoginCheck with com.tlcsdm.demo.ControlsLoginCheck;
}