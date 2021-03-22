package view;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Date;

public class AdminView extends JFrame {
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel usersLabel;

    private JTextField usernameTF;
    private JTextField passwordTF;

    private JButton generateReportBtn;
    private JButton addUserBtn;
    private JButton removeUserBtn;
    private JButton updatePasswordBtn;
    private JButton updateUsernameBtn;
    private JButton backBtn;

    private JList<String> userJList;
    private JScrollPane userSP;


    private JDatePickerImpl datePickerStart;
    private JDatePickerImpl datePickerEnd;

    public AdminView() {
        setSize(300, 600);
        setLocationRelativeTo(null);
        initializeFields();
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        addFields();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(false);
    }


    private void initializeFields() {
        startDateLabel = new JLabel("start date");
        endDateLabel = new JLabel("end date");
        usernameLabel = new JLabel("username");
        passwordLabel = new JLabel("password");
        usersLabel = new JLabel("users");

        usernameTF = new JTextField();
        passwordTF = new JTextField();

        generateReportBtn = new JButton("Generate Report");
        addUserBtn = new JButton("Add user");
        removeUserBtn = new JButton("Remove user");
        updatePasswordBtn = new JButton("Update password");
        updateUsernameBtn = new JButton("Update username");
        backBtn = new JButton("Back");

        userJList = new JList<>();
        userSP = new JScrollPane(userJList);

        UtilDateModel model = new UtilDateModel();
        UtilDateModel model2 = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        JDatePanelImpl datePanel2 = new JDatePanelImpl(model2);
        datePickerStart = new JDatePickerImpl(datePanel);
        datePickerEnd = new JDatePickerImpl(datePanel2);


    }

    private void addFields() {
        add(startDateLabel);
        add(datePickerStart);

        add(endDateLabel);
         add(datePickerEnd);

        add(usernameLabel);
        add(usernameTF);

        add(passwordLabel);
        add(passwordTF);

        add(usersLabel);
        add(userSP);

        add(generateReportBtn);
        add(addUserBtn);
        add(removeUserBtn);
        add(updatePasswordBtn);
        add(updateUsernameBtn);
        add(backBtn);

    }

    public String getUsername() {
        return usernameTF.getText();
    }

    public String getPassword() {
        return passwordTF.getText();
    }

    public String getSelectedUsername() {
        return userJList.getSelectedValue();
    }

    public Date getStartDate() {
        return (Date)datePickerStart.getModel().getValue();
    }

    public Date getEndDate() {
        return (Date)datePickerEnd.getModel().getValue();
    }

    public void setGenerateReportBtnListener(ActionListener actionListener) {
        generateReportBtn.addActionListener(actionListener);
    }

    public void setUserList(String[] users) {
        userJList.setListData(users);
    }

    public void setAddUserBtnListener(ActionListener actionListener){
        addUserBtn.addActionListener(actionListener);
    }

    public void setRemoveUserBtnListener(ActionListener actionListener){
        removeUserBtn.addActionListener(actionListener);
    }

    public void setUpdateUsernameBtnListener(ActionListener actionListener){
        updateUsernameBtn.addActionListener(actionListener);
    }

    public void setUpdatePasswordBtnListener(ActionListener actionListener){
        updatePasswordBtn.addActionListener(actionListener);
    }

    public void setBackBtnListener(ActionListener actionListener) {
        backBtn.addActionListener(actionListener);
    }


}
