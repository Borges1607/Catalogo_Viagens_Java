module mvc.cadastroViagens {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
    requires org.apache.commons.codec;

    opens mvc.cadastroViagens to javafx.fxml;
    exports mvc.cadastroViagens;

    opens mvc.cadastroViagens.controller to javafx.fxml;
    exports mvc.cadastroViagens.controller;

    opens mvc.cadastroViagens.model to javafx.fxml;
    exports mvc.cadastroViagens.model;
}