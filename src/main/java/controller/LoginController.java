package controller;

import model.validation.Notification;
import service.user.AuthenticationService;
import view.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 18/03/2017.
 */
public class LoginController implements Observer, Observable{
    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    private final List<Observer> observers;

    public LoginController(LoginView loginView, AuthenticationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;

        observers = new ArrayList<>();
        loginView.setLoginButtonListener(new LoginButtonListener());
        loginView.setRegisterButtonListener(new RegisterButtonListener());
    }



    @Override
    public void alert(Class toAlert) {
        if(getClass().equals(toAlert)) {
            loginView.setVisible(true);
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

    private class LoginButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> notification = authenticationService.login(username, password);

            if(notification.hasErrors()) {
                JOptionPane.showMessageDialog(loginView.getContentPane(), notification.getFormattedErrors());
                return;
            } else {
                JOptionPane.showMessageDialog(loginView.getContentPane(), "Logged in successfully!");
            }
            alertObservers(AdminController.class);
            alertObservers(UserController.class);
            loginView.setVisible(false);
        }
    }

    private class RegisterButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);
            if (registerNotification.hasErrors()) {
                JOptionPane.showMessageDialog(loginView.getContentPane(), registerNotification.getFormattedErrors());
            } else {
                if (!registerNotification.getResult()) {
                    JOptionPane.showMessageDialog(loginView.getContentPane(), "Registration not successful, please try again later.");
                } else {
                    JOptionPane.showMessageDialog(loginView.getContentPane(), "Registration successful!");
                }
            }
        }
    }


}
