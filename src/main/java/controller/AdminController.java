package controller;

import model.User;
import model.validation.Notification;
import org.joda.time.DateTime;
import service.user.UserService;
import service.user.UserSession;
import view.AdminView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AdminController implements Observer, Observable{

    private final UserSession userSession;
    private final UserService userService;
    private final AdminView adminView;

    private final List<Observer> observers;

    public AdminController(UserSession userSession, UserService userService, AdminView adminView) {
        this.userSession = userSession;
        this.userService = userService;
        this.adminView = adminView;

        observers = new ArrayList<>();
        adminView.setAddUserBtnListener(new AddUserBtnListener());
        adminView.setRemoveUserBtnListener(new RemoveUserBtnListener());
        adminView.setUpdateUsernameBtnListener(new UpdateUsernameBtnListener());
        adminView.setUpdatePasswordBtnListener(new UpdatePasswordBtnListener());
        adminView.setGenerateReportBtnListener(new GenerateReportBtnLister());
        adminView.setBackBtnListener(new BackBtnListener());
    }

    @Override
    public void alert(Class toAlert) {
        if(getClass().equals(toAlert) && userSession.getLoggedInUser().
                getRole().getRole().equals("admin")) {
            setUserList();
            adminView.setVisible(true);
        }

    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void alertObservers(Class toAlert) {
        observers.forEach(o -> o.alert(toAlert));
    }

    private void setUserList() {
        List<User> users = userService.getAll().getResult();
        String[] usersAsStringArray = users.stream().map(User::getUsername).toArray(String[]::new);
        adminView.setUserList(usersAsStringArray);

    }



    private class AddUserBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            User user = new User();
            user.setUsername(adminView.getUsername());
            user.setPassword(adminView.getPassword());
            Notification<Boolean> notification = userService.addUser(user);

            if(notification.hasErrors()) {
                JOptionPane.showMessageDialog(adminView.getContentPane(), notification.getFormattedErrors());
            } else {
                JOptionPane.showMessageDialog(adminView.getContentPane(), "Added user successfully");
            }
            setUserList();
        }
    }

    private class RemoveUserBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Notification<Boolean> notification = userService.removeUser(adminView.getSelectedUsername());
            if(notification.hasErrors()) {
                JOptionPane.showMessageDialog(adminView.getContentPane(), notification.getFormattedErrors());
            } else {
                JOptionPane.showMessageDialog(adminView.getContentPane(), "Removed user successfully");
            }
            setUserList();
        }
    }

    private class UpdateUsernameBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Notification<Boolean> notification = userService.updateUserUsername(adminView.getSelectedUsername(),
                    adminView.getUsername());
            if(notification.hasErrors()) {
                JOptionPane.showMessageDialog(adminView.getContentPane(), notification.getFormattedErrors());
            } else {
                JOptionPane.showMessageDialog(adminView.getContentPane(), "Username updated successfully");
            }
            setUserList();
        }
    }

    private class UpdatePasswordBtnListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Notification<Boolean> notification = userService.updateUserPassword(adminView.getSelectedUsername(),
                    adminView.getPassword());
            if(notification.hasErrors()) {
                JOptionPane.showMessageDialog(adminView.getContentPane(), notification.getFormattedErrors());
            } else {
                JOptionPane.showMessageDialog(adminView.getContentPane(), "Password updated successfully");
            }
        }
    }
    private class GenerateReportBtnLister implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Notification<Boolean> notification = userService.generateReport(
                    new DateTime(adminView.getStartDate().getTime()),
                    new DateTime(adminView.getEndDate().getTime()),
                    adminView.getSelectedUsername()
            );
            if(notification.hasErrors()) {
                JOptionPane.showMessageDialog(adminView.getContentPane(), notification.getFormattedErrors());
            }
        }
    }
    private class BackBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            adminView.setVisible(false);
            userSession.endSession();
            alertObservers(LoginController.class);
        }
    }

}
