package tapsi.logic;

import javafx.collections.ObservableList;

public class Group {

    private ObservableList<String> teams;
    private String groupName;

    public Group(ObservableList<String> teams, String groupName) {
        this.teams = teams;
        this.groupName = groupName;
    }
}
