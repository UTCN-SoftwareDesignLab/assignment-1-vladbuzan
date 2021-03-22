package controller;

public interface Observable {
    void addObserver(Observer observer);
    void alertObservers(Class toAlert);

}
