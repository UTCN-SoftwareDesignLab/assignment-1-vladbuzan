package view;

import model.Client;

import javax.swing.*;
import java.awt.event.ActionListener;

public class TransferView extends JFrame {
    private JLabel senderLabel;
    private JLabel receiverLabel;
    private JLabel amountLabel;

    private JTextField amountTF;

    private JList<Client> senderJList;
    private JScrollPane senderSP;

    private JList<Client> receiverJList;
    private JScrollPane receiverSP;

    private JButton transferBtn;
    private JButton backBtn;

    public TransferView(){
        setSize(300, 500);
        setLocationRelativeTo(null);
        initializeFields();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        addFields();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(false);
    }

    private void initializeFields(){
        senderLabel = new JLabel("sender");
        receiverLabel = new JLabel("receiver");
        amountLabel = new JLabel("amount");

        amountTF = new JTextField();
        senderJList = new JList<>();
        senderSP = new JScrollPane(senderJList);
        receiverJList = new JList<>();
        receiverSP = new JScrollPane(receiverJList);
        transferBtn = new JButton("Transfer");
        backBtn = new JButton("Back");

    }

    private void addFields(){
        add(senderLabel);
        add(senderSP);
        add(receiverLabel);
        add(receiverSP);
        add(amountLabel);
        add(amountTF);
        add(transferBtn);
        add(backBtn);
    }

    public void setSenderList(Client[] clients) {
        senderJList.setListData(clients);
    }

    public void setReceiverList(Client[] clients) {
        receiverJList.setListData(clients);
    }

    public Client getSelectedSender(){
        return senderJList.getSelectedValue();
    }

    public Client getSelectedReceiver() {
        return receiverJList.getSelectedValue();
    }

    public String getAmount() {
        return amountTF.getText();
    }

    public void setTransferBtnListener(ActionListener listener) {
        transferBtn.addActionListener(listener);
    }

    public void setBackBtnListener(ActionListener listener) {
        backBtn.addActionListener(listener);
    }
}
