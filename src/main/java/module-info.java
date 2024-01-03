module com.example.to_do_list {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;



    opens com.example.to_do_list to javafx.fxml;
    exports com.example.to_do_list;
    exports com.example.to_do_list.controller;
    opens com.example.to_do_list.controller to javafx.fxml;
    exports com.example.to_do_list.db;
    opens com.example.to_do_list.db to javafx.fxml;
    exports com.example.to_do_list.tm;
}