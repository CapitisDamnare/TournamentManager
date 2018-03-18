package sample;

public class Person {

    private Controller controller;

    public Person(Controller controller) {
        this.controller = controller;
    }

    public void setLabel(String text) {
        controller.setRdyLabel(text);
    }
}
