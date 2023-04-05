package com.slh.quanlythuvienapp;

import com.slh.pojo.MuonTra;
import com.slh.services.MuonTraService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class PrimaryController implements Initializable {

    static MuonTraService s = new MuonTraService();
    @FXML
    private ComboBox cbkey;
    ObservableList<String> list = FXCollections.observableArrayList("khach", "sach");
    @FXML
    private TableView<MuonTra> tbMuonTra;
    @FXML
    private TextField tfMDG;
    @FXML
    private TextField tfMDGName;
    @FXML
    private TextField tfMS;
    @FXML
    private TextField tfMSName;
    @FXML
    private DatePicker dpNM;
    @FXML
    private DatePicker dpNT;
    @FXML
    private TextArea tatong;
    @FXML
    private TextField tfs;
    @FXML
    private TextField tfs2;

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MuonTraService s = new MuonTraService();
        cbkey.setItems(list);
        cbkey.setValue("khach");
        dpNM.setValue(LocalDate.now());
        dpNT.setValue(LocalDate.now().plusDays(30));
        this.loadTableColumns();
        try {
            String m = tfs2.getText();
            this.loadTableData(null, m);
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tfs2.setText("khach");
        this.tfs.textProperty().addListener(e -> {
            try {
                String n = tfs2.getText();
                tfs2.setText(n);
                this.loadTableData(this.tfs.getText(), n);
            } catch (SQLException ex) {
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.dpNM.valueProperty().addListener(ev -> {
            if (dpNM.getValue() != null)
            {
                dpNT.setValue(dpNM.getValue().plusDays(30));
            }
        });
    }

    public void loadTableColumns() {
//        TableColumn colId = new TableColumn("STT");
//        colId.setCellValueFactory(new PropertyValueFactory("id"));

        TableColumn colMuon = new TableColumn("Ngay Muon");
        colMuon.setCellValueFactory(new PropertyValueFactory("ngaymuon"));

        TableColumn colTra = new TableColumn("Ngay Tra");
        colTra.setCellValueFactory(new PropertyValueFactory("ngaytra"));

        TableColumn colCustomer = new TableColumn("khach");
        colCustomer.setCellValueFactory(new PropertyValueFactory("name"));

        TableColumn colSach = new TableColumn("sach");
        colSach.setCellValueFactory(new PropertyValueFactory("sach_name"));

        this.tbMuonTra.getColumns().addAll(colMuon, colCustomer, colSach, colTra);
    }

    public void loadTableData(String kw, String key) throws SQLException {
        MuonTraService mt = new MuonTraService();
        this.tbMuonTra.setItems(FXCollections.observableList(mt.getMuonTra(kw, key)));
    }

    public void change(ActionEvent evt) {
        String k = String.valueOf(cbkey.getValue());
        tatong.setText(k);
        tfs2.setText(k);
    }

    public void tbMuoseClick(MouseEvent evt) {
        MuonTra current = tbMuonTra.getSelectionModel().getSelectedItem();
        tfMDG.setText(String.valueOf(current.getDocgia_id()));
        tfMDGName.setText(current.getName());
        tfMS.setText(String.valueOf(current.getSach_id()));
        tfMSName.setText(current.getSach_name());
        dpNM.setValue(current.getNgaymuon().toLocalDate());
        dpNT.setValue(current.getNgaymuon().toLocalDate().plusDays(30));
        //tatong.setText(String.valueOf(dpNT.getValue()));
    }

    public void deleteMuonTra(ActionEvent event) throws SQLException {
        MuonTraService mt = new MuonTraService();
        MuonTra current = tbMuonTra.getSelectionModel().getSelectedItem();
        mt.deleteMuonTra(String.valueOf(current.getId()));
        clearinf();
        this.loadTableData(null, "khach");
    }

    public void upadateMuonTra(ActionEvent evt) throws SQLException {
        MuonTraService mt = new MuonTraService();
        MuonTra current = tbMuonTra.getSelectionModel().getSelectedItem();
        mt.updateMuonTra(String.valueOf(current.getId()), String.valueOf(dpNM.getValue()), String.valueOf(dpNT.getValue()), tfMDG.getText(), tfMS.getText());
        clearinf();
        this.loadTableData(null, tfs2.getText());
    }

    public void addMuonTra(ActionEvent event) throws SQLException {
        MuonTraService mt = new MuonTraService();
        int MaDG = Integer.parseInt(tfMDG.getText());
        int MaS = Integer.parseInt(tfMS.getText());
        Date NM = Date.valueOf(dpNM.getValue());
        Date NT = Date.valueOf(dpNT.getValue().plusDays(30));
        tatong.setText(String.valueOf(NT));
        MuonTra m = new MuonTra(0, null, null, NM, NT, MaDG, MaS);
        mt.addMuonTra(m);
        clearinf();
        this.loadTableData(null, tfs2.getText());
    }

    public void clearinf() {
        tfMDG.clear();
        tfMDGName.clear();
        tfMS.clear();
        tfMSName.clear();
        dpNM.setValue(null);
        dpNT.setValue(null);
    }
}
