package com.slh.quanlythuvienapp;

import com.slh.pojo.DocGia;
import com.slh.services.DocGiaService;
import com.slh.utils.MessageBox;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
//import javafx.scene.control.Button;
//import javafx.scene.control.ButtonType;
//import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class PrimaryController implements Initializable {

    static DocGiaService s = new DocGiaService();
    @FXML
    private TableView<DocGia> tbDocGia;
    @FXML
    private TextField txtKeyWord;
    @FXML
    private TextField txtTen;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtSdt;
    @FXML
    private TextField txtDiaChi;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            this.loadTableView();
            this.loadTableData(null);
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.txtKeyWord.textProperty().addListener(e -> {
            try {
                this.loadTableData(this.txtKeyWord.getText());
            } catch (SQLException ex) {
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    private void loadTableView() {
        TableColumn colId = new TableColumn("Id");
        colId.setCellValueFactory(new PropertyValueFactory("id"));

        TableColumn colDocGia = new TableColumn("Tên Đọc Giả");
        colDocGia.setCellValueFactory(new PropertyValueFactory("name"));

        TableColumn colEmail = new TableColumn("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));

        TableColumn colSDT = new TableColumn("Số điện thoại");

        colSDT.setCellValueFactory(new PropertyValueFactory("so_dien_thoai"));
        TableColumn colDiaChi = new TableColumn("Địa Chỉ");
        colDiaChi.setCellValueFactory(new PropertyValueFactory("dia_chi"));
        this.tbDocGia.getColumns().addAll(colId, colDocGia, colEmail, colSDT, colDiaChi);
    }

    private void loadTableData(String kw) throws SQLException {
        DocGiaService dg = new DocGiaService();
        this.tbDocGia.setItems(FXCollections.observableArrayList(dg.getDocGia(kw)));
    }
   

    public void addDocGia(ActionEvent evt) {
        DocGia dg = new DocGia(this.txtTen.getText(), this.txtEmail.getText(), this.txtSdt.getText(), this.txtDiaChi.getText());
        try {
            if (s.addUser(dg)) {
                this.loadTableData(null);
                MessageBox.getBox("Đọc giả", "Add successful", Alert.AlertType.INFORMATION).show();
            }
        } catch (SQLException ex) {
            MessageBox.getBox("Đọc giả", "Add failed", Alert.AlertType.ERROR).show();
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void hienThi_Click(MouseEvent evt) {
        DocGia current = tbDocGia.getSelectionModel().getSelectedItem();
        txtTen.setText(current.getName());
        txtDiaChi.setText(current.getDia_chi());
        txtEmail.setText(current.getEmail());
        txtSdt.setText(current.getSo_dien_thoai());
    }
    public void delete_Click(ActionEvent event) throws SQLException {
        DocGia current = tbDocGia.getSelectionModel().getSelectedItem();
       s.deleteDocGia(String.valueOf(current.getId()));
        clearinf();
        this.loadTableData(null);
    }
    public void upadate_click(ActionEvent evt) throws SQLException {
        DocGia current = tbDocGia.getSelectionModel().getSelectedItem();
        s.updateDocGia(current.getId(),txtTen.getText(), txtDiaChi.getText(),txtEmail.getText(),txtSdt.getText());
        clearinf();
        this.loadTableData(null);
    }
    public void clearinf() {
        txtTen.clear();
        txtDiaChi.clear();
        txtEmail.clear();
        txtSdt.clear();
    }
    
}
