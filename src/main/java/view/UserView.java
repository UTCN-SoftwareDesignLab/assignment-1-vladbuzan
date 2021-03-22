package view;

import model.Client;

import javax.swing.*;
import java.awt.event.ActionListener;

public class UserView extends JFrame {
    private JLabel nameLabel;
    private JLabel idcnLabel;
    private JLabel pncLabel;
    private JLabel addressLabel;
    private JLabel typeLabel;
    private JLabel identifLabel;
    private JLabel amountLabel;


    private JTextField nameTF;
    private JTextField idcnTF;
    private JTextField pncTF;
    private JTextField addressTF;
    private JTextField typeTF;
    private JTextField identifTF;
    private JTextField amountTF;

    private JButton addClientBtn;
    private JButton removeClientBtn;
    private JButton updateClientBtn;
    private JButton updateClientAccountBtn;
    private JButton transferBtn;
    private JButton backBtn;

    private JList<Client> clientJList;
    private JScrollPane clientSP;

    public UserView() {
        setSize(300, 600);
        setLocationRelativeTo(null);
        initializeFields();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        addFields();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(false);
    }

    private void initializeFields() {
        nameLabel = new JLabel("name");
        idcnLabel = new JLabel("identity card number");
        pncLabel = new JLabel("personal numerical code");
        addressLabel = new JLabel("address");
        typeLabel = new JLabel("type");
        identifLabel = new JLabel("identification number");
        amountLabel = new JLabel("amount");

        nameTF = new JTextField();
        idcnTF = new JTextField();
        pncTF = new JTextField();
        addressTF = new JTextField();
        typeTF = new JTextField();
        identifTF = new JTextField();
        amountTF = new JTextField();

        addClientBtn = new JButton("Add Client");
        removeClientBtn = new JButton("Remove client");
        updateClientBtn = new JButton("Update client");
        updateClientAccountBtn = new JButton("Update client account");
        transferBtn = new JButton("Transfer");
        backBtn = new JButton("Back");

        clientJList = new JList<>();
        clientSP = new JScrollPane(clientJList);

    }

    private void addFields() {
        add(nameLabel);
        add(nameTF);

        add(idcnLabel);
        add(idcnTF);

        add(pncLabel);
        add(pncTF);

        add(addressLabel);
        add(addressTF);

        add(typeLabel);
        add(typeTF);

        add(identifLabel);
        add(identifTF);

        add(amountLabel);
        add(amountTF);

        add(clientSP);

        add(addClientBtn);
        add(removeClientBtn);
        add(updateClientBtn);
        add(updateClientAccountBtn);
        add(transferBtn);
        add(backBtn);
    }

    public String getName() {
        return nameTF.getText();
    }

    public String getIdcn() {
        return idcnTF.getText();
    }

    public String getPnc() {
        return pncTF.getText();
    }

    public String getAddress() {
        return addressTF.getText();
    }

    public String getAccountType() {
        return typeTF.getText();
    }

    public String getIdentif() {
        return identifTF.getText();
    }

    public String getAmount() {
        return amountTF.getText();
    }

    public Client getSelectedClient() {
        return clientJList.getSelectedValue();
    }

    public void setClientList(Client[] clientList) {
        clientJList.setListData(clientList);
    }

    public void setAddClientBtnListener(ActionListener actionListener) {
        addClientBtn.addActionListener(actionListener);
    }

    public void setRemoveClientBtnListener(ActionListener actionListener) {
        removeClientBtn.addActionListener(actionListener);
    }

    public void setUpdateClientBtnListener(ActionListener actionListener) {
        updateClientBtn.addActionListener(actionListener);
    }

    public void setUpdateClientAccountBtnListener(ActionListener actionListener) {
        updateClientAccountBtn.addActionListener(actionListener);
    }


    public void setTransferBtnListener(ActionListener actionListener) {
        transferBtn.addActionListener(actionListener);
    }

    public void setBackBtnListener(ActionListener actionListener) {
        backBtn.addActionListener(actionListener);
    }
}
