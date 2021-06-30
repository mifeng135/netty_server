package com.game.logic.aoi;

import com.game.logic.model.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class EnterLeftInfo {
    private List<Grid> enterList;
    private List<Grid> leaveList;


    public List<Integer> getLeavePlayerIndexList() {
        List<Integer> list = new ArrayList<>();
        for (Grid grid : leaveList) {
            list.addAll(grid.getPlayerIndexList());
        }
        return list;
    }

    public List<Player> getPlayerEnterList() {
        List<Player> list = new ArrayList<>();
        for (Grid grid : leaveList) {
            list.addAll(grid.getPlayerList());
        }
        return list;
    }
}
